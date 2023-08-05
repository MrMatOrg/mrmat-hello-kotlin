package org.mrmat.hello.kotlin.app.helloworld.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main application class
 */
@SpringBootApplication
class App

/**
 * Main application entry point
 * @param args Command line parameters
 */
@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<App>(*args)
}
