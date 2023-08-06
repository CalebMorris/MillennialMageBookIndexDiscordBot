package io.honu.books

import kotlin.test.Test
import kotlin.test.assertNotNull

class BookIndexerTest {
    @Test
    fun appHasAGreeting() {
        val classUnderTest = BookIndexer()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }

//    @Test
//    fun failing() {
//        assert(false, { "should fail" })
//    }
}
