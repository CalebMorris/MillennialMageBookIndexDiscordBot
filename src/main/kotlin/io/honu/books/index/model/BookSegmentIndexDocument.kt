package io.honu.books.index.model

data class BookSegmentIndexDocument(
    val bookIndex: UInt?,
    val bookTitle: String,
    val chapterIndex: Int,
    val chapterName: String,
    val segmentIndex: Int,
    val segmentContent: String,
)