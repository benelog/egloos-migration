package net.benelog.blog.migration.item

object ExternalProcesses {

    fun execute(command: String, stdin: String):String {
        val commands = command.split(" ")
        ProcessBuilder(commands).run {
            redirectErrorStream(true)
            return@run start();
        }.run {
            outputStream.apply {
                write(stdin.toByteArray())
                close()
            }
            waitFor()
            return inputStream.bufferedReader().use {
                it.readText();
            }
        }
    }
}