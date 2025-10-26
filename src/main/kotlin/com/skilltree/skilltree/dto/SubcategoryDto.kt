package com.skilltree.skilltree.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateSubcategoryRequest(
    @field:NotBlank(message = "Subcategory name is required")
    @field:Size(min = 3, max = 100, message = "Subcategory name must be between 3 and 100 characters")
    val name: String,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null,

    @field:NotNull(message = "Category ID is required")
    val categoryId: Long
)

data class UpdateSubcategoryRequest(
    @field:Size(min = 3, max = 100, message = "Subcategory name must be between 3 and 100 characters")
    val name: String? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null,

    val categoryId: Long? = null
)

data class SubcategoryResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val categoryId: Long,
    val categoryName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class SubcategoriesResponse(
    val subcategories: List<SubcategoryResponse>,
    val total: Int
)
