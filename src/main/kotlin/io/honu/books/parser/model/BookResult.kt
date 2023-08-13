package io.honu.books.parser.model

/**
 * The result set for the entire book
 * @param bookIndex Which number book is this? <code>null</code> indicates the current serial
 * @param bookTitle What is the title of the book?
 * @param metaData The extracted metadata pulled for the entire book
 * @param chapters The chapter information
 */
data class BookResult(
    val bookIndex: UInt?,
    val bookTitle: String?,
    val metaData: Map<String, String>,
    val chapters: List<BookChapter>,
)