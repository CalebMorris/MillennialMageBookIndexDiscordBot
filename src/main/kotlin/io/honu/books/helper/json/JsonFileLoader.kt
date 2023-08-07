package io.honu.books.helper.json

import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.nio.file.Path

object JsonFileLoader {

    inline fun <reified TModel> load(path: Path): TModel {
        val bufferedReader: BufferedReader = path.toFile().bufferedReader()
        val inputString: String = bufferedReader.use { it.readText() }
        return Json.decodeFromString<TModel>(inputString)
    }

}