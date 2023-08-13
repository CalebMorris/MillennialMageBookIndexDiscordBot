package io.honu.books.bot

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Message
import dev.kord.core.event.gateway.ConnectEvent
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.message.EmbedBuilder
import io.honu.books.bot.ResponseExtensions.addInputReactions
import io.honu.books.bot.ResponseExtensions.removeInputReactions
import io.honu.books.bot.mm.MillennialMageThumbnailResolver
import io.honu.books.bot.view.SearchResultEmbedBuilder
import io.honu.books.command.IndexDirectoryCommand
import io.honu.books.command.QueryIndexCommand
import io.honu.books.index.model.IndexConfig
import io.honu.books.index.view.SearchResult
import io.honu.books.parser.model.BookSourceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


class IndexBot(
    private val queryCommandPrefix: String,
    indexConfig: IndexConfig,
    private val kord: Kord,
) {

    private val logger: Logger = LoggerFactory.getLogger(IndexBot::class.java)
    private val queryIndexCommand = QueryIndexCommand(indexConfig)
    private val indexCommand = IndexDirectoryCommand(indexConfig)

    val activeMessages: Cache<Snowflake, ActiveBotMessage> = CacheBuilder.newBuilder()
        .maximumSize(10000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<Snowflake, ActiveBotMessage>()

    init {
        logger.info("Initializing IndexBot with {queryCommandPrefix=${queryCommandPrefix},indexConfig=${indexConfig}}")
    }

    suspend fun start() = withContext(Dispatchers.Default) {
        indexCommand.clearCurrentIndex()
        indexCommand.indexSourceFiles()
        val bookConfigs: Sequence<BookSourceConfig> = indexCommand.loadBookConfigs()
        val thumbnailResolver = MillennialMageThumbnailResolver(bookConfigs)
        val searchResultEmbedBuilder = SearchResultEmbedBuilder(thumbnailResolver)

        kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read
            // ignore other bots, even ourselves. We only serve humans here!
            if (message.author?.isBot != false) return@on

            logger.info("Message Created by ${this.message.author?.username}: ${message.content}")

            // check if our command is being invoked
            if (!message.content.startsWith(queryCommandPrefix)) return@on

            logger.info("Should return pong")

            val queryString: String = message.content.removePrefix("!query ")
            logger.info("Querying with string \"${queryString}\"")

            val results: List<SearchResult> = queryIndexCommand.queryIndex(queryString, false, 50)
            val result1: SearchResult = results.first()

            val responseMessage: Message = message.channel.createEmbed(
                searchResultEmbedBuilder.buildEmbed(queryString, 1, results.size, result1)
            )

            message.author?.id?.let {
                activeMessages.put(
                    responseMessage.id, ActiveBotMessage(
                        channelId = responseMessage.channelId,
                        messageId = responseMessage.id,
                        initiatingMessageId = message.id,
                        requestingUserId = it,
                        queryString = queryString,
                        queryPosition = 0,
                        results = results,
                    )
                )
            }
            responseMessage.addInputReactions()
        }

        kord.on<ReactionAddEvent> {
            if (this.getUser().isBot) return@on

            logger.info("reaction: $this")

            val activeMessage = activeMessages.getIfPresent(this.messageId) ?: return@on

            logger.info("Reaction on active message!")
            when (this.emoji.name) {
                "❌" -> {
                    logger.info("Close this message")
                    activeMessages.invalidate(this.messageId)
                    this.getMessage().delete("User requested removal")
                }

                "⬅️" -> {
                    logger.info("Previous page")
                    if (activeMessage.queryPosition <= 1) return@on
                    val previousActiveMessage = activeMessage.copy(queryPosition = activeMessage.queryPosition - 1)
                    activeMessages.put(activeMessage.messageId, previousActiveMessage)
                    this.message.edit {
                        this.embeds =
                            mutableListOf(EmbedBuilder().apply(searchResultEmbedBuilder.buildEmbed(previousActiveMessage)))
                    }
                }

                "⏹️" -> {
                    logger.info("This is the right page!")
                    this.getMessage().removeInputReactions()
                }

                "➡️" -> {
                    logger.info("Next page")
                    if (activeMessage.queryPosition >= (activeMessage.results.size - 1)) return@on
                    val nextActiveMessage = activeMessage.copy(queryPosition = activeMessage.queryPosition + 1)
                    activeMessages.put(activeMessage.messageId, nextActiveMessage)
                    this.message.edit {
                        this.embeds =
                            mutableListOf(EmbedBuilder().apply(searchResultEmbedBuilder.buildEmbed(nextActiveMessage)))
                    }
                }
            }
        }

        kord.on<DisconnectEvent> {
            logger.info("disconnect: $this")
        }

        kord.on<ConnectEvent> {
            logger.info("connect: $this")
        }

        kord.on<ReadyEvent> {
            logger.info("connect: $this")
        }

        kord.login {
            // we need to specify this to receive the content of messages
            @OptIn(PrivilegedIntent::class)
            intents += Intent.MessageContent

            logger.info("Starting up BookIndex Bot")
            logger.info("Query index set to [${queryCommandPrefix}]")
        }
    }

}