package tech.youko.wpshashpatcher

import java.nio.ByteBuffer

fun findPattern(
    data: ByteBuffer,
    pattern: ByteArray,
    mask: ByteArray? = null,
    index: Int = data.position(),
    reverse: Boolean = false,
    maxMatches: Int = Int.MAX_VALUE
): List<Int> {
    val matches = mutableListOf<Int>()
    val start = if (reverse) minOf(data.limit() - pattern.size, index) else maxOf(0, index)
    val range = if (reverse) start downTo 0 else start..data.limit() - pattern.size

    for (i in range) {
        if (pattern.indices.all { j -> mask != null && mask[j] == 0xFF.toByte() || pattern[j] == data[i + j] }) {
            matches.add(i)
            if (matches.size >= maxMatches) break
        }
    }
    return matches
}
