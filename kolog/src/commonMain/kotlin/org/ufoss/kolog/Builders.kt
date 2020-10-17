/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

// fixme verify this naming is working as expected
@file:JvmMultifileClass
@file:JvmName("LoggerKt")
@file:Suppress("FunctionName")

package org.ufoss.kolog

import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * Return a logger named corresponding to the class this function is called in
 */
@JsName("logger")
public expect inline fun Logger(): Logger
