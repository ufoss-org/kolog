/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:Suppress("unused")

package org.ufoss.kolog

import org.slf4j.LoggerFactory
import org.slf4j.Marker

/**
 * Kotlin idiomatic logger for JVM based on Slf4j
 */
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
public actual inline class Logger @PublishedApi internal constructor(
        @PublishedApi internal val parameter: Parameter
): LoggerProperties {
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
        @Suppress("INAPPLICABLE_JVM_NAME", "NOTHING_TO_INLINE")
        @JvmName("withName")
        public actual inline fun withName(name: String): Logger = Logger(LoggerFactory.getLogger(name))

        /**
         * Return a logger named corresponding to the class of the generic parameter
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("of")
        public actual inline fun <reified T : Any> of(): Logger = Logger(LoggerFactory.getLogger(T::class.java))
    }

    /**
     * Return the name of this <code>Logger</code> instance.
     * @return name of this logger instance
     */
    public override val name: String get() = parameter.name

    /**
     * Is the logger instance enabled for the TRACE level?
     *
     * @return True if this Logger is enabled for the TRACE level, false otherwise.
     */
    public override val isTraceEnabled: Boolean get() = parameter.isTraceEnabled

    /**
     * Similar to isTraceEnabled property except that the marker data is also taken into account.
     *
     * @param marker The marker data to take into consideration
     * @return True if this Logger is enabled for the TRACE level, false otherwise.
     */
    public fun isTraceEnabled(marker: Marker): Boolean = parameter.isTraceEnabled(marker)

    /**
     * Is the logger instance enabled for the DEBUG level?
     *
     * @return True if this Logger is enabled for the DEBUG level, false otherwise.
     */
    public override val isDebugEnabled: Boolean get() = parameter.isDebugEnabled

    /**
     * Similar to isDebugEnabled property except that the marker data is also taken into account.
     *
     * @param marker The marker data to take into consideration
     * @return True if this Logger is enabled for the DEBUG level, false otherwise.
     */
    public fun isDebugEnabled(marker: Marker): Boolean = parameter.isDebugEnabled(marker)

    /**
     * Is the logger instance enabled for the INFO level?
     *
     * @return True if this Logger is enabled for the INFO level, false otherwise.
     */
    public override val isInfoEnabled: Boolean get() = parameter.isInfoEnabled

    /**
     * Similar to isInfoEnabled property except that the marker data is also taken into account.
     *
     * @param marker The marker data to take into consideration
     * @return True if this Logger is enabled for the INFO level, false otherwise.
     */
    public fun isInfoEnabled(marker: Marker?): Boolean = parameter.isInfoEnabled(marker)

    /**
     * Is the logger instance enabled for the WARN level?
     *
     * @return True if this Logger is enabled for the WARN level, false otherwise.
     */
    public override val isWarnEnabled: Boolean get() = parameter.isWarnEnabled

    /**
     * Similar to isWarnEnabled property except that the marker data is also taken into account.
     *
     * @param marker The marker data to take into consideration
     * @return True if this Logger is enabled for the WARN level, false otherwise.
     */
    public fun isWarnEnabled(marker: Marker?): Boolean = parameter.isWarnEnabled(marker)

    /**
     * Is the logger instance enabled for the ERROR level?
     *
     * @return True if this Logger is enabled for the ERROR level, false otherwise.
     */
    public override val isErrorEnabled: Boolean get() = parameter.isErrorEnabled

    /**
     * Similar to isErrorEnabled property except that the marker data is also taken into account.
     *
     * @param marker The marker data to take into consideration
     * @return True if this Logger is enabled for the ERROR level, false otherwise.
     */
    public fun isErrorEnabled(marker: Marker?): Boolean = parameter.isErrorEnabled(marker)

    /**
     * Log a message at the TRACE level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun trace(msg: () -> Any?) {
        if (isTraceEnabled) {
            parameter.trace(msg().toString())
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
            parameter.trace(msg().toString(), t)
        }
    }

    /**
     * Log a message with the specific Marker at the TRACE level.
     *
     * @param marker the marker data specific to this log statement
     * @param msg    a function that returns the message to be logged
     */
    public inline fun trace(marker: Marker, msg: () -> Any?) {
        if (isTraceEnabled(marker)) {
            parameter.trace(marker, msg().toString())
        }
    }

    /**
     * This method is similar to trace(Throwable, () -> Any?)
     * method except that the marker data is also taken into
     * consideration.
     *
     * @param marker the marker data specific to this log statement
     * @param t      the exception (throwable) to log
     * @param msg    a function that returns the message accompanying the exception
     */
    public inline fun trace(marker: Marker, t: Throwable, msg: () -> Any?) {
        if (isTraceEnabled(marker)) {
            parameter.trace(marker, msg().toString(), t)
        }
    }

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun debug(msg: () -> Any?) {
        if (isDebugEnabled) {
            parameter.debug(msg().toString())
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
            parameter.debug(msg().toString(), t)
        }
    }

    /**
     * Log a message with the specific Marker at the DEBUG level.
     *
     * @param marker the marker data specific to this log statement
     * @param msg    a function that returns the message to be logged
     */
    public inline fun debug(marker: Marker, msg: () -> Any?) {
        if (isDebugEnabled(marker)) {
            parameter.debug(marker, msg().toString())
        }
    }

    /**
     * This method is similar to debug(Throwable, () -> Any?)
     * method except that the marker data is also taken into
     * consideration.
     *
     * @param marker the marker data specific to this log statement
     * @param t      the exception (throwable) to log
     * @param msg    a function that returns the message accompanying the exception
     */
    public inline fun debug(marker: Marker, t: Throwable, msg: () -> Any?) {
        if (isDebugEnabled(marker)) {
            parameter.debug(marker, msg().toString(), t)
        }
    }

    /**
     * Log a message at the INFO level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun info(msg: () -> Any?) {
        if (isInfoEnabled) {
            parameter.info(msg().toString())
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
            parameter.info(msg().toString(), t)
        }
    }

    /**
     * Log a message with the specific Marker at the INFO level.
     *
     * @param marker the marker data specific to this log statement
     * @param msg    a function that returns the message to be logged
     */
    public inline fun info(marker: Marker, msg: () -> Any?) {
        if (isInfoEnabled(marker)) {
            parameter.info(marker, msg().toString())
        }
    }

    /**
     * This method is similar to info(Throwable, () -> Any?)
     * method except that the marker data is also taken into
     * consideration.
     *
     * @param marker the marker data specific to this log statement
     * @param t      the exception (throwable) to log
     * @param msg    a function that returns the message accompanying the exception
     */
    public inline fun info(marker: Marker, t: Throwable, msg: () -> Any?) {
        if (isInfoEnabled(marker)) {
            parameter.info(marker, msg().toString(), t)
        }
    }

    /**
     * Log a message at the WARN level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun warn(msg: () -> Any?) {
        if (isWarnEnabled) {
            parameter.warn(msg().toString())
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
            parameter.warn(msg().toString(), t)
        }
    }

    /**
     * Log a message with the specific Marker at the WARN level.
     *
     * @param marker the marker data specific to this log statement
     * @param msg    a function that returns the message to be logged
     */
    public inline fun warn(marker: Marker, msg: () -> Any?) {
        if (isWarnEnabled(marker)) {
            parameter.warn(marker, msg().toString())
        }
    }

    /**
     * This method is similar to warn(Throwable, () -> Any?)
     * method except that the marker data is also taken into
     * consideration.
     *
     * @param marker the marker data specific to this log statement
     * @param t      the exception (throwable) to log
     * @param msg    a function that returns the message accompanying the exception
     */
    public inline fun warn(marker: Marker, t: Throwable, msg: () -> Any?) {
        if (isWarnEnabled(marker)) {
            parameter.warn(marker, msg().toString(), t)
        }
    }

    /**
     * Log a message at the ERROR level.
     *
     * @param msg a function that returns the message to be logged
     */
    public actual inline fun error(msg: () -> Any?) {
        if (isErrorEnabled) {
            parameter.error(msg().toString())
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
            parameter.error(msg().toString(), t)
        }
    }

    /**
     * Log a message with the specific Marker at the ERROR level.
     *
     * @param marker the marker data specific to this log statement
     * @param msg    a function that returns the message to be logged
     */
    public inline fun error(marker: Marker, msg: () -> Any?) {
        if (isErrorEnabled(marker)) {
            parameter.error(marker, msg().toString())
        }
    }

    /**
     * This method is similar to error(Throwable, () -> Any?)
     * method except that the marker data is also taken into
     * consideration.
     *
     * @param marker the marker data specific to this log statement
     * @param t      the exception (throwable) to log
     * @param msg    a function that returns the message accompanying the exception
     */
    public inline fun error(marker: Marker, t: Throwable, msg: () -> Any?) {
        if (isErrorEnabled(marker)) {
            parameter.error(marker, msg().toString(), t)
        }
    }
}
