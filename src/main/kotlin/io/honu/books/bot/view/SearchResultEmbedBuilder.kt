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
                text = "fetched by your local Archivist"
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
                    this.value = searchResult.segmentContent
                    this.inline = false
                },
            )
        }
    }

}