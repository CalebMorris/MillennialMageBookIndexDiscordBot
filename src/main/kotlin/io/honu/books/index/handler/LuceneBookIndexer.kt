package io.honu.books.index.handler

import io.honu.books.index.const.BookIndexDocFields
import io.honu.books.index.model.BookSegmentIndexDocument
import io.honu.books.index.model.IndexConfig
import io.honu.books.parser.model.BookResult
import org.apache.lucene.document.*
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.MMapDirectory

class LuceneBookIndexer(
    private val indexConfig: IndexConfig,
    indexWriterConfig: IndexWriterConfig? = null,
) {

    private val writerConfig: IndexWriterConfig =
        indexWriterConfig ?: IndexWriterConfig(indexConfig.indexAnalyzer).setOpenMode(IndexWriterConfig.OpenMode.CREATE)

    fun indexBook(
        book: BookResult,
    ) {
        val idx = MMapDirectory(indexConfig.indexDir)
        val writer = IndexWriter(idx, writerConfig)
        for (bookSegmentIndexDoc in bookResultToIndexDocument(bookResult = book)) {
            writer.addDocument(bookSegmentIndexDoc.asLuceneDocument())
        }
        writer.close()
    }

    private fun bookResultToIndexDocument(bookResult: BookResult): Sequence<BookSegmentIndexDocument> = sequence {
        val title: String = bookResult.bookTitle
        for ((chapterIndex, chapter) in bookResult.chapters.withIndex()) {
            for ((segmentIndex, segment) in chapter.segments.withIndex()) {
                yield(
                    BookSegmentIndexDocument(
                        bookTitle = title,
                        chapterIndex = chapterIndex,
                        chapterName = chapter.chapterName,
                        segmentIndex = segmentIndex,
                        segmentContent = segment,
                    )
                )
            }
        }
    }

    private fun BookSegmentIndexDocument.asLuceneDocument(): Document {
        val doc = Document()
        doc.add(StringField(BookIndexDocFields.BOOK_TITLE, this.bookTitle, Field.Store.YES))
        doc.add(StringField(BookIndexDocFields.CHAPTER_NAME, this.chapterName, Field.Store.YES))
        doc.add(IntPoint(BookIndexDocFields.CHAPTER_INDEX, this.chapterIndex))
        doc.add(IntPoint(BookIndexDocFields.SEGMENT_INDEX, this.segmentIndex))
        doc.add(TextField(BookIndexDocFields.SEGMENT_CONTENT, this.segmentContent, Field.Store.YES))
        return doc
    }

}