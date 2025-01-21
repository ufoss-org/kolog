/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import kotlin.jvm.JvmInline

@JvmInline
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
public expect value class Logger internal constructor(
        internal val parameter: Parameter
) {
    /**
     * Companion object for [Logger] class that contains its constructor functions
     * [withName] and [of].
     */
    public companion object {
        /**
         * Return a logger named according to the name parameter
         *
         * @param name  The name of the logger.
         * @return the logger
         */
        public inline fun withName(name: String): Logger

        /**
         * Return a logger named corresponding to the class of the generic parameter
         */
        public inline fun <reified T : Any> of(): Logger
    }

    /**
     * Log a message at the TRACE level.
     *
     * @param msg a function that returns the message to be logged
     */
    public inline fun trace(msg: () -> Any?)

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public inline fun trace(t: Throwable, msg: () -> Any?)

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg a function that returns the message to be logged
     */
    public inline fun debug(msg: () -> Any?)

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public inline fun debug(t: Throwable, msg: () -> Any?)

    /**
     * Log a message at the INFO level.
     *
     * @param msg a function that returns the message to be logged
     */
    public inline fun info(msg: () -> Any?)

    /**
     * Log an exception (throwable) at the INFO level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public inline fun info(t: Throwable, msg: () -> Any?)

    /**
     * Log a message at the WARN level.
     *
     * @param msg a function that returns the message to be logged
     */
    public inline fun warn(msg: () -> Any?)

    /**
     * Log an exception (throwable) at the WARN level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public inline fun warn(t: Throwable, msg: () -> Any?)

    /**
     * Log a message at the ERROR level.
     *
     * @param msg a function that returns the message to be logged
     */
    public inline fun error(msg: () -> Any?)

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public inline fun error(t: Throwable, msg: () -> Any?)
}

/**
 * @return the name of this <code>Logger</code> instance.
 */
public expect val Logger.name: String

/**
 * Is the logger instance enabled for the TRACE level?
 *
 * @return True if this Logger is enabled for the TRACE level, false otherwise.
 */
public expect val Logger.isTraceEnabled: Boolean

/**
 * Is the logger instance enabled for the DEBUG level?
 *
 * @return True if this Logger is enabled for the DEBUG level, false otherwise.
 */
public expect val Logger.isDebugEnabled: Boolean

/**
 * Is the logger instance enabled for the INFO level?
 *
 * @return True if this Logger is enabled for the INFO level, false otherwise.
 */
public expect val Logger.isInfoEnabled: Boolean

/**
 * Is the logger instance enabled for the WARN level?
 *
 * @return True if this Logger is enabled for the WARN level, false otherwise.
 */
public expect val Logger.isWarnEnabled: Boolean

/**
 * Is the logger instance enabled for the ERROR level?
 *
 * @return True if this Logger is enabled for the ERROR level, false otherwise.
 */
public expect val Logger.isErrorEnabled: Boolean
