package com.skilltree.skilltree.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateSkillRequest(
    @field:NotBlank(message = "Skill name is required")
    @field:Size(min = 3, max = 100, message = "Skill name must be between 3 and 100 characters")
    val name: String,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null,

    @field:Min(value = 1, message = "Level must be at least 1")
    @field:Max(value = 10, message = "Level must not exceed 10")
    val level: Int = 1,

    @field:NotNull(message = "Subcategory ID is required")
    val subcategoryId: Long
)

data class UpdateSkillRequest(
    @field:Size(min = 3, max = 100, message = "Skill name must be between 3 and 100 characters")
    val name: String? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null,

    @field:Min(value = 1, message = "Level must be at least 1")
    @field:Max(value = 10, message = "Level must not exceed 10")
    val level: Int? = null,

    val subcategoryId: Long? = null
)

data class SkillResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val level: Int,
    val subcategoryId: Long,
    val subcategoryName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class SkillsResponse(
    val skills: List<SkillResponse>,
    val total: Int
)

data class DeleteResponse(
    val success: Boolean,
    val message: String
)

data class MessageResponse(
    val message: String
)
