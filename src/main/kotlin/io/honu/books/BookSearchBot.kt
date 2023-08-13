package io.honu.books

import dev.kord.core.Kord
import io.honu.books.bot.IndexBot
import io.honu.books.index.model.IndexConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.Path

suspend fun main() = withContext(Dispatchers.Default) {
    val queryCommandPrefix = System.getenv("BOOK_INDEX_QUERY_PREFIX") ?: "!query"

    require(System.getenv("DISCORD_TOKEN") != null) { "[DISCORD_TOKEN] must not be empty" }
    require(System.getenv("DISCORD_BOOK_INDEX_PATH") != null) { "[DISCORD_BOOK_INDEX_PATH] must not be empty" }
    require(System.getenv("DISCORD_BOOK_SOURCE_PATH") != null) { "[DISCORD_BOOK_SOURCE_PATH] must not be empty" }
    val kord: Kord = Kord(System.getenv("DISCORD_TOKEN"))

    val indexConfig = IndexConfig(
        indexPath = Path(System.getenv("DISCORD_BOOK_INDEX_PATH")),
        sourcePath = Path(System.getenv("DISCORD_BOOK_SOURCE_PATH"))
    )


    val box = IndexBot(queryCommandPrefix, indexConfig, kord)
    box.start()
}