package io.honu.books.command

import io.honu.books.helper.json.JsonFileLoader
import io.honu.books.index.handler.LuceneBookIndexer
import io.honu.books.index.model.IndexConfig
import io.honu.books.parser.mm.MillennialMageEpubParser
import io.honu.books.parser.mm.MillennialMageParserConfigs
import io.honu.books.parser.model.BookIndexConfig
import java.io.File
import java.nio.file.Path

class IndexDirectoryCommand(
    indexConfig: IndexConfig,
) {

    private val indexer: LuceneBookIndexer = LuceneBookIndexer(indexConfig = indexConfig)
    private val parserConfig = MillennialMageParserConfigs.mmBookParserConfig

    fun indexSourceFiles(sourcePath: Path) {
        sourcePath
            .toFile()
            .walkTopDown()
            .filter { it.isFile }
            .filter { it.name.endsWith(".epub") }
            .map {
                val maybeConfig = File(it.path + ".json")
                val bookConfig: BookIndexConfig? =
                    if (maybeConfig.exists()) JsonFileLoader.load<BookIndexConfig>(maybeConfig.toPath()) else null

                if (bookConfig == null) println("Parsing file [$it]")
                else println("Parsing file [$it] with config [$maybeConfig]")

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

}