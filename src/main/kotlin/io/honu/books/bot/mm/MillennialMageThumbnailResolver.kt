package io.honu.books.bot.mm

import io.honu.books.bot.ThumbnailResolver
import io.honu.books.parser.model.BookSourceConfig

class MillennialMageThumbnailResolver(
    mmBookConfigs: Sequence<BookSourceConfig>
) : ThumbnailResolver {

    private val thumbnailMap: Map<UInt, String> = mmBookConfigs.fold(mutableMapOf()) { acc, config ->
        if (config.bookIndex != null && config.thumbnailUrl != null) {
            acc[config.bookIndex] = config.thumbnailUrl
        }
        acc
    }

    override val defaultThumbnailUrl: String =
        "https://www.royalroadcdn.com/public/covers-full/47826-millennial-mage-a-slice-of-life-progression.jpg"

    override fun getThumbnailForBookIndex(index: UInt): String = thumbnailMap.getOrDefault(index, defaultThumbnailUrl)
}