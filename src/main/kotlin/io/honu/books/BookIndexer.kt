package io.honu.books

import io.honu.books.command.IndexDirectoryCommand
import io.honu.books.command.QueryIndexCommand
import io.honu.books.index.model.IndexConfig
import org.apache.tika.exception.TikaException
import org.xml.sax.SAXException
import java.io.IOException
import kotlin.io.path.Path

class BookIndexer {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    val indexConfig = IndexConfig(indexPath = Path(".mm_demo/index"), sourcePath = Path(".mm_demo/source"))
//    indexExample(indexConfig)
    searchExample(indexConfig)
}

@Throws(IOException::class, SAXException::class, TikaException::class)
fun indexExample(indexConfig: IndexConfig): Unit {
    val indexDirectoryCommand = IndexDirectoryCommand(indexConfig)
    indexDirectoryCommand.clearCurrentIndex()
    indexDirectoryCommand.indexSourceFiles()
}

@Throws(IOException::class, SAXException::class, TikaException::class)
fun searchExample(indexConfig: IndexConfig): Unit {
    val queryIndexCommand = QueryIndexCommand(indexConfig)
    val results = queryIndexCommand.queryIndex("Dasgannach", false, 5)
    println(results)
}
