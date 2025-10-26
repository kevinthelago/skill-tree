package com.skilltree.skilltree.controller.domain

import com.skilltree.skilltree.dto.ErrorResponse
import com.skilltree.skilltree.exception.DomainNotFoundException
import com.skilltree.skilltree.exception.DuplicateUserException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.domain"])
class DomainControllerAdvice {
    private val logger = LoggerFactory.getLogger(DomainControllerAdvice::class.java)

    @ExceptionHandler(DomainNotFoundException::class)
    fun handleDomainNotFound(e: DomainNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Domain not found: ${e.message}")
        return ResponseEntity.status(404).body(
            ErrorResponse(
                errorCode = "DOMAIN_NOT_FOUND",
                message = e.message ?: "Domain not found"
            )
        )
    }

    @ExceptionHandler(DuplicateUserException::class)
    fun handleDuplicateDomain(e: DuplicateUserException): ResponseEntity<ErrorResponse> {
        logger.error("Duplicate domain: ${e.message}")
        return ResponseEntity.status(409).body(
            ErrorResponse(
                errorCode = "DUPLICATE_DOMAIN",
                message = e.message ?: "Domain already exists"
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
        logger.error("Unhandled exception in domain controller", e)
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "INTERNAL_ERROR",
                message = "An unexpected error occurred"
            )
        )
    }
}
