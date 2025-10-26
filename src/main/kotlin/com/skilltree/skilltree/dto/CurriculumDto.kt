package com.skilltree.skilltree.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateCurriculumRequest(
    @field:NotBlank(message = "Curriculum name is required")
    @field:Size(min = 3, max = 200, message = "Curriculum name must be between 3 and 200 characters")
    val name: String,

    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String? = null,

    val prompt: String? = null
)

data class UpdateCurriculumRequest(
    @field:Size(min = 3, max = 200, message = "Curriculum name must be between 3 and 200 characters")
    val name: String? = null,

    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String? = null,

    val prompt: String? = null,

    val published: Boolean? = null
)

data class CurriculumResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val version: Int,
    val published: Boolean,
    val domains: List<DomainWithOrder>,
    val categories: List<CategoryWithOrder>,
    val subcategories: List<SubcategoryWithOrder>,
    val skills: List<SkillWithOrder>,
    val microskills: List<MicroskillWithOrder>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CurriculumSummaryResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val version: Int,
    val published: Boolean,
    val totalDomains: Int,
    val totalCategories: Int,
    val totalSubcategories: Int,
    val totalSkills: Int,
    val totalMicroskills: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class DomainWithOrder(
    val id: Long,
    val name: String,
    val description: String?,
    val sequenceOrder: Int,
    val required: Boolean
)

data class CategoryWithOrder(
    val id: Long,
    val name: String,
    val description: String?,
    val domainId: Long,
    val domainName: String,
    val sequenceOrder: Int,
    val required: Boolean
)

data class SubcategoryWithOrder(
    val id: Long,
    val name: String,
    val description: String?,
    val categoryId: Long,
    val categoryName: String,
    val sequenceOrder: Int,
    val required: Boolean
)

data class SkillWithOrder(
    val id: Long,
    val name: String,
    val description: String?,
    val level: Int,
    val subcategoryId: Long,
    val subcategoryName: String,
    val sequenceOrder: Int,
    val required: Boolean
)

data class MicroskillWithOrder(
    val id: Long,
    val name: String,
    val description: String?,
    val level: Int,
    val skillId: Long,
    val skillName: String,
    val sequenceOrder: Int,
    val required: Boolean
)

data class AddItemToCurriculumRequest(
    val sequenceOrder: Int = 0,
    val required: Boolean = true
)

data class CurriculumVersionResponse(
    val id: Long,
    val version: Int,
    val changeDescription: String?,
    val createdAt: LocalDateTime
)

data class PublishCurriculumRequest(
    val changeDescription: String? = null
)
