package org.mrmat.hello.kotlin.app.versioning

fun main(args: Array<String>) {
    println("Hello World!")
    println("Version: ${Version.VERSION}")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}
