
/*
 * This is free and unencumbered software released into the public domain, following <https://unlicense.org>
 */

package org.ufoss.kolog

public fun interface TimeScope {
    public fun printTime()
}

@PublishedApi
internal object NoopTimeScope : TimeScope {
    override fun printTime() {
        /* no-op */
    }
}
