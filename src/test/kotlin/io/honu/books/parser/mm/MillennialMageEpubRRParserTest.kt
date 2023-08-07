package io.honu.books.parser.mm

import io.honu.books.parser.model.BookResult
import io.honu.books.parser.model.ParserConfig
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class MillennialMageEpubRRParserTest {

    private val title = "Millennial Mage - Royal Road Chapters"
    private val mmRoyalRoadBookParser: ParserConfig = MillennialMageParserConfigs.mmRoyalRoadBookParser

    @Test
    fun parsesChaptersMM5RRCorrectly() {
        val path = ".mm_demo/source/Royal Road/MM 5 Fusing _RR-Rip_.epub"
        val testFile = File(path)
        val mmParser =
            MillennialMageEpubParser(parserConfig = mmRoyalRoadBookParser.copy(firstChapter = "Chapter: 136 - Underpinning Aura"))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))

        val chapter1 = results.chapters.first()
        assertThat(chapter1.chapterName, startsWith("Chapter: 136"))
        assertThat(chapter1.segments.first(), startsWith("Tala couldn’t wipe the smile from her face"))
        assertThat(
            chapter1.segments.last(),
            equalTo("“Yes. Let’s spar. I need to clear my head.”")
        )

        val chapter172 = results.chapters.last()
        assertThat(chapter172.chapterName, startsWith("Chapter: 172"))
        assertThat(
            chapter172.segments.first(),
            equalTo("Tala waited just within the Constructionist Guild for one of the two Archons she was seeking a meeting with.")
        )
        assertThat(chapter172.segments.last(), equalTo("“Good day.”"))
    }

    @Test
    fun parsesChaptersMM6RRCorrectly() {
        val path = ".mm_demo/source/Royal Road/MM 6 Fused _RR-Rip_.epub"
        val testFile = File(path)
        val mmParser =
            MillennialMageEpubParser(parserConfig = mmRoyalRoadBookParser.copy(firstChapter = "Chapter: 173 - Tight Spaces"))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))

        val chapter1 = results.chapters.first()
        assertThat(chapter1.chapterName, startsWith("Chapter: 173"))
        assertThat(
            chapter1.segments.first(),
            allOf(
                startsWith("Tala walked out of the Constructionist Guildhall and into the late morning, winter air."),
                endsWith("a smile blossoming across her face."),
            )
        )
        assertThat(
            chapter1.segments.last(),
            equalTo("Note to self: This works and is incredibly effective. However, it is not for use in tight spaces.")
        )

        val chapter199 = results.chapters.last()
        assertThat(chapter199.chapterName, startsWith("Chapter: 199"))
        assertThat(chapter199.segments.first(), equalTo("Tala felt that her time in Marliweather was an unmitigated success."))
        assertThat(
            chapter199.segments.last(),
            equalTo("All told, it was a wonderfully peaceful time.")
        )
    }

    @Test
    fun parsesChaptersMM7PlusRRCorrectly() {
        val path = ".mm_demo/source/Royal Road/MM 7+ _RR-Rip_.epub"
        val testFile = File(path)
        val mmParser =
            MillennialMageEpubParser(parserConfig = mmRoyalRoadBookParser.copy(firstChapter = "Chapter: 200 - The Reason"))
        val results: BookResult = mmParser.parse(testFile.toPath())

        assertThat(results.bookTitle, equalTo(title))

        val chapter1 = results.chapters.first()
        assertThat(chapter1.chapterName, startsWith("Chapter: 200"))
        assertThat(chapter1.segments.first(), startsWith("Tala swayed gently"))
        assertThat(
            chapter1.segments.last(),
            equalTo("“Here’s what we’re going to do.”")
        )

        val chapter309 = results.chapters.last()
        assertThat(chapter309.chapterName, startsWith("Chapter: 309"))
        assertThat(chapter309.segments.first(), startsWith("Tala contemplated the Mage"))
        assertThat(
            chapter309.segments.last(),
            equalTo("Void it is!")
        )
    }

}