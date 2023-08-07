package io.honu.books.parser.model

/**
 * The result set for the entire book
 * @param metaData The extracted metadata pulled for the entire book
 * @param chapters The chapter information
 */
data class BookResult(
    val bookTitle: String,
    val metaData: Map<String, String>,
    val chapters: List<BookChapter>,
)