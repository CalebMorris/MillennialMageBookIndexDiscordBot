package io.honu.books.index.view

data class SearchResult(
    val bookIndex: UInt?,
    val bookTitle: String,
    val chapterName: String,
    val segmentContent: String,
    val explanation: String? = null,
)
