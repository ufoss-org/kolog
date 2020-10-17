/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import ch.tutteli.atrium.api.fluent.en_GB.isEqualComparingTo
import ch.tutteli.atrium.api.verbs.assertThat
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
}
