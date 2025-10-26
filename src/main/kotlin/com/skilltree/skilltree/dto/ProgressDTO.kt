package com.skilltree.skilltree.dto

import java.time.LocalDateTime

data class MicroskillProgressDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val completed: Boolean,
    val progressPercentage: Double,
    val lastAccessed: LocalDateTime?
)

data class SkillProgressDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val completed: Boolean,
    val progressPercentage: Double,
    val lastAccessed: LocalDateTime?,
    val microskills: List<MicroskillProgressDTO>
)

data class SubcategoryProgressDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val completed: Boolean,
    val progressPercentage: Double,
    val lastAccessed: LocalDateTime?,
    val skills: List<SkillProgressDTO>
)

data class CategoryProgressDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val completed: Boolean,
    val progressPercentage: Double,
    val lastAccessed: LocalDateTime?,
    val subcategories: List<SubcategoryProgressDTO>
)

data class DomainProgressDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val completed: Boolean,
    val progressPercentage: Double,
    val lastAccessed: LocalDateTime?,
    val categories: List<CategoryProgressDTO>
)

data class UserProgressTreeDTO(
    val domains: List<DomainProgressDTO>,
    val overallProgress: Double
)

data class UserProfileDTO(
    val id: Long,
    val username: String,
    val email: String,
    val role: String,
    val thumbnailUrl: String?,
    val createdAt: LocalDateTime
)
