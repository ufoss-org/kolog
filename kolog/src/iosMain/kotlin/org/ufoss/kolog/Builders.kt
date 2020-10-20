/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

@file:Suppress("unused", "FunctionName", "NOTHING_TO_INLINE")

package org.ufoss.kolog

/**
 * Return a logger named corresponding to the class this function is called in
 */
public actual inline fun Logger(): Logger = Logger("test")
