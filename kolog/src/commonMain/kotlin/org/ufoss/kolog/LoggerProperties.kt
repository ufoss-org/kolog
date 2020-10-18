/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

// fixme verify this naming is working as expected
@file:JvmMultifileClass
@file:JvmName("LoggerKt")

package org.ufoss.kolog

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

internal interface LoggerProperties {
    /**
     * Return the name of this <code>Logger</code> instance.
     * @return name of this logger instance
     */
    val name: String

    /**
     * Is the logger instance enabled for the TRACE level?
     *
     * @return True if this Logger is enabled for the TRACE level, false otherwise.
     */
    val isTraceEnabled: Boolean

    /**
     * Is the logger instance enabled for the DEBUG level?
     *
     * @return True if this Logger is enabled for the DEBUG level, false otherwise.
     */
    val isDebugEnabled: Boolean

    /**
     * Is the logger instance enabled for the INFO level?
     *
     * @return True if this Logger is enabled for the INFO level, false otherwise.
     */
    val isInfoEnabled: Boolean

    /**
     * Is the logger instance enabled for the WARN level?
     *
     * @return True if this Logger is enabled for the WARN level, false otherwise.
     */
    val isWarnEnabled: Boolean

    /**
     * Is the logger instance enabled for the ERROR level?
     *
     * @return True if this Logger is enabled for the ERROR level, false otherwise.
     */
    val isErrorEnabled: Boolean
}
