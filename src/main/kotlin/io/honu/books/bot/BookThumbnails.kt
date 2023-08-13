package io.honu.books.bot

object BookThumbnails {

    const val royalRoad =
        "https://www.royalroadcdn.com/public/covers-full/47826-millennial-mage-a-slice-of-life-progression.jpg"
    const val book1Mageling = "https://m.media-amazon.com/images/I/41K2r-kklFL._SY346_.jpg"
    const val book2Mage = "https://m.media-amazon.com/images/I/41gJn5wsibL._SY346_.jpg"
    const val book3Binding = "https://m.media-amazon.com/images/I/316SNRyrAvL.jpg"
    const val book4Bound = "https://m.media-amazon.com/images/I/51el2-L6DOL.jpg"

    fun fromBookIndex(index: Int) {
        when (index) {
            1 -> book1Mageling
            2 -> book2Mage
            3 -> book3Binding
            4 -> book4Bound
            else -> royalRoad
        }
    }

    fun fromBookName(name: String): String =
        if (name.contains("mageling", true)) book1Mageling
        else if (name.contains("mage", true)) book2Mage
        else if (name.contains("binding", true)) book3Binding
        else if (name.contains("bound", true)) book4Bound
        else royalRoad

}