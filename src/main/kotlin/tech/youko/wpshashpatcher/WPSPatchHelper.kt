package tech.youko.wpshashpatcher

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

enum class Architecture {
    I386, AMD64
}

fun patchExecutableFile(file: File) {
    RandomAccessFile(file, "rw").use {
        val data = it.channel.map(FileChannel.MapMode.READ_WRITE, 0, it.channel.size())
        // 设置处理方式为小端
        data.order(ByteOrder.LITTLE_ENDIAN)
        // 判断DOS头
        check(data.short == 0x5A4D.toShort()) { "Invalid DOS header." }
        // 移动到PE头
        data.position(0x3C)
        data.position(data.int)
        // 判断PE头
        check(data.int == 0x4550) { "Invalid PE header." }
        // 获取可执行文件架构
        val architecture = when (data.short) {
            0x014C.toShort() -> Architecture.I386
            0x8664.toShort() -> Architecture.AMD64
            else -> error("Unsupported architecture.")
        }
        // 修补
        patchExecutableFile(data, architecture)
    }
}

fun patchExecutableFile(data: ByteBuffer, architecture: Architecture) {
    val (anchorPattern, anchorMask, replacementPattern, replacement) = when (architecture) {
        Architecture.I386 -> arrayOf(
            byteArrayOf(0x00, 0x02, 0x00, 0x00, 0x73, -0x01, 0x56),
            byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, -0x01, 0x00),
            byteArrayOf(0x53, -0x75, -0x24),
            byteArrayOf(0x31, -0x40, -0x3D, -0x01)
        )

        Architecture.AMD64 -> arrayOf(
            byteArrayOf(0x63, -0x01, -0x01, -0x01, -0x01, 0x00, 0x02, 0x00, 0x00, 0x73),
            byteArrayOf(0x00, -0x01, -0x01, -0x01, -0x01, 0x00, 0x00, 0x00, 0x00, 0x00),
            byteArrayOf(0x48, -0x77, 0x5C, 0x24, 0x10),
            byteArrayOf(0x48, 0x31, -0x40, -0x3D, -0x01)
        )
    }

    // 检查是否已经修补，以替换的字节及其末尾的0xFF作为标识
    check(findPattern(data, replacement, maxMatches = 1).isEmpty()) { "Patch already applied." }
    // 以特征码模糊查找目标位置，最多查询2个结果以检测特征码的唯一性
    val anchorMatches = findPattern(data, anchorPattern, anchorMask, maxMatches = 2)
    check(anchorMatches.size == 1) { "Anchor pattern is not unique or not found." }
    // 从目标位置向前寻找函数起始位置
    data.position(anchorMatches[0])
    val replacementMatches = findPattern(data, replacementPattern, reverse = true, maxMatches = 1)
    check(replacementMatches.isNotEmpty()) { "Function start not found." }
    // 替换
    data.position(replacementMatches[0])
    data.put(replacement)
}
