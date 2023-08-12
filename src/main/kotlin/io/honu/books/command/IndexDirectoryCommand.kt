package io.honu.books.command

import io.honu.books.helper.json.JsonFileLoader
import io.honu.books.index.handler.LuceneBookIndexer
import io.honu.books.index.model.IndexConfig
import io.honu.books.parser.mm.MillennialMageEpubParser
import io.honu.books.parser.mm.MillennialMageParserConfigs
import io.honu.books.parser.model.BookIndexConfig
import java.io.File
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

class IndexDirectoryCommand(
    private val indexConfig: IndexConfig,
) {

    private val indexer: LuceneBookIndexer = LuceneBookIndexer(indexConfig = indexConfig)
    private val parserConfig = MillennialMageParserConfigs.mmBookParserConfig

    @OptIn(ExperimentalPathApi::class)
    fun clearCurrentIndex() {
        indexConfig.indexPath.deleteRecursively()
    }

    fun indexSourceFiles() {
        indexConfig.sourcePath
            .toFile()
            .walkTopDown()
            .filter { it.isFile }
            .filter { it.name.endsWith(".epub") }
            .map {
                val bookConfig: BookIndexConfig? = getConfig(it)
                val parser = MillennialMageEpubParser(
                    parserConfig.copy(
                        metaDataOverrides = if (bookConfig != null) mapOf("dc:title" to bookConfig.bookTitle) else mapOf()
                    )
                )
                parser.parse(it.toPath())
            }
            .forEach {
                println("Indexing file [${it.bookTitle}]")
                indexer.indexBook(it)
            }
    }

    private fun getConfig(baseFile: File): BookIndexConfig? {
        val maybeFileSpecificConfig = File(baseFile.path + ".json")
        if (maybeFileSpecificConfig.exists()) {
            println("Found file config @ $maybeFileSpecificConfig")
            return JsonFileLoader.load<BookIndexConfig>(maybeFileSpecificConfig.toPath())
        }
        val maybeDirConfig = File(baseFile.toPath().parent.toFile().path + "/config.json")
        if (maybeDirConfig.exists()) {
            println("Found directory config @ $maybeDirConfig")
            return JsonFileLoader.load<BookIndexConfig>(maybeDirConfig.toPath())
        }
        return null
    }

}