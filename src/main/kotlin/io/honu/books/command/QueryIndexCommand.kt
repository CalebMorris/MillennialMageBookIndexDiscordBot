package io.honu.books.command

import io.honu.books.index.handler.LuceneBookSearcher
import io.honu.books.index.model.IndexConfig
import io.honu.books.index.view.SearchResult

class QueryIndexCommand(
    indexConfig: IndexConfig,
) {
    private val searcher: LuceneBookSearcher = LuceneBookSearcher(indexConfig)

    fun queryIndex(
        query: String,
        explain: Boolean,
        maxResults: Int,
    ): List<SearchResult> = searcher.search(query, explain, maxResults)

}