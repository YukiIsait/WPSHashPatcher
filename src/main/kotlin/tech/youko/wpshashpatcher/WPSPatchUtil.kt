package tech.youko.wpshashpatcher

import java.nio.ByteBuffer

enum class Architecture {
    I386, AMD64
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

    // 检查是否已经打了补丁，以替换字节及其末尾的 0xFF 标识
    if (findPattern(data, replacement, maxMatches = 1).isNotEmpty()) {
        throw IllegalStateException("Patch already applied.")
    }

    // 以特征码模糊查找目标位置，最多查询 2 个结果以检测特征码的唯一性
    val anchorMatches = findPattern(data, anchorPattern, anchorMask, maxMatches = 2)
    if (anchorMatches.size != 1) {
        throw IllegalStateException("Anchor pattern is not unique or not found.")
    }

    // 从目标位置向前寻找函数起始位置
    val replacementMatches = findPattern(data, replacementPattern, null, anchorMatches[0], true, 1)
    if (replacementMatches.isEmpty()) {
        throw IllegalStateException("Function start not found.")
    }

    // 替换
    data.put(replacementMatches[0], replacement)
}
