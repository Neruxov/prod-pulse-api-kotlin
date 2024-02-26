package ru.prodcontest.kotlinprodcontesttemplate

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ping")
class PingController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun ping() {

    }

}
