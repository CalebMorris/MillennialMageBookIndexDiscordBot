package io.honu.books.index.model

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import java.nio.file.Path

/**
 * @param indexDir Where the generated index files are stored
 * @param indexAnalyzer Which analyzer should be used when generating the index
 */
data class IndexConfig(
    val indexDir: Path,
    val indexAnalyzer: Analyzer = EnglishAnalyzer(),
)
