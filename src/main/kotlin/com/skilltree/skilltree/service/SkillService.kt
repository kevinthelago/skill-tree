package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.Skill
import com.skilltree.skilltree.exception.SkillNotFoundException
import com.skilltree.skilltree.exception.SubcategoryNotFoundException
import com.skilltree.skilltree.repository.SkillRepository
import com.skilltree.skilltree.repository.SubcategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SkillService(
    private val skillRepository: SkillRepository,
    private val subcategoryRepository: SubcategoryRepository
) {
    private val logger = LoggerFactory.getLogger(SkillService::class.java)

    @Transactional
    fun createSkill(request: CreateSkillRequest): SkillResponse {
        logger.info("Creating skill: ${request.name}")

        val subcategory = subcategoryRepository.findById(request.subcategoryId)
            .orElseThrow {
                logger.error("Subcategory not found with ID: ${request.subcategoryId}")
                SubcategoryNotFoundException("Subcategory not found with ID: ${request.subcategoryId}")
            }

        val skill = Skill(
            name = request.name,
            description = request.description,
            prompt = request.prompt,
            level = request.level,
            subcategory = subcategory
        )

        val savedSkill = skillRepository.save(skill)
        logger.info("Skill created successfully with ID: ${savedSkill.id}")

        return toSkillResponse(savedSkill)
    }

    fun getSkill(id: Long): SkillResponse {
        logger.info("Fetching skill with ID: $id")

        val skill = skillRepository.findById(id)
            .orElseThrow {
                logger.error("Skill not found with ID: $id")
                SkillNotFoundException("Skill not found with ID: $id")
            }

        return toSkillResponse(skill)
    }

    fun getAllSkills(subcategoryId: Long?, sortBy: String?): SkillsResponse {
        logger.info("Fetching all skills - subcategoryId: $subcategoryId, sortBy: $sortBy")

        val skills = when {
            subcategoryId != null -> skillRepository.findBySubcategoryIdOrderByLevelAsc(subcategoryId)
            sortBy == "name" -> skillRepository.findAllByOrderByNameAsc()
            else -> skillRepository.findAll()
        }

        logger.info("Retrieved ${skills.size} skills")

        return SkillsResponse(
            skills = skills.map { toSkillResponse(it) },
            total = skills.size
        )
    }

    @Transactional
    fun updateSkill(id: Long, request: UpdateSkillRequest): SkillResponse {
        logger.info("Updating skill with ID: $id")

        val skill = skillRepository.findById(id)
            .orElseThrow {
                logger.error("Skill not found with ID: $id")
                SkillNotFoundException("Skill not found with ID: $id")
            }

        request.name?.let { skill.name = it }
        request.description?.let { skill.description = it }
        request.prompt?.let { skill.prompt = it }
        request.level?.let { skill.level = it }
        request.subcategoryId?.let { subcategoryId ->
            val subcategory = subcategoryRepository.findById(subcategoryId)
                .orElseThrow {
                    logger.error("Subcategory not found with ID: $subcategoryId")
                    SubcategoryNotFoundException("Subcategory not found with ID: $subcategoryId")
                }
            skill.subcategory = subcategory
        }

        val updatedSkill = skillRepository.save(skill)
        logger.info("Skill updated successfully with ID: ${updatedSkill.id}")

        return toSkillResponse(updatedSkill)
    }

    @Transactional
    fun deleteSkill(id: Long): DeleteResponse {
        logger.info("Deleting skill with ID: $id")

        if (!skillRepository.existsById(id)) {
            logger.error("Skill not found with ID: $id")
            throw SkillNotFoundException("Skill not found with ID: $id")
        }

        skillRepository.deleteById(id)
        logger.info("Skill deleted successfully with ID: $id")

        return DeleteResponse(
            success = true,
            message = "Skill deleted successfully"
        )
    }

    private fun toSkillResponse(skill: Skill): SkillResponse {
        return SkillResponse(
            id = skill.id,
            name = skill.name,
            description = skill.description,
            level = skill.level,
            subcategoryId = skill.subcategory.id,
            subcategoryName = skill.subcategory.name,
            createdAt = skill.createdAt,
            updatedAt = skill.updatedAt
        )
    }
}
