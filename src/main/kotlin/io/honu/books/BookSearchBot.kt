package io.honu.books

import dev.kord.common.Color
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.gateway.ConnectEvent
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.message.EmbedBuilder
import io.honu.books.command.IndexDirectoryCommand
import io.honu.books.command.QueryIndexCommand
import io.honu.books.index.model.IndexConfig
import io.honu.books.index.view.SearchResult
import io.honu.books.parser.model.BookSourceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.Path

suspend fun main() = withContext(Dispatchers.Default) {
    val queryCommandPrefix = System.getenv("BOOK_INDEX_QUERY_PREFIX") ?: "!query"

    require(System.getenv("DISCORD_TOKEN") != null) { "[DISCORD_TOKEN] must not be empty" }
    val kord = Kord(System.getenv("DISCORD_TOKEN"))

    val indexConfig = IndexConfig(indexPath = Path(".mm_demo/index"), sourcePath = Path(".mm_demo/source"))
    val queryIndexCommand = QueryIndexCommand(indexConfig)
    val indexCommand = IndexDirectoryCommand(indexConfig)

    indexCommand.clearCurrentIndex()
    indexCommand.indexSourceFiles()
    val bookConfigs: Sequence<BookSourceConfig> = indexCommand.loadBookConfigs()
    val defaultThumbnailUrl =
        "https://www.royalroadcdn.com/public/covers-full/47826-millennial-mage-a-slice-of-life-progression.jpg"
    val thumbnailMap: Map<UInt, String> = bookConfigs.fold(mutableMapOf()) { acc, config ->
        if (config.bookIndex != null && config.thumbnailUrl != null) {
            acc[config.bookIndex] = config.thumbnailUrl
        }
        acc
    }

    kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read
        // ignore other bots, even ourselves. We only serve humans here!
        if (message.author?.isBot != false) return@on

        println("Message Created by ${this.message.author?.username}: ${message.content}")

        // check if our command is being invoked
        if (!message.content.startsWith(queryCommandPrefix)) return@on

        println("Should return pong")

        val queryString: String = message.content.removePrefix("!query ")
        println("Querying with string \"${queryString}\"")

        val results = queryIndexCommand.queryIndex(queryString, false, 50)

        val footer = EmbedBuilder.Footer()
        footer.text = "fetched by your local Archivist"

        val result1: SearchResult = results.first()

        val responseMessage = message.channel.createEmbed {
            this.title = "Search \"${queryString}\" (1/${results.size})"
            this.thumbnail = EmbedBuilder.Thumbnail().apply {
                this.url = thumbnailMap.getOrDefault(result1.bookIndex ?: 0, defaultThumbnailUrl)
            }
            this.color = Color(0x0099FF)
            this.footer = footer
            this.fields = mutableListOf(
                EmbedBuilder.Field().apply {
                    this.name = result1.bookTitle
                    this.inline = true
                },
                EmbedBuilder.Field().apply {
                    this.name = result1.chapterName
                    this.inline = true
                },
                EmbedBuilder.Field().apply {
                    this.name = ""
                    this.value = result1.segmentContent
                    this.inline = false
                },
            )
        }
        responseMessage.addReaction(ReactionEmoji.Unicode("❌"))
        responseMessage.addReaction(ReactionEmoji.Unicode("⬅️"))
        responseMessage.addReaction(ReactionEmoji.Unicode("⏹️"))
        responseMessage.addReaction(ReactionEmoji.Unicode("➡️"))
    }

    kord.on<ReactionAddEvent> {
        println("reaction: $this")
    }

    kord.on<DisconnectEvent> {
        println("disconnect: $this")
    }

    kord.on<ConnectEvent> {
        println("connect: $this")
    }

    kord.on<ReadyEvent> {
        println("connect: $this")
    }

    kord.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent

        println("Starting up BookIndex Bot")
        println("Query index set to [${queryCommandPrefix}]")
    }
}