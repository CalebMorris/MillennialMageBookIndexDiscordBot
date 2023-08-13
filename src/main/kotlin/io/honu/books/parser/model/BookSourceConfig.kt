package io.honu.books.parser.model

import kotlinx.serialization.Serializable

@Serializable
data class BookSourceConfig(
    val bookIndex: UInt? = null,
    val bookTitle: String? = null,
    val thumbnailUrl: String? = null,
)
