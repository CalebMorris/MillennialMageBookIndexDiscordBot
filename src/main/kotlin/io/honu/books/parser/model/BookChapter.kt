package io.honu.books.parser.model

/**
 * @param chapterName The name of the chapter itself
 * @param segments The extracted content segments for the chapter
 */
data class BookChapter(
    val chapterName: String,
    val segments: List<String>
)