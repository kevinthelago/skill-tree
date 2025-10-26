package com.skilltree.skilltree.controller.curriculum

import com.skilltree.skilltree.dto.ErrorResponse
import com.skilltree.skilltree.exception.InvalidHierarchyException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.curriculum"])
class CurriculumControllerAdvice {
    private val logger = LoggerFactory.getLogger(CurriculumControllerAdvice::class.java)

    @ExceptionHandler(InvalidHierarchyException::class)
    fun handleInvalidHierarchy(e: InvalidHierarchyException): ResponseEntity<ErrorResponse> {
        logger.error("Invalid hierarchy operation: ${e.message}")
        return ResponseEntity.status(400).body(
            ErrorResponse(
                errorCode = "INVALID_HIERARCHY",
                message = e.message ?: "Invalid curriculum hierarchy operation"
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

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.error("Illegal argument: ${e.message}")
        return ResponseEntity.status(400).body(
            ErrorResponse(
                errorCode = "INVALID_ARGUMENT",
                message = e.message ?: "Invalid argument provided"
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception in curriculum controller", e)
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "INTERNAL_ERROR",
                message = "An unexpected error occurred"
            )
        )
    }
}
