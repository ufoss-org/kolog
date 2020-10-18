/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import ch.tutteli.atrium.api.fluent.en_GB.all
import ch.tutteli.atrium.api.fluent.en_GB.endsWith
import ch.tutteli.atrium.api.fluent.en_GB.isEqualComparingTo
import ch.tutteli.atrium.api.fluent.en_GB.startsWith
import ch.tutteli.atrium.api.verbs.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
public class AndroidLoggerTest : LoggerTest() {

    @Test
    public fun verifyInfoTimeMillisIsWorkingRealDelay(): Unit = runBlocking {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        val measuredName = "my test operation"
        logger.infoTimeMillis(measuredName) {
            delay(50)
            printTime()
            delay(50)
        }
        ShadowLog.getLogs().apply {
            val logsWithMessage = this.filter { it.tag == "org.ufoss.kolog.Test" }
            assertThat(logsWithMessage.size).isEqualComparingTo(2)
            assertThat(logsWithMessage.map { it.msg }).all {
                startsWith(measuredName)
                endsWith("ms since start")
            }
        }
    }
}
