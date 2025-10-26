package com.skilltree.skilltree.controller.category

import com.skilltree.skilltree.dto.ErrorResponse
import com.skilltree.skilltree.exception.CategoryNotFoundException
import com.skilltree.skilltree.exception.DomainNotFoundException
import com.skilltree.skilltree.exception.DuplicateUserException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.category"])
class CategoryControllerAdvice {
    private val logger = LoggerFactory.getLogger(CategoryControllerAdvice::class.java)

    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFound(e: CategoryNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Category not found: ${e.message}")
        return ResponseEntity.status(404).body(
            ErrorResponse(
                errorCode = "CATEGORY_NOT_FOUND",
                message = e.message ?: "Category not found"
            )
        )
    }

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
    fun handleDuplicateCategory(e: DuplicateUserException): ResponseEntity<ErrorResponse> {
        logger.error("Duplicate category: ${e.message}")
        return ResponseEntity.status(409).body(
            ErrorResponse(
                errorCode = "DUPLICATE_CATEGORY",
                message = e.message ?: "Category already exists"
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
        logger.error("Unhandled exception in category controller", e)
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "INTERNAL_ERROR",
                message = "An unexpected error occurred"
            )
        )
    }
}
