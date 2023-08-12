package io.honu.books.index.model

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import java.nio.file.Path

/**
 * @param indexPath Where the generated index files are stored
 * @param sourcePath Where the source files to be indexed are stored
 * @param indexAnalyzer Which analyzer should be used when generating the index
 */
data class IndexConfig(
    val indexPath: Path,
    val sourcePath: Path,
    val indexAnalyzer: Analyzer = EnglishAnalyzer(),
) {
    init {
        require(indexPath != sourcePath)
    }
}
