/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
public class AndroidLoggerTest : LoggerTest() {

    @Test
    public fun `verify infoTimeMillis is working`(): Unit = runBlocking {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        val measuredName = "my test operation"
        logger.infoTimeMillis(measuredName) {
            delay(50)
            printTime()
            delay(50)
        }
        ShadowLog.getLogs().apply {
            val logsWithMessage = this.filter { it.tag == "org.ufoss.kolog.Test" }
            assertEquals(logsWithMessage.size, 2)
            logsWithMessage
                    .map { it.msg }
                    .forEach { message ->
                        assertTrue(message.startsWith(measuredName))
                        assertTrue(message.endsWith("ms since start"))
                    }
        }
    }
}
