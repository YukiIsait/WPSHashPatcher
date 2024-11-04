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
        if (e.message != null) {
            println("Failure: ${if (e.message!!.endsWith('.')) e.message!! else e.message!!.plus('.')}")
        }
        exitProcess(127)
    }
}
