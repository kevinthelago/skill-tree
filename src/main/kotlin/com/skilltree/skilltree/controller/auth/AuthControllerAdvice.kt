package com.skilltree.skilltree.controller.auth

import com.skilltree.skilltree.dto.ErrorResponse
import com.skilltree.skilltree.exception.DuplicateUserException
import com.skilltree.skilltree.exception.InvalidCredentialsException
import com.skilltree.skilltree.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.auth"])
class AuthControllerAdvice {
    private val logger = LoggerFactory.getLogger(AuthControllerAdvice::class.java)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("User not found: ${e.message}")
        return ResponseEntity.status(404).body(
            ErrorResponse(
                errorCode = "USER_NOT_FOUND",
                message = e.message ?: "User not found"
            )
        )
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(e: InvalidCredentialsException): ResponseEntity<ErrorResponse> {
        logger.error("Invalid credentials: ${e.message}")
        return ResponseEntity.status(401).body(
            ErrorResponse(
                errorCode = "INVALID_CREDENTIALS",
                message = e.message ?: "Invalid credentials"
            )
        )
    }

    @ExceptionHandler(DuplicateUserException::class)
    fun handleDuplicateUser(e: DuplicateUserException): ResponseEntity<ErrorResponse> {
        logger.error("Duplicate user: ${e.message}")
        return ResponseEntity.status(409).body(
            ErrorResponse(
                errorCode = "DUPLICATE_USER",
                message = e.message ?: "User already exists"
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = e.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Invalid value"
            fieldName to errorMessage
        }

        logger.error("Validation errors: $errors")

        return ResponseEntity.status(400).body(
            ErrorResponse(
                errorCode = "VALIDATION_ERROR",
                message = "Validation failed",
                details = errors
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception in auth controller", e)
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "INTERNAL_ERROR",
                message = "An unexpected error occurred"
            )
        )
    }
}
