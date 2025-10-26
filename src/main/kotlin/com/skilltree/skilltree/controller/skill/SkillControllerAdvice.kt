package com.skilltree.skilltree.controller.skill

import com.skilltree.skilltree.dto.ErrorResponse
import com.skilltree.skilltree.exception.SkillNotFoundException
import com.skilltree.skilltree.exception.SubcategoryNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["com.skilltree.skilltree.controller.skill"])
class SkillControllerAdvice {
    private val logger = LoggerFactory.getLogger(SkillControllerAdvice::class.java)

    @ExceptionHandler(SkillNotFoundException::class)
    fun handleSkillNotFound(e: SkillNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Skill not found: ${e.message}")
        return ResponseEntity.status(404).body(
            ErrorResponse(
                errorCode = "SKILL_NOT_FOUND",
                message = e.message ?: "Skill not found"
            )
        )
    }

    @ExceptionHandler(SubcategoryNotFoundException::class)
    fun handleSubcategoryNotFound(e: SubcategoryNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Subcategory not found: ${e.message}")
        return ResponseEntity.status(404).body(
            ErrorResponse(
                errorCode = "SUBCATEGORY_NOT_FOUND",
                message = e.message ?: "Subcategory not found"
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
        logger.error("Unhandled exception in skill controller", e)
        return ResponseEntity.status(500).body(
            ErrorResponse(
                errorCode = "INTERNAL_ERROR",
                message = "An unexpected error occurred"
            )
        )
    }
}
