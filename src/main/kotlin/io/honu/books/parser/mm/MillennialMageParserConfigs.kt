package io.honu.books.parser.mm

import io.honu.books.parser.model.ParserConfig

object MillennialMageParserConfigs {

    val mmBookParserConfig: ParserConfig = ParserConfig(
        firstChapter = "Chapter: 1",
        tableOfContentsRegex = "^(Contents)|(Table of Contents)$",
        chapterRegex = "^Chapter(:.*\\d+)?$",
        filteredSections = listOf("Table of Contents", "Contents", "Author’s Note", "Title Page"),
    )

    val mmRoyalRoadBookParser: ParserConfig = ParserConfig(
        firstChapter = "",
        tableOfContentsRegex = "^(Contents)|(Table of Contents)$",
        chapterRegex = "^Chapter(:.*\\d+)? - .*$",
        filteredSections = listOf("Information", "Table of Contents", "Title Page"),
        metaDataOverrides = mapOf("dc:title" to "Millennial Mage - Royal Road Chapters")
    )

}