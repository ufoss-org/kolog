/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import ch.tutteli.atrium.api.fluent.en_GB.isEqualComparingTo
import ch.tutteli.atrium.api.verbs.assertThat
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.*
import kotlin.test.Test

abstract class LoggerTest {

    @Test
    fun verifyNameIsTheNamePassedToLogger() {
        val logger = Logger.withName("this.is.a.logger")
        assertThat("this.is.a.logger").isEqualComparingTo(logger.name)
    }

    @Test
    fun verifyNameIsTheFullNameOfTheClassPassedToLogger() {
        val logger = Logger.of<Logger>()
        assertThat("org.ufoss.kolog.Logger").isEqualComparingTo(logger.name)
    }

    @Test
    fun verifyNameIsTheFullNameOfTheInvokingClass() {
        val logger = Logger()
        assertThat("org.ufoss.kolog.LoggerTest").isEqualComparingTo(logger.name)
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
