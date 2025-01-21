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
class AndroidLoggerTest : LoggerTest() {

    @Test
    fun verifyInfoTimeMillisIsWorkingRealDelay(): Unit = runBlocking {
        val logger = Logger.withName("org.ufoss.kolog.Test")
        val measuredName = "my test operation"
        logger.infoTimeMillis(measuredName) {
            delay(50)
            printTime()
            delay(50)
        }
        ShadowLog.getLogs().apply {
            val logsWithMessage = this.filter { it.tag == "org.ufoss.kolog.Test" }
            assertEquals(logsWithMessage.size, 3)
            println(logsWithMessage[0].msg)
            println(logsWithMessage[1].msg)
            println(logsWithMessage[2].msg)
            assertEquals(logsWithMessage[0].msg, "my test operation starting")
            assertTrue(logsWithMessage[1].msg.startsWith("my test operation step 1 :"))
            assertTrue(logsWithMessage[1].msg.endsWith("since start"))
            assertTrue(logsWithMessage[2].msg.startsWith("my test operation is finished :"))
        }
    }
}
