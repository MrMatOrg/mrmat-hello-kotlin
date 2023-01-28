package org.mrmat.hello.kotlin.app.helloworld.spring.model

class GreetingModel(name: String = "Stranger") {
    val greeting: String

    init {
        greeting = "Hello $name"
    }
}
