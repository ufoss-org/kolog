/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:JvmMultifileClass
@file:JvmName("LoggerKt")
@file:Suppress("unused", "FunctionName", "NOTHING_TO_INLINE")

package org.ufoss.kolog

import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

/**
 * Return a logger named corresponding to the class this function is called in
 */
public inline fun Logger(): Logger = Logger(LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()))
