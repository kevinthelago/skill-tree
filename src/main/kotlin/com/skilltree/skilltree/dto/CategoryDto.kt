package com.skilltree.skilltree.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateCategoryRequest(
    @field:NotBlank(message = "Category name is required")
    @field:Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
    val name: String,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null,

    @field:NotNull(message = "Domain ID is required")
    val domainId: Long
)

data class UpdateCategoryRequest(
    @field:Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
    val name: String? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null,

    val domainId: Long? = null
)

data class CategoryResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val domainId: Long,
    val domainName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CategoriesResponse(
    val categories: List<CategoryResponse>,
    val total: Int
)
