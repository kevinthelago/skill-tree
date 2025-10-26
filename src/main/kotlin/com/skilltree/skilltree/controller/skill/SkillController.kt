package com.skilltree.skilltree.controller.skill

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.SkillService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/skills")
class SkillController(
    private val skillService: SkillService
) {
    private val logger = LoggerFactory.getLogger(SkillController::class.java)

    @PostMapping
    fun createSkill(@Valid @RequestBody request: CreateSkillRequest): ResponseEntity<SkillResponse> {
        logger.info("POST /api/skills - Creating skill: ${request.name}")
        val skill = skillService.createSkill(request)
        logger.info("POST /api/skills - Skill created with ID: ${skill.id}")
        return ResponseEntity.ok(skill)
    }

    @GetMapping("/{id}")
    fun getSkill(@PathVariable id: Long): ResponseEntity<SkillResponse> {
        logger.info("GET /api/skills/$id - Request received")
        val skill = skillService.getSkill(id)
        logger.info("GET /api/skills/$id - Completed successfully")
        return ResponseEntity.ok(skill)
    }

    @GetMapping
    fun getAllSkills(
        @RequestParam(required = false) subcategoryId: Long?,
        @RequestParam(required = false) sortBy: String?
    ): ResponseEntity<SkillsResponse> {
        logger.info("GET /api/skills - subcategoryId: $subcategoryId, sortBy: $sortBy")
        val response = skillService.getAllSkills(subcategoryId, sortBy)
        logger.info("GET /api/skills - Retrieved ${response.total} skills")
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateSkill(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateSkillRequest
    ): ResponseEntity<SkillResponse> {
        logger.info("PUT /api/skills/$id - Updating skill")
        val skill = skillService.updateSkill(id, request)
        logger.info("PUT /api/skills/$id - Skill updated successfully")
        return ResponseEntity.ok(skill)
    }

    @DeleteMapping("/{id}")
    fun deleteSkill(@PathVariable id: Long): ResponseEntity<DeleteResponse> {
        logger.info("DELETE /api/skills/$id - Request received")
        val response = skillService.deleteSkill(id)
        logger.info("DELETE /api/skills/$id - Skill deleted successfully")
        return ResponseEntity.ok(response)
    }
}
