package io.honu.books

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
//    val path = ".mm_demo/Millennial Mage 1 - Mageling.epub"
//    val testFile = File(path)
//    val mmParser = MillennialMageEpubParser({x -> true}, listOf(), listOf(),
//        parserConfig = ParserConfig(
//            firstChapter = "Chapter: 1",
//            chapterRegex = "^Chapter(:.*\\d+)?$",
//            filteredSections = listOf("Table of Contents", "Contents")
//        )
//    )
//    val results = mmParser.parse(testFile.toPath())
//
//    for (chapter in results.chapters.take(3)) {
//        println(chapter.chapterName)
//        println("First Line: ${chapter.segments.first()}")
//        println("Last Line: ${chapter.segments.last()}")
//        println("----")
//    }

//    val parser = EpubParser()
//    val handler = BodyContentHandler(-1)
//    val metadata = Metadata()
//    val parseContext = ParseContext()
//
//    val path = ".mm_demo/Millennial Mage 1 - Mageling.epub"
//    val testFile = File(path)
//
//    parser.parse(testFile.inputStream(), handler, metadata, parseContext);
//
//    println("--${testFile.absolutePath}--")
//
//    println("--MetaData--")
//    val metadataNames = metadata.names()
//    for (name in metadataNames) {
//        println(name + ": " + metadata[name])
//    }
//
//    val content: String = handler.toString()
//    val lines: List<String> = content.split("\n")
//
//    var hasBegun = false
//    var ch1Count = 0
//    val final = lines.foldRight(ChapterInfo(-1, 0)){ line, chapterInfo ->
//        if (!hasBegun) {
//            if (line.contains(Regex("Chapter: \\d+"))) {
//                if (ch1Count == 1) hasBegun = true
//                ch1Count = 1
//            }
//            chapterInfo
//        } else {
//            if (line.contains(Regex("Chapter: \\d+"))) {
//                println(chapterInfo)
//                ChapterInfo(chapterInfo.chapterNumber + 1, 0)
//            } else {
//                chapterInfo.copy(segmentCount = chapterInfo.segmentCount + 1)
//            }
//        }
//    }
//    println(final)
//
////    println("${content.split("\n").size} lines")
//    println(content.take(1000))
}
