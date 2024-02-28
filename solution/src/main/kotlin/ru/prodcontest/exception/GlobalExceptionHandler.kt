package ru.prodcontest.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(StatusCodeException::class)
    fun handleStatusCodeException(e: StatusCodeException): ResponseEntity<Any> {
        return ResponseEntity.status(e.statusCode).body(e.toMap())
    }

}