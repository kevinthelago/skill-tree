package com.skilltree.skilltree.controller.microskill

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.MicroskillService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/microskills")
class MicroskillController(
    private val microskillService: MicroskillService
) {
    private val logger = LoggerFactory.getLogger(MicroskillController::class.java)

    @PostMapping
    fun createMicroskill(@Valid @RequestBody request: CreateMicroskillRequest): ResponseEntity<MicroskillResponse> {
        logger.info("POST /api/microskills - Creating microskill: ${request.name}")
        val microskill = microskillService.createMicroskill(request)
        logger.info("POST /api/microskills - Microskill created with ID: ${microskill.id}")
        return ResponseEntity.ok(microskill)
    }

    @GetMapping("/{id}")
    fun getMicroskill(@PathVariable id: Long): ResponseEntity<MicroskillResponse> {
        logger.info("GET /api/microskills/$id - Request received")
        val microskill = microskillService.getMicroskill(id)
        logger.info("GET /api/microskills/$id - Completed successfully")
        return ResponseEntity.ok(microskill)
    }

    @GetMapping
    fun getAllMicroskills(@RequestParam(required = false) skillId: Long?): ResponseEntity<MicroskillsResponse> {
        logger.info("GET /api/microskills - skillId: $skillId")
        val response = microskillService.getAllMicroskills(skillId)
        logger.info("GET /api/microskills - Retrieved ${response.total} microskills")
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateMicroskill(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateMicroskillRequest
    ): ResponseEntity<MicroskillResponse> {
        logger.info("PUT /api/microskills/$id - Updating microskill")
        val microskill = microskillService.updateMicroskill(id, request)
        logger.info("PUT /api/microskills/$id - Microskill updated successfully")
        return ResponseEntity.ok(microskill)
    }

    @DeleteMapping("/{id}")
    fun deleteMicroskill(@PathVariable id: Long): ResponseEntity<DeleteResponse> {
        logger.info("DELETE /api/microskills/$id - Request received")
        val response = microskillService.deleteMicroskill(id)
        logger.info("DELETE /api/microskills/$id - Microskill deleted successfully")
        return ResponseEntity.ok(response)
    }
}
