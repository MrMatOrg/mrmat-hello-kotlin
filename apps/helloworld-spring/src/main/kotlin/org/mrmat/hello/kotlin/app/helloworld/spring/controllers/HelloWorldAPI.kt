package org.mrmat.hello.kotlin.app.helloworld.spring.controllers

import org.mrmat.hello.kotlin.app.helloworld.spring.Version
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldAPI {

    @GetMapping("/")
    fun index(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World, from HelloWorldAPI in version " + Version.VERSION)
    }
}
