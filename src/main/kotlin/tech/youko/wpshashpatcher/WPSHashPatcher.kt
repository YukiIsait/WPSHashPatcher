package tech.youko.wpshashpatcher

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val parser = ArgParser("WPSHashPatcher")
    val filePath by parser.argument(ArgType.String, description = "Path to the executable file")
    try {
        parser.parse(args)
        patchExecutableFile(File(filePath))
    } catch (e: Exception) {
        println("Failure: ${e.message}")
        exitProcess(1)
    }
}
