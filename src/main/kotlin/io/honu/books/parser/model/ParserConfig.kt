package io.honu.books.parser.model

/**
 * The configuration for how to handle the specific oddities when parsing the desired document.
 * @param firstChapter The exact string match of the first chapter title. Ex: 'chapter 1', 'Preface
 * @param tableOfContentsRegex The regex to detect table of contents. This is needed to ignore the chapter matches for each content row
 * @param chapterRegex The regex definition to capture the chapters in the book. Should include special chapter names like 'Epilogue'
 * @param filteredSections The sections that may come up that should be excluded from results. Ex: 'Table of Content', 'Contents', 'Author’s Note'
 *                         If this is not included, sections not specified in "chapterRegex" may be incorrectly pulled into another chapter
 * @param metaDataOverrides Map values that override extracted values
 */
data class ParserConfig(
    val firstChapter: String,
    val tableOfContentsRegex: String,
    val chapterRegex: String,
    val filteredSections: List<String>,
    val metaDataOverrides: Map<String, String> = mapOf(),
)
