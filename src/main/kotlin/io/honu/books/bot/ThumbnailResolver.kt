package io.honu.books.bot

interface ThumbnailResolver {

    val defaultThumbnailUrl: String

    fun getThumbnailForBookIndex(index: UInt): String

}