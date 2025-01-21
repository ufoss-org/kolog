/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:JvmMultifileClass
@file:JvmName("LoggerKt")
@file:Suppress("unused", "UNUSED_PARAMETER")

package org.ufoss.kolog

/**
 * **GENERAL NOTE**
 *
 * These deprecations are added to improve user experience when they will start to
 * search for their logging methods that are missing in Logger.
 * Pure stubs have [noImpl].
 */
internal fun noImpl(): Nothing =
        throw UnsupportedOperationException("Not implemented, should not be called")

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("trace { format }"))
public fun Logger.trace(format: String): Unit = noImpl()


@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace and adapt to use Kotlin string interpolation using '$' for arguments with lazy equivalent",
        replaceWith = ReplaceWith("trace { format }"))
public fun Logger.trace(format: String, vararg arguments: Any): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("trace(t) { format }"))
public fun Logger.trace(format: String, t: Throwable): Unit = noImpl()


@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("debug { format }"))
public fun Logger.debug(format: String): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace and adapt to use Kotlin string interpolation using '$' for arguments with lazy equivalent",
        replaceWith = ReplaceWith("debug { format }"))
public fun Logger.debug(format: String, vararg arguments: Any): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("debug(t) { format }"))
public fun Logger.debug(format: String, t: Throwable): Unit = noImpl()


@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("info { format }"))
public fun Logger.info(format: String): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace and adapt to use Kotlin string interpolation using '$' for arguments with lazy equivalent",
        replaceWith = ReplaceWith("info { format }"))
public fun Logger.info(format: String, vararg arguments: Any): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("info(t) { format }"))
public fun Logger.info(format: String, t: Throwable): Unit = noImpl()


@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("warn { format }"))
public fun Logger.warn(format: String): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace and adapt to use Kotlin string interpolation using '$' for arguments with lazy equivalent",
        replaceWith = ReplaceWith("warn { format }"))
public fun Logger.warn(format: String, vararg arguments: Any): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("warn(t) { format }"))
public fun Logger.warn(format: String, t: Throwable): Unit = noImpl()


@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("error { format }"))
public fun Logger.error(format: String): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace and adapt to use Kotlin string interpolation using '$' for arguments with lazy equivalent",
        replaceWith = ReplaceWith("error { format }"))
public fun Logger.error(format: String, vararg arguments: Any): Unit = noImpl()

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Replace with lazy equivalent",
        replaceWith = ReplaceWith("error(t) { format }"))
public fun Logger.error(format: String, t: Throwable): Unit = noImpl()
