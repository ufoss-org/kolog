/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.delay

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
    fun verifyNameIsTheFullNameOfTheInvokingClass() {
        val logger = Logger()
        assertEquals("org.ufoss.kolog.LoggerTest", logger.name)
    }

    @Test
    fun verifyInfoTimeMillisIsWorking() = runBlockingTest {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        logger.infoTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }

    @Test
    fun verifyInfoTimeMillisIsWorking_noOpCase() = runBlockingTest {
        val logger = Logger.withName("not.existing.Class")
        logger.infoTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }
}
