/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class JvmLoggerTest {

    @Test
    fun `verify traceTimeMillis is working real delay`() = runBlocking {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        logger.traceTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }
}
