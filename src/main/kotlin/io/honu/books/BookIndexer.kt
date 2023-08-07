package io.honu.books

import io.honu.books.index.handler.LuceneBookIndexer
import io.honu.books.index.handler.LuceneBookSearcher
import io.honu.books.index.model.IndexConfig
import io.honu.books.parser.mm.MillennialMageEpubParser
import io.honu.books.parser.model.ParserConfig
import org.apache.tika.Tika
import org.apache.tika.exception.TikaException
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.epub.EpubParser
import org.apache.tika.sax.*
import org.xml.sax.ContentHandler
import org.xml.sax.SAXException
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.StringWriter
import java.nio.charset.Charset
import kotlin.io.path.Path
import kotlin.io.path.absolute


//import org.apache.tika.parser.EpubParser

class BookIndexer {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    parseExample()
}

@Throws(IOException::class, SAXException::class, TikaException::class)
fun parseExample(): Unit {

    val indexPath = Path(".mm_demo/index")
    println("Index will be loaded into: ${indexPath.absolute()}")

    val indexConfig = IndexConfig(indexDir = Path(".mm_demo/index"))
//    val indexer: LuceneBookIndexer = LuceneBookIndexer(indexConfig = indexConfig)
    val searcher: LuceneBookSearcher = LuceneBookSearcher(indexConfig)

//    val mmBookParserConfig: ParserConfig = ParserConfig(
//        firstChapter = "Chapter: 1",
//        tableOfContentsRegex = "^(Contents)|(Table of Contents)$",
//        chapterRegex = "^Chapter(:.*\\d+)?$",
//        filteredSections = listOf("Table of Contents", "Contents", "Author’s Note"),
//    )
//    val mmBook1Parser = MillennialMageEpubParser(mmBookParserConfig.copy(
//        metaDataOverrides = mapOf("dc:title" to "Mageling")
//    ))
//
//    indexer.indexBook(
//        mmBook1Parser.parse(Path(".mm_demo/Millennial Mage 1 - Mageling.epub"))
//    )

    val results = searcher.search("Grediv")
    println(results)

}
