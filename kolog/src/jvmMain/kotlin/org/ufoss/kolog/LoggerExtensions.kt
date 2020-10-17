/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:JvmMultifileClass
@file:JvmName("LoggerKt")

package org.ufoss.kolog

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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

    val start = System.currentTimeMillis()
    val timeScope: TimeScope = if (platformLogger.isTraceEnabled) {
        var index = 1
        TimeScope {
            platformLogger.trace("$measuredName step ${index++} : ${System.currentTimeMillis() - start} ms since start")
        }
    } else {
        NoopTimeScope
    }
    return block(timeScope).apply {
        trace { "$measuredName is finished : ${System.currentTimeMillis() - start} ms since start" }
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

    val start = System.currentTimeMillis()
    val timeScope: TimeScope = if (platformLogger.isDebugEnabled) {
        var index = 1
        TimeScope {
            platformLogger.debug("$measuredName step ${index++} : ${System.currentTimeMillis() - start} ms since start")
        }
    } else {
        NoopTimeScope
    }
    return block(timeScope).apply {
        debug { "$measuredName is finished : ${System.currentTimeMillis() - start} ms since start" }
    }
}
