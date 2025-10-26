package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.User
import com.skilltree.skilltree.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProgressService(
    private val domainRepository: DomainRepository,
    private val userDomainProgressRepository: UserDomainProgressRepository,
    private val userCategoryProgressRepository: UserCategoryProgressRepository,
    private val userSubcategoryProgressRepository: UserSubcategoryProgressRepository,
    private val userSkillProgressRepository: UserSkillProgressRepository,
    private val userMicroskillProgressRepository: UserMicroskillProgressRepository
) {

    fun getUserProgressTree(user: User): UserProgressTreeDTO {
        val domains = domainRepository.findAll()
        val domainProgressMap = userDomainProgressRepository.findAllByUserId(user.id)
            .associateBy { it.domain.id }
        val categoryProgressMap = userCategoryProgressRepository.findAllByUserId(user.id)
            .associateBy { it.category.id }
        val subcategoryProgressMap = userSubcategoryProgressRepository.findAllByUserId(user.id)
            .associateBy { it.subcategory.id }
        val skillProgressMap = userSkillProgressRepository.findAllByUserId(user.id)
            .associateBy { it.skill.id }
        val microskillProgressMap = userMicroskillProgressRepository.findAllByUserId(user.id)
            .associateBy { it.microskill.id }

        val domainProgressDTOs = domains.map { domain ->
            val categories = domain.categories.map { category ->
                val subcategories = category.subcategories.map { subcategory ->
                    val skills = subcategory.skills.map { skill ->
                        val microskills = skill.microskills.map { microskill ->
                            val progress = microskillProgressMap[microskill.id]
                            MicroskillProgressDTO(
                                id = microskill.id,
                                name = microskill.name,
                                description = microskill.description,
                                completed = progress?.completed ?: false,
                                progressPercentage = progress?.progressPercentage ?: 0.0,
                                lastAccessed = progress?.lastAccessed
                            )
                        }

                        val skillProgress = skillProgressMap[skill.id]
                        val calculatedProgress = if (microskills.isEmpty()) 0.0
                        else microskills.map { it.progressPercentage }.average()

                        SkillProgressDTO(
                            id = skill.id,
                            name = skill.name,
                            description = skill.description,
                            completed = skillProgress?.completed ?: microskills.all { it.completed },
                            progressPercentage = skillProgress?.progressPercentage ?: calculatedProgress,
                            lastAccessed = skillProgress?.lastAccessed,
                            microskills = microskills
                        )
                    }

                    val subcategoryProgress = subcategoryProgressMap[subcategory.id]
                    val calculatedProgress = if (skills.isEmpty()) 0.0
                    else skills.map { it.progressPercentage }.average()

                    SubcategoryProgressDTO(
                        id = subcategory.id,
                        name = subcategory.name,
                        description = subcategory.description,
                        completed = subcategoryProgress?.completed ?: skills.all { it.completed },
                        progressPercentage = subcategoryProgress?.progressPercentage ?: calculatedProgress,
                        lastAccessed = subcategoryProgress?.lastAccessed,
                        skills = skills
                    )
                }

                val categoryProgress = categoryProgressMap[category.id]
                val calculatedProgress = if (subcategories.isEmpty()) 0.0
                else subcategories.map { it.progressPercentage }.average()

                CategoryProgressDTO(
                    id = category.id,
                    name = category.name,
                    description = category.description,
                    completed = categoryProgress?.completed ?: subcategories.all { it.completed },
                    progressPercentage = categoryProgress?.progressPercentage ?: calculatedProgress,
                    lastAccessed = categoryProgress?.lastAccessed,
                    subcategories = subcategories
                )
            }

            val domainProgress = domainProgressMap[domain.id]
            val calculatedProgress = if (categories.isEmpty()) 0.0
            else categories.map { it.progressPercentage }.average()

            DomainProgressDTO(
                id = domain.id,
                name = domain.name,
                description = domain.description,
                completed = domainProgress?.completed ?: categories.all { it.completed },
                progressPercentage = domainProgress?.progressPercentage ?: calculatedProgress,
                lastAccessed = domainProgress?.lastAccessed,
                categories = categories
            )
        }

        val overallProgress = if (domainProgressDTOs.isEmpty()) 0.0
        else domainProgressDTOs.map { it.progressPercentage }.average()

        return UserProgressTreeDTO(
            domains = domainProgressDTOs,
            overallProgress = overallProgress
        )
    }

    fun getUserProfile(user: User): UserProfileDTO {
        return UserProfileDTO(
            id = user.id,
            username = user.username,
            email = user.email,
            role = user.role,
            thumbnailUrl = user.thumbnailUrl,
            createdAt = user.createdAt
        )
    }
}
