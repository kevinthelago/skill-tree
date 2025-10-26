package com.skilltree.skilltree.controller.resource

import com.skilltree.skilltree.dto.ErrorResponse
import com.skilltree.skilltree.exception.InvalidHierarchyException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException
import java.io.IOException

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.resource"])
class ResourceControllerAdvice {
    private val logger = LoggerFactory.getLogger(ResourceControllerAdvice::class.java)

    @ExceptionHandler(InvalidHierarchyException::class)
    fun handleInvalidHierarchy(e: InvalidHierarchyException): ResponseEntity<ErrorResponse> {
        logger.error("Invalid hierarchy operation: ${e.message}")
        return ResponseEntity.status(400).body(
            ErrorResponse(
                errorCode = "INVALID_HIERARCHY",
                message = e.message ?: "Invalid resource hierarchy operation"
            )
        )
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxSizeException(e: MaxUploadSizeExceededException): ResponseEntity<ErrorResponse> {
        logger.error("File size exceeds maximum: ${e.message}")
        return ResponseEntity.status(413).body(
            ErrorResponse(
                errorCode = "FILE_TOO_LARGE",
                message = "File size exceeds maximum allowed size (10MB)"
            )
        )
    }

    @ExceptionHandler(IOException::class)
    fun handleIOException(e: IOException): ResponseEntity<ErrorResponse> {
        logger.error("IO error during file operation: ${e.message}")
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "FILE_IO_ERROR",
                message = "Error processing file: ${e.message}"
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

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error("Runtime exception in resource controller: ${e.message}", e)

        // Check if it's a file-related error
        if (e.message?.contains("File not found", ignoreCase = true) == true ||
            e.message?.contains("Could not", ignoreCase = true) == true) {
            return ResponseEntity.status(404).body(
                ErrorResponse(
                    errorCode = "FILE_NOT_FOUND",
                    message = e.message ?: "File not found"
                )
            )
        }

        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "RUNTIME_ERROR",
                message = e.message ?: "An error occurred processing the resource"
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception in resource controller", e)
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "INTERNAL_ERROR",
                message = "An unexpected error occurred"
            )
        )
    }
}
