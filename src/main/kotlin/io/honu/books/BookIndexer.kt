package io.honu.books

import io.honu.books.command.IndexDirectoryCommand
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
    parseExample()
}

@Throws(IOException::class, SAXException::class, TikaException::class)
fun parseExample(): Unit {
    val indexConfig = IndexConfig(indexDir = Path(".mm_demo/index"))
    val indexDirectoryCommand = IndexDirectoryCommand(indexConfig)
    indexDirectoryCommand.indexSourceFiles(Path(".mm_demo/source"))
}
