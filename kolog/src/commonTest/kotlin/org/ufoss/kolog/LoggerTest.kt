/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

abstract class LoggerTest {

    @Test
    fun verifyNameIsTheNamePassedToLogger() {
        val logger = Logger.withName("this.is.a.logger")
        assertEquals("this.is.a.logger", logger.name)
    }

    @Test
    fun verifyNameIsTheFullNameOfTheClassPassedToLogger() {
        val logger = Logger.of<Logger>()
        assertEquals("org.ufoss.kolog.Logger", logger.name)
    }

    @Test
    fun verifyTraceTimeMillisIsWorking() = runBlocking {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        logger.traceTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }

    @Test
    fun verifyDebugTimeMillisIsWorking_noOpCase() = runBlocking {
        val logger = Logger.withName("not.existing.Class")
        logger.debugTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }
}
