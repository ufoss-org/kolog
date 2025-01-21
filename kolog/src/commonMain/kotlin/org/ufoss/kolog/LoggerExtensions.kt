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
 * Executes the given [block] and logs at the TRACE level the elapsed time.
 *
 * @param measuredName  The name of the task that will be logged
 * @param block         The code block that will be time tracked
 */
public inline fun <T> Logger.traceTimeMillis(measuredName: String, block: TimeScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val start = TimeSource.Monotonic.markNow()
    trace { "starting" }

    val timeScope: TimeScope = if (isTraceEnabled) {
        var index = 1
        TimeScope {
            trace {
                "$measuredName step ${index++} : ${formatDuration(start.elapsedNow().inWholeNanoseconds)} since start"
            }
        }
    } else {
        NoopTimeScope
    }

    var completedNormally = false
    try {
        val result = block(timeScope)
        completedNormally = true
        return result
    } finally {
        if (isTraceEnabled) {
            trace {
                "$measuredName ${result(completedNormally)} : " +
                        "run in ${formatDuration(start.elapsedNow().inWholeNanoseconds)}"
            }
        }
    }
}

/**
 * Executes the given [block] and logs at the DEBUG level the elapsed time.
 *
 * @param measuredName  The name of the task that will be logged
 * @param block         The code block that will be time tracked
 */
public inline fun <T> Logger.debugTimeMillis(measuredName: String, block: TimeScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val start = TimeSource.Monotonic.markNow()
    debug { "starting" }

    val timeScope: TimeScope = if (isDebugEnabled) {
        var index = 1
        TimeScope {
            debug {
                "$measuredName step ${index++} : ${formatDuration(start.elapsedNow().inWholeNanoseconds)} since start"
            }
        }
    } else {
        NoopTimeScope
    }

    var completedNormally = false
    try {
        val result = block(timeScope)
        completedNormally = true
        return result
    } finally {
        if (isDebugEnabled) {
            debug {
                "$measuredName ${result(completedNormally)} : " +
                        "run in ${formatDuration(start.elapsedNow().inWholeNanoseconds)}"
            }
        }
    }
}

/**
 * Executes the given [block] and logs at the INFO level the elapsed time.
 *
 * @param measuredName  The name of the task that will be logged
 * @param block         The code block that will be time tracked
 */
public inline fun <T> Logger.infoTimeMillis(measuredName: String, block: TimeScope.() -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val start = TimeSource.Monotonic.markNow()
    info { "$measuredName starting" }

    val timeScope: TimeScope = if (isInfoEnabled) {
        var index = 1
        TimeScope {
            info {
                "$measuredName step ${index++} : ${formatDuration(start.elapsedNow().inWholeNanoseconds)} since start"
            }
        }
    } else {
        NoopTimeScope
    }

    var completedNormally = false
    try {
        val result = block(timeScope)
        completedNormally = true
        return result
    } finally {
        if (isInfoEnabled) {
            info {
                "$measuredName ${result(completedNormally)} : " +
                        "run in ${formatDuration(start.elapsedNow().inWholeNanoseconds)}"
            }
        }
    }
}

@PublishedApi
internal fun result(completedNormally: Boolean): String = if (completedNormally) "is finished" else "has failed"

/**
 * Returns a duration in the nearest whole-number units like "999 µs" or "  1 s ". This rounds 0.5
 * units away from 0 and 0.499 towards 0. The smallest unit this returns is "µs"; the largest unit
 * it returns is "s". For values in [-499..499] this returns "  0 µs".
 *
 * The returned string attempts to be column-aligned to 6 characters. For negative and large values
 * the returned string may be longer.
 */
@PublishedApi
internal fun formatDuration(ns: Long): String {
    val s =
        when {
            ns <= -999_500_000 -> "${(ns - 500_000_000) / 1_000_000_000} s"
            ns <= -999_500 -> "${(ns - 500_000) / 1_000_000} ms"
            ns <= 0 -> "${(ns - 500) / 1_000} µs"
            ns < 999_500 -> "${(ns + 500) / 1_000} µs"
            ns < 999_500_000 -> "${(ns + 500_000) / 1_000_000} ms"
            else -> "${(ns + 500_000_000) / 1_000_000_000} s"
        }
    return s
}
