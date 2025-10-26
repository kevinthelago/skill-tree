package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.Microskill
import com.skilltree.skilltree.exception.MicroskillNotFoundException
import com.skilltree.skilltree.exception.SkillNotFoundException
import com.skilltree.skilltree.repository.MicroskillRepository
import com.skilltree.skilltree.repository.SkillRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MicroskillService(
    private val microskillRepository: MicroskillRepository,
    private val skillRepository: SkillRepository
) {
    private val logger = LoggerFactory.getLogger(MicroskillService::class.java)

    @Transactional
    fun createMicroskill(request: CreateMicroskillRequest): MicroskillResponse {
        logger.info("Creating microskill: ${request.name}")

        val skill = skillRepository.findById(request.skillId)
            .orElseThrow {
                logger.error("Skill not found with ID: ${request.skillId}")
                SkillNotFoundException("Skill not found with ID: ${request.skillId}")
            }

        val microskill = Microskill(
            name = request.name,
            description = request.description,
            prompt = request.prompt,
            level = request.level,
            skill = skill
        )

        val savedMicroskill = microskillRepository.save(microskill)
        logger.info("Microskill created successfully with ID: ${savedMicroskill.id}")

        return toMicroskillResponse(savedMicroskill)
    }

    fun getMicroskill(id: Long): MicroskillResponse {
        logger.info("Fetching microskill with ID: $id")

        val microskill = microskillRepository.findById(id)
            .orElseThrow {
                logger.error("Microskill not found with ID: $id")
                MicroskillNotFoundException("Microskill not found with ID: $id")
            }

        return toMicroskillResponse(microskill)
    }

    fun getAllMicroskills(skillId: Long?): MicroskillsResponse {
        logger.info("Fetching all microskills - skillId: $skillId")

        val microskills = if (skillId != null) {
            microskillRepository.findBySkillIdOrderByLevelAsc(skillId)
        } else {
            microskillRepository.findAll()
        }

        logger.info("Retrieved ${microskills.size} microskills")

        return MicroskillsResponse(
            microskills = microskills.map { toMicroskillResponse(it) },
            total = microskills.size
        )
    }

    @Transactional
    fun updateMicroskill(id: Long, request: UpdateMicroskillRequest): MicroskillResponse {
        logger.info("Updating microskill with ID: $id")

        val microskill = microskillRepository.findById(id)
            .orElseThrow {
                logger.error("Microskill not found with ID: $id")
                MicroskillNotFoundException("Microskill not found with ID: $id")
            }

        request.name?.let { microskill.name = it }
        request.description?.let { microskill.description = it }
        request.prompt?.let { microskill.prompt = it }
        request.level?.let { microskill.level = it }
        request.skillId?.let { skillId ->
            val skill = skillRepository.findById(skillId)
                .orElseThrow {
                    logger.error("Skill not found with ID: $skillId")
                    SkillNotFoundException("Skill not found with ID: $skillId")
                }
            microskill.skill = skill
        }

        val updatedMicroskill = microskillRepository.save(microskill)
        logger.info("Microskill updated successfully with ID: ${updatedMicroskill.id}")

        return toMicroskillResponse(updatedMicroskill)
    }

    @Transactional
    fun deleteMicroskill(id: Long): DeleteResponse {
        logger.info("Deleting microskill with ID: $id")

        if (!microskillRepository.existsById(id)) {
            logger.error("Microskill not found with ID: $id")
            throw MicroskillNotFoundException("Microskill not found with ID: $id")
        }

        microskillRepository.deleteById(id)
        logger.info("Microskill deleted successfully with ID: $id")

        return DeleteResponse(
            success = true,
            message = "Microskill deleted successfully"
        )
    }

    private fun toMicroskillResponse(microskill: Microskill): MicroskillResponse {
        return MicroskillResponse(
            id = microskill.id,
            name = microskill.name,
            description = microskill.description,
            level = microskill.level,
            skillId = microskill.skill.id,
            skillName = microskill.skill.name,
            createdAt = microskill.createdAt,
            updatedAt = microskill.updatedAt
        )
    }
}
