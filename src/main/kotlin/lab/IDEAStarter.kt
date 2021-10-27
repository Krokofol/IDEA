package lab

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

@ExperimentalUnsignedTypes
class IDEAStarter {

    private val scanner = Scanner(System.`in`)

    fun start(args: Array<String>) {
        when (args.size) {
            1 -> idea.code(BufferedReader(FileReader(args[0])))
            2 -> idea.decode(BufferedReader(FileReader(args[0])), BufferedReader(FileReader(args[1])))
            else -> {
                println("Wrong args")
                startWithoutArgs()
            }
        }
    }

    fun startWithoutArgs() {
        var action: Action? = null
        while (action == null) {
            println("Enter code/decode")
            try {
                action = Action.valueOf(scanner.nextLine().uppercase(Locale.getDefault()))
            } catch (e: Exception) {
                println("Try again")
            }
        }
        when (action) {
            Action.CODE -> {
                var textFileName = "fileName"
                while (!File(textFileName).exists()) {
                    println("Enter your file name (with format)")
                    textFileName = scanner.nextLine()
                }
                idea.code(BufferedReader(FileReader(textFileName)))
            }
            Action.DECODE -> {
                var codeFileName = "fileName"
                var keyFileName = "fileName"
                while (!File(codeFileName).exists()) {
                    println("Enter your code file name (with format)")
                    codeFileName = scanner.nextLine()
                }
                while (!File(keyFileName).exists()) {
                    println("Enter your key file name (with format)")
                    keyFileName = scanner.nextLine()
                }
                idea.decode(BufferedReader(FileReader(codeFileName)), BufferedReader(FileReader(keyFileName)))
            }
        }
    }

    companion object {
        private val idea = IDEA()

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isNotEmpty()) {
                IDEAStarter().start(args)
            } else {
                IDEAStarter().startWithoutArgs()
            }
        }
    }
}