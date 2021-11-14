package lab

import java.util.*

@ExperimentalUnsignedTypes
class IDEAStarter {

    private val scanner = Scanner(System.`in`)

    fun start(args: Array<String>) {
        if (args.isNotEmpty()) {
            val argBuilder = StringBuilder()
            args.forEach { word ->
                argBuilder.append(word)
            }
            idea.code(argBuilder.toString())
        } else {
            println("Wrong args")
            startWithoutArgs()
        }
    }

    fun startWithoutArgs() {
        var textFileName : String? = null
        while (textFileName == null) {
            println("Enter your text to hash")
            textFileName = scanner.nextLine()
        }
        idea.code(textFileName)
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