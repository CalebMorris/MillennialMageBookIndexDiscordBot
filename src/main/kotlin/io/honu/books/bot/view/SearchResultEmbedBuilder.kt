package io.honu.books.bot.view

import dev.kord.common.Color
import dev.kord.rest.builder.message.EmbedBuilder
import io.honu.books.bot.ActiveBotMessage
import io.honu.books.bot.ThumbnailResolver
import io.honu.books.index.view.SearchResult

class SearchResultEmbedBuilder(
    private val thumbnailResolver: ThumbnailResolver,
) {

    fun buildEmbed(activeMessage: ActiveBotMessage): EmbedBuilder.() -> Unit = buildEmbed(
        queryString = activeMessage.queryString,
        queryIndex = activeMessage.queryPosition + 1,
        queryTotalResults = activeMessage.results.size,
        searchResult = activeMessage.results[activeMessage.queryPosition]
    )

    fun buildEmbed(
        queryString: String,
        queryIndex: Int,
        queryTotalResults: Int,
        searchResult: SearchResult
    ): EmbedBuilder.() -> Unit {
        return {
            this.title = "Search \"${queryString}\" (${queryIndex}/${queryTotalResults})"
            this.thumbnail = EmbedBuilder.Thumbnail().apply {
                this.url = thumbnailResolver.getThumbnailForBookIndex(searchResult.bookIndex ?: 0U)
            }
            this.color = Color(0x0099FF)
            this.footer = EmbedBuilder.Footer().apply {
                this.text = "fetched by your local Archivist"
                this.icon = "https://cdn.discordapp.com/emojis/1140113649801908367.webp"
            }
            this.fields = mutableListOf(
                EmbedBuilder.Field().apply {
                    this.name = searchResult.bookTitle
                    this.inline = true
                },
                EmbedBuilder.Field().apply {
                    this.name = searchResult.chapterName
                    this.inline = true
                },
                EmbedBuilder.Field().apply {
                    this.name = ""
                    this.inline = false
                },
                EmbedBuilder.Field().apply {
                    this.name = ""
                    this.value = searchResult.segmentContent.boldDesiredWords(queryString.splitToSequence(" "))
                    this.inline = false
                },
                EmbedBuilder.Field().apply {
                    this.name = ""
                    this.inline = false
                },
            )
        }
    }

    private fun String.boldDesiredWords(terms: Sequence<String>): String =
        terms.distinct().fold(this) { acc, term ->
            acc.replace(Regex(term, RegexOption.IGNORE_CASE)) { matchResult: MatchResult -> "**${matchResult.value}**" }
        }

}