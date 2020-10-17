/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:JvmMultifileClass
@file:JvmName("LoggerKt")

package org.ufoss.kolog

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.time.TimeSource

/**
 * Executes the given [block] and logs at the TRACE level the elapsed time in milliseconds.
 *
 * @param measuredName  The name of the task that will be logged
 * @param block         The code block that will be time tracked
 */
public inline fun <T> Logger.traceTimeMillis(measuredName: String, block: TimeScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val start = TimeSource.Monotonic.markNow()
    val timeScope: TimeScope = if (isTraceEnabled) {
        var index = 1
        TimeScope {
            trace { "$measuredName step ${index++} : ${start.elapsedNow().inMilliseconds} ms since start" }
        }
    } else {
        NoopTimeScope
    }
    return block(timeScope).apply {
        trace { "$measuredName is finished : ${start.elapsedNow().inMilliseconds} ms since start" }
    }
}

/**
 * Executes the given [block] and and logs at the TRACE level the elapsed time in milliseconds.
 *
 * @param measuredName  The name of the task that will be logged
 * @param block         The code block that will be time tracked
 */
public inline fun <T> Logger.debugTimeMillis(measuredName: String, block: TimeScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val start = TimeSource.Monotonic.markNow()
    val timeScope: TimeScope = if (isDebugEnabled) {
        var index = 1
        TimeScope {
            debug { "$measuredName step ${index++} : ${start.elapsedNow().inMilliseconds} ms since start" }
        }
    } else {
        NoopTimeScope
    }
    return block(timeScope).apply {
        debug { "$measuredName is finished : ${start.elapsedNow().inMilliseconds} ms since start" }
    }
}
