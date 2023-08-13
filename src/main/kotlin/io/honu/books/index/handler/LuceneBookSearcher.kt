package io.honu.books.index.handler

import io.honu.books.index.const.BookIndexDocFields
import io.honu.books.index.model.IndexConfig
import io.honu.books.index.view.SearchResult
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.Term
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.AutomatonQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.MMapDirectory
import org.apache.lucene.util.automaton.LevenshteinAutomata
import java.io.IOException

class LuceneBookSearcher(
    private val indexConfig: IndexConfig
) {

    private val idx = MMapDirectory(indexConfig.indexPath)

    fun search(
        queryString: String,
        explain: Boolean,
        maxResults: Int,
    ): List<SearchResult> {
        DirectoryReader.open(idx).use { reader ->
            val searcher = IndexSearcher(reader)
            val results = searchByEnglishParser(queryString, searcher, explain, maxResults)
            if (results.isNotEmpty()) return results
            return searchByFuzzyTerms(queryString, searcher, explain, maxResults)
        }
    }

    @Throws(ParseException::class, IOException::class)
    private fun searchByEnglishParser(
        queryString: String,
        searcher: IndexSearcher,
        explain: Boolean,
        maxResults: Int
    ): List<SearchResult> {
        val parser = QueryParser(BookIndexDocFields.SEGMENT_CONTENT, indexConfig.indexAnalyzer)
        val query: Query = parser.parse(QueryParser.escape(queryString))
        val hits: TopDocs = searcher.search(query, maxResults)

        return hits.scoreDocs.map {
            LuceneIndexSearchResult(
                it.doc,
                searcher.doc(it.doc),
                if (explain) searcher.explain(query, it.doc) else null
            ).asSearchView()
        }
    }

    private fun searchByFuzzyTerms(
        queryString: String,
        searcher: IndexSearcher,
        explain: Boolean,
        maxResults: Int
    ): List<SearchResult> {
        val term = Term(BookIndexDocFields.SEGMENT_CONTENT, queryString);
        val fuzzyAutomation = LevenshteinAutomata(queryString, true).toAutomaton(3)
        val query = AutomatonQuery(term, fuzzyAutomation);

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
        bookIndex = this.document.get(BookIndexDocFields.BOOK_INDEX)?.toUInt(),
        bookTitle = this.document.get(BookIndexDocFields.BOOK_TITLE) ?: "",
        chapterName = this.document.get(BookIndexDocFields.CHAPTER_NAME) ?: "",
        segmentContent = this.document.get(BookIndexDocFields.SEGMENT_CONTENT) ?: "",
        explanation = this.explanation?.toString(),
    )

}
