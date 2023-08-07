package io.honu.books.index.handler

import org.apache.lucene.document.Document
import org.apache.lucene.search.Explanation

data class LuceneIndexSearchResult(
    val docNumber: Int,
    val document: Document,
    val explanation: Explanation? = null
)