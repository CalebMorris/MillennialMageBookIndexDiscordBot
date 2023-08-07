package io.honu.books.parser.model

import kotlinx.serialization.Serializable

@Serializable
data class BookIndexConfig(
    val bookTitle: String,
)
