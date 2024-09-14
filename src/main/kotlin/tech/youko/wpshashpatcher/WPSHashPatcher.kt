package tech.youko.wpshashpatcher

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val parser = ArgParser("WPSHashPatcher")
    val architecture by parser.option(
        ArgType.Choice<Architecture>(),
        shortName = "a",
        description = "Architecture of the executable file"
    ).default(Architecture.I386)
    val filePath by parser.argument(ArgType.String, description = "Path to the executable file")
    try {
        parser.parse(args)
        RandomAccessFile(filePath, "rw").use { file ->
            patchExecutableFile(
                file.channel.map(FileChannel.MapMode.READ_WRITE, 0, file.channel.size()),
                architecture
            )
        }
    } catch (e: Exception) {
        println("Failure: ${e.message}")
        exitProcess(1)
    }
}
