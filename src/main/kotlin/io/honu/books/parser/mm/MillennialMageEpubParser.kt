package io.honu.books.parser.mm

import io.honu.books.helper.json.JsonFileLoader
import io.honu.books.parser.model.BookChapter
import io.honu.books.parser.model.BookResult
import io.honu.books.parser.model.BookSourceConfig
import io.honu.books.parser.model.ParserConfig
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.epub.EpubParser
import org.apache.tika.sax.BodyContentHandler
import org.apache.tika.sax.SafeContentHandler
import java.io.File
import java.nio.file.Path

class MillennialMageEpubParser(
    private val parserConfig: ParserConfig,
) {
    private val chapterRegex = Regex(parserConfig.chapterRegex)
    private val tableOfContentsRegex = Regex(parserConfig.tableOfContentsRegex)

    private val parser = EpubParser()
    private val handler = SafeContentHandler(BodyContentHandler(-1))
    private val metadata = Metadata()
    private val parseContext = ParseContext()

    fun parse(filePath: Path): BookResult {
        val config: BookSourceConfig = getConfig(filePath)
            ?: throw Error("Unable to resolved configuration for [${filePath}]")
        return parseContent(parseEpub(filePath), config)
    }

    private fun parseEpub(filePath: Path): BookContent {
        val testFile: File = filePath.toFile()
        parser.parse(testFile.inputStream(), handler, metadata, parseContext)
        return BookContent(
            textContent = handler.toString(),
            metaData = metadata.names().fold(mutableMapOf()) { acc, name ->
                acc[name] = metadata[name]
                acc
            }
        )
    }

    private fun isChapterStart(line: String, currentHeading: String): Boolean {
        return if (currentHeading.matches(tableOfContentsRegex) && line != parserConfig.firstChapter) {
            false
        } else {
            line.matches(chapterRegex) || line.matches(tableOfContentsRegex) || parserConfig.filteredSections.contains(
                line
            )
        }
    }

    private fun parseContent(content: BookContent, config: BookSourceConfig): BookResult {
        val lines: List<String> = content.textContent.lines().reversed()
        val chapters: Sequence<BookChapter> = sequence {
            val final = lines.foldRight(Pair("Title Page", mutableListOf<String>())) { line, acc ->
                val normalizedLine = line.trim()
                if (normalizedLine.isBlank()) {
                    acc
                } else if (isChapterStart(normalizedLine, acc.first)) {
                    yield(
                        BookChapter(
                            chapterName = acc.first,
                            segments = acc.second.toList(),
                        )
                    )
                    Pair(normalizedLine, mutableListOf())
                } else {
                    acc.second.add(normalizedLine)
                    acc
                }
            }
            yield(
                BookChapter(
                    chapterName = final.first,
                    segments = final.second.toList(),
                )
            )
        }
        val mergedMetaData: Map<String, String> = content.metaData + parserConfig.metaDataOverrides
        return BookResult(
            bookIndex = config.bookIndex,
            bookTitle = mergedMetaData.getOrDefault("dc:title", ""),
            metaData = mergedMetaData,
            chapters = chapters
                .toList()
                .filterNot { c -> parserConfig.filteredSections.contains(c.chapterName) },
        )
    }

    private fun getConfig(basePath: Path): BookSourceConfig? {
        val maybeFileSpecificConfig = File(basePath.toFile().path + ".json")
        if (maybeFileSpecificConfig.exists()) {
            println("Found file config @ $maybeFileSpecificConfig")
            return JsonFileLoader.load<BookSourceConfig>(maybeFileSpecificConfig.toPath())
        }
        val maybeDirConfig = File(basePath.parent.toFile().path + "/config.json")
        if (maybeDirConfig.exists()) {
            println("Found directory config @ $maybeDirConfig")
            return JsonFileLoader.load<BookSourceConfig>(maybeDirConfig.toPath())
        }
        return null
    }

    private data class BookContent(
        val textContent: String,
        val metaData: Map<String, String>
    )

}
