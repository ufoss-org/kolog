/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import platform.posix.LOG_DEBUG
import platform.posix.LOG_ERR
import platform.posix.LOG_INFO
import platform.posix.LOG_WARNING
import platform.posix.syslog

/**
 * Kotlin idiomatic logger for IOS based on NSLog
 */
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
public actual value class Logger @PublishedApi internal constructor(
    @PublishedApi internal val parameter: Parameter
) {
    /**
     * Companion object for [Logger] class that contains its constructor functions
     * [withName] and [of].
     */
    public actual companion object {
        /**
         * Return a logger named according to the name parameter
         *
         * @param name  The name of the logger.
         * @return the logger
         */
        @Suppress("NOTHING_TO_INLINE")
        public actual inline fun withName(name: String): Logger = Logger(name)

        /**
         * Return a logger named corresponding to the class of the generic parameter
         */
        public actual inline fun <reified T : Any> of(): Logger = Logger(T::class.qualifiedName!!)
    }

    /**
     * Log a message at the TRACE level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun trace(msg: () -> Any?) {
        if (isTraceEnabled) {
            syslog(LOG_DEBUG, "%s: (%s) %s", "TRACE", name, msg().toString())
        }
    }

    /**
     * Log an exception (throwable) at the TRACE level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public actual inline fun trace(t: Throwable, msg: () -> Any?) {
        if (isTraceEnabled) {
            syslog(LOG_DEBUG, "%s: (%s) %s", "TRACE", name, msg().toString())
            syslog(LOG_DEBUG, "%s: %s", "TRACE", t.message)
        }
    }

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun debug(msg: () -> Any?) {
        if (isDebugEnabled) {
            syslog(LOG_DEBUG, "%s: (%s) %s", "DEBUG", name, msg().toString())
        }
    }

    /**
     * Log an exception (throwable) at the DEBUG level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public actual inline fun debug(t: Throwable, msg: () -> Any?) {
        if (isDebugEnabled) {
            syslog(LOG_DEBUG, "%s: (%s) %s", "DEBUG", name, msg().toString())
            syslog(LOG_DEBUG, "%s: %s", "DEBUG", t.message)
        }
    }

    /**
     * Log a message at the INFO level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun info(msg: () -> Any?) {
        if (isInfoEnabled) {
            syslog(LOG_INFO, "%s: (%s) %s", "INFO", name, msg().toString())
        }
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public actual inline fun info(t: Throwable, msg: () -> Any?) {
        if (isInfoEnabled) {
            syslog(LOG_INFO, "%s: (%s) %s", "INFO", name, msg().toString())
            syslog(LOG_INFO, "%s: %s", "INFO", t.message)
        }
    }

    /**
     * Log a message at the WARN level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun warn(msg: () -> Any?) {
        if (isWarnEnabled) {
            syslog(LOG_WARNING, "%s: (%s) %s", "WARN", name, msg().toString())
        }
    }

    /**
     * Log an exception (throwable) at the WARN level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public actual inline fun warn(t: Throwable, msg: () -> Any?) {
        if (isWarnEnabled) {
            syslog(LOG_WARNING, "%s: (%s) %s", "WARN", name, msg().toString())
            syslog(LOG_WARNING, "%s: %s", "WARN", t.message)
        }
    }

    /**
     * Log a message at the ERROR level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun error(msg: () -> Any?) {
        if (isErrorEnabled) {
            syslog(LOG_ERR, "%s: (%s) %s", "ERROR", name, msg().toString())
        }
    }

    /**
     * Log an exception (throwable) at the ERROR level with an accompanying message.
     *
     * @param t   the exception (throwable) to log
     * @param msg a function that returns the message accompanying the exception
     */
    public actual inline fun error(t: Throwable, msg: () -> Any?) {
        if (isErrorEnabled) {
            syslog(LOG_ERR, "%s: (%s) %s", "ERROR", name, msg().toString())
            syslog(LOG_ERR, "%s: %s", "ERROR", t.message)
        }
    }
}

/**
 * @return the name of this <code>Logger</code> instance.
 */
public actual val Logger.name: String
    get() = parameter as String

/**
 * Is the logger instance enabled for the TRACE level?
 *
 * @return True if this Logger is enabled for the TRACE level, false otherwise.
 */
public actual val Logger.isTraceEnabled: Boolean
    get() = true

/**
 * Is the logger instance enabled for the DEBUG level?
 *
 * @return True if this Logger is enabled for the DEBUG level, false otherwise.
 */
public actual val Logger.isDebugEnabled: Boolean
    get() = true

/**
 * Is the logger instance enabled for the INFO level?
 *
 * @return True if this Logger is enabled for the INFO level, false otherwise.
 */
public actual val Logger.isInfoEnabled: Boolean
    get() = true

/**
 * Is the logger instance enabled for the WARN level?
 *
 * @return True if this Logger is enabled for the WARN level, false otherwise.
 */
public actual val Logger.isWarnEnabled: Boolean
    get() = true

/**
 * Is the logger instance enabled for the ERROR level?
 *
 * @return True if this Logger is enabled for the ERROR level, false otherwise.
 */
public actual val Logger.isErrorEnabled: Boolean
    get() = true

