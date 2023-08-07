package io.honu.books.parser.mm

import io.honu.books.parser.model.BookResult
import io.honu.books.parser.model.ParserConfig
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class MillennialMageEpubParserTest {

    private val mmBookParser: ParserConfig = ParserConfig(
        firstChapter = "Chapter: 1",
        tableOfContentsRegex = "^(Contents)|(Table of Contents)$",
        chapterRegex = "^Chapter(:.*\\d+)?$",
        filteredSections = listOf("Table of Contents", "Contents", "Author’s Note"),
    )

    @Test
    fun parsesChaptersMM1Correctly() {
        val title = "Millennial Mage Book 1: Mageling"
        val path = ".mm_demo/Millennial Mage 1 - Mageling.epub"
        val testFile = File(path)
        val mmParser = MillennialMageEpubParser(parserConfig = mmBookParser.copy(metaDataOverrides = mapOf("dc:title" to title)))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))
        assertEquals(results.chapters.first().chapterName, "Title Page")

        val chapter1 = results.chapters[1]
        assertThat(chapter1.chapterName, equalTo("Chapter: 1"))
        assertThat(chapter1.segments.first(), equalTo("New Beginnings"))
        assertThat(
            chapter1.segments.last(),
            equalTo("Without further delay, she strode through the wide, double doors.")
        )

        val chapter40 = results.chapters.last()
        assertThat(chapter40.chapterName, equalTo("Chapter: 40"))
        assertThat(chapter40.segments.first(), equalTo("The Beginning of True Magehood"))
        assertThat(chapter40.segments.last(), equalTo("This was only the beginning."))
    }

    @Test
    fun parsesChaptersMM2Correctly() {
        val title = "Millennial Mage Book 2: Mage"
        val path = ".mm_demo/Millennial Mage 2 - Mage.epub"
        val testFile = File(path)
        val mmParser = MillennialMageEpubParser(parserConfig = mmBookParser.copy(metaDataOverrides = mapOf("dc:title" to title)))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))
        assertEquals(results.chapters.first().chapterName, "Title Page")

        val chapter1 = results.chapters[1]
        assertThat(chapter1.chapterName, equalTo("Chapter: 1"))
        assertThat(chapter1.segments.first(), equalTo("To the Ending Grove"))
        assertThat(
            chapter1.segments.last(),
            equalTo("Alright. Now, we’re getting somewhere. And back to work she went.")
        )

        val chapter30 = results.chapters.last()
        assertThat(chapter30.chapterName, equalTo("Chapter: 30"))
        assertThat(chapter30.segments.first(), equalTo("Understatement"))
        assertThat(
            chapter30.segments.last(),
            equalTo("I don’t need to think about that now. Now, I just need to make a properly powered Archon Star. That shouldn’t be too difficult. She rolled her eyes again at her own optimism even as she smiled at the challenge ahead.")
        )
    }

    @Test
    fun parsesChaptersMM3Correctly() {
        val title = "Millennial Mage Book 3: Binding"
        val path = ".mm_demo/Millennial_Mage_3_-_Binding.epub"
        val testFile = File(path)
        val mmParser = MillennialMageEpubParser(parserConfig = mmBookParser.copy(metaDataOverrides = mapOf("dc:title" to title)))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))
        assertEquals(results.chapters.first().chapterName, "Title Page")

        val chapter1 = results.chapters[1]
        assertThat(chapter1.chapterName, equalTo("Chapter: 1"))
        assertThat(chapter1.segments.first(), equalTo("I’m Going to Become an Archon"))
        assertThat(
            chapter1.segments.last(),
            allOf(startsWith("I knew it!"), endsWith("“You and me both.”"))
        )

        val chapter34 = results.chapters.last()
        assertThat(chapter34.chapterName, equalTo("Chapter: 34"))
        assertThat(chapter34.segments.first(), equalTo("Fool’s Folly"))
        assertThat(
            chapter34.segments.last(),
            allOf(
                startsWith("And, though she would never admit it"),
                endsWith("Devi was coming to like being called ‘Kit.’")
            )
        )
    }

    @Test
    fun parsesChaptersMM4Correctly() {
        val title = "Millennial Mage Book 3: Bound"
        val path = ".mm_demo/Millennial_Mage_4_-_Bound.epub"
        val testFile = File(path)
        val mmParser = MillennialMageEpubParser(parserConfig = mmBookParser.copy(metaDataOverrides = mapOf("dc:title" to title)))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))
        assertEquals(results.chapters.first().chapterName, "Title Page")

        val chapter1 = results.chapters[1]
        assertThat(chapter1.chapterName, equalTo("Chapter: 1"))
        assertThat(chapter1.segments.first(), equalTo("That Seems Bad"))
        assertThat(
            chapter1.segments.last(),
            allOf(
                startsWith("The Leshkin lost all cohesion"),
                endsWith("a wind that Tala couldn’t feel or detect.")
            )
        )

        val finalChapter = results.chapters.last()
        assertThat(finalChapter.chapterName, equalTo("Chapter: 32"))
        assertThat(finalChapter.segments.first(), equalTo("Foundational Understandings"))
        assertThat(
            finalChapter.segments.last(),
            equalTo("Whatever it took, it was time that she truly started Fusing.")
        )
    }

}