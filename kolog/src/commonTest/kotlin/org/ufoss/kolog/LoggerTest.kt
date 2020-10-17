/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import ch.tutteli.atrium.api.fluent.en_GB.isEqualComparingTo
import ch.tutteli.atrium.api.verbs.assertThat
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.*
import kotlin.test.*

class LoggerTest {

    @Test
    fun `verify name is the name passed to Logger`() {
        val logger = Logger.withName("this.is.a.logger")
        assertThat("this.is.a.logger").isEqualComparingTo(logger.name)
    }

    @Test
    fun `verify name is the full name of the Class passed to Logger`() {
        val logger = Logger.of<Logger>()
        assertThat("org.ufoss.kolog.Logger").isEqualComparingTo(logger.name)
    }

    @Test
    fun `verify name is the full name of the invoking class`() {
        val logger = Logger()
        assertThat("org.ufoss.kolog.LoggerTest").isEqualComparingTo(logger.name)
    }

    @Test
    fun `verify traceTimeMillis is working`() = runBlockingTest {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        logger.traceTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }

    @Test
    fun `verify traceTimeMillis is working (no-op)`() = runBlockingTest {
        val logger = Logger.withName("not.existing.Class")
        logger.traceTimeMillis("my test operation") {
            delay(50)
            printTime()
            delay(50)
        }
    }
}
