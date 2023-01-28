package org.mrmat.hello.kotlin.app.helloworld.spring.controllers

import org.mrmat.hello.kotlin.app.helloworld.spring.Version
import org.mrmat.hello.kotlin.app.helloworld.spring.model.GreetingModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldAPI {

    companion object {
        const val VERSION_HEADER = "X-APP-VERSION"
    }

    @GetMapping("/")
    fun index(
        @RequestParam("name", required = false, defaultValue = "Stranger") name: String
    ): ResponseEntity<GreetingModel> {
        return ResponseEntity.ok().header(VERSION_HEADER, Version.VERSION).body(GreetingModel(name = name))
    }
}
