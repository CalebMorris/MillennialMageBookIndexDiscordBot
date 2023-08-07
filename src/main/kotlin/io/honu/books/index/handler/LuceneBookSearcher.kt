package io.honu.books.index.handler

import io.honu.books.index.const.BookIndexDocFields
import io.honu.books.index.model.IndexConfig
import io.honu.books.index.view.SearchResult
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.MMapDirectory
import java.io.IOException

class LuceneBookSearcher(
    private val indexConfig: IndexConfig
) {

    private val idx = MMapDirectory(indexConfig.indexDir)

    fun search(
        queryString: String,
        explain: Boolean = false,
        maxResults: Int = 5
    ): List<SearchResult> {
        DirectoryReader.open(idx).use { reader ->
            val searcher = IndexSearcher(reader)
            return search(queryString, searcher, explain, maxResults)
        }
    }

    @Throws(ParseException::class, IOException::class)
    private fun search(
        queryString: String,
        searcher: IndexSearcher,
        explain: Boolean,
        maxResults: Int
    ): List<SearchResult> {
        val parser = QueryParser(BookIndexDocFields.SEGMENT_CONTENT, indexConfig.indexAnalyzer)
        val query: Query = parser.parse(queryString)
        val hits: TopDocs = searcher.search(query, maxResults)

        return hits.scoreDocs.map {
            LuceneIndexSearchResult(
                it.doc,
                searcher.doc(it.doc),
                if (explain) searcher.explain(query, it.doc) else null
            ).asSearchView()
        }
    }

    private fun LuceneIndexSearchResult.asSearchView(): SearchResult = SearchResult(
        bookTitle = this.document.get(BookIndexDocFields.BOOK_TITLE),
        chapterName = this.document.get(BookIndexDocFields.CHAPTER_NAME),
        segmentContent = this.document.get(BookIndexDocFields.SEGMENT_CONTENT),
        explanation = this.explanation?.toString(),
    )

}
