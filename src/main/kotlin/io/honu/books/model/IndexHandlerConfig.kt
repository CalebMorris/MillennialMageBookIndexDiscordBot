package io.honu.books.model

import io.honu.books.index.parser.BookSegment
import org.apache.lucene.analysis.Analyzer
import java.nio.file.Path

data class IndexHandlerConfig(
    val seriesName: String,
    val indexDir: Path,
    val bookSupplier: Sequence<BookSegment>,
    val analyzer: Analyzer,
)
