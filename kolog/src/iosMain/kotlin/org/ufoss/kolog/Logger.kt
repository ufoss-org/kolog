/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

import platform.Foundation.NSLog

/**
 * Kotlin idiomatic logger for Android based on Logcat
 */
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
public actual inline class Logger @PublishedApi internal constructor(
        @PublishedApi internal val parameter: Parameter
) : LoggerProperties {
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
     * Return the name of this <code>Logger</code> instance.
     * @return name of this logger instance
     */
    public override val name: String get() = parameter as String

    /**
     * Is the logger instance enabled for the TRACE level?
     *
     * @return True if this Logger is enabled for the TRACE level, false otherwise.
     */
    public override val isTraceEnabled: Boolean get() = true

    /**
     * Is the logger instance enabled for the DEBUG level?
     *
     * @return True if this Logger is enabled for the DEBUG level, false otherwise.
     */
    public override val isDebugEnabled: Boolean get() = true

    /**
     * Is the logger instance enabled for the INFO level?
     *
     * @return True if this Logger is enabled for the INFO level, false otherwise.
     */
    public override val isInfoEnabled: Boolean get() = true

    /**
     * Is the logger instance enabled for the WARN level?
     *
     * @return True if this Logger is enabled for the WARN level, false otherwise.
     */
    public override val isWarnEnabled: Boolean get() = true

    /**
     * Is the logger instance enabled for the ERROR level?
     *
     * @return True if this Logger is enabled for the ERROR level, false otherwise.
     */
    public override val isErrorEnabled: Boolean get() = true

    /**
     * Log a message at the TRACE level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun trace(msg: () -> Any?) {
        if (isTraceEnabled) {
            NSLog("%s: (%s) %s", "TRACE", name, msg().toString())
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
            NSLog("%s: (%s) %s", "TRACE", name, msg().toString())
            NSLog("%s: %s", "TRACE", t.message)
        }
    }

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun debug(msg: () -> Any?) {
        if (isDebugEnabled) {
            NSLog("%s: (%s) %s", "DEBUG", name, msg().toString())
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
            NSLog("%s: (%s) %s", "DEBUG", name, msg().toString())
            NSLog("%s: %s", "DEBUG", t.message)
        }
    }

    /**
     * Log a message at the INFO level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun info(msg: () -> Any?) {
        if (isInfoEnabled) {
            NSLog("%s: (%s) %s", "INFO", name, msg().toString())
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
            NSLog("%s: (%s) %s", "INFO", name, msg().toString())
            NSLog("%s: %s", "INFO", t.message)
        }
    }

    /**
     * Log a message at the WARN level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun warn(msg: () -> Any?) {
        if (isWarnEnabled) {
            NSLog("%s: (%s) %s", "WARN", name, msg().toString())
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
            NSLog("%s: (%s) %s", "WARN", name, msg().toString())
            NSLog("%s: %s", "WARN", t.message)
        }
    }

    /**
     * Log a message at the ERROR level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun error(msg: () -> Any?) {
        if (isErrorEnabled) {
            NSLog("%s: (%s) %s", "ERROR", name, msg().toString())
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
            NSLog("%s: (%s) %s", "ERROR", name, msg().toString())
            NSLog("%s: %s", "ERROR", t.message)
        }
    }
}
