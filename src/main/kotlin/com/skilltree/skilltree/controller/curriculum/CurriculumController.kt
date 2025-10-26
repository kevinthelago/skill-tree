package com.skilltree.skilltree.controller.curriculum

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.CurriculumService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/curricula")
class CurriculumController(
    private val curriculumService: CurriculumService
) {

    @PostMapping
    fun createCurriculum(@Valid @RequestBody request: CreateCurriculumRequest): ResponseEntity<CurriculumSummaryResponse> {
        val response = curriculumService.createCurriculum(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllCurricula(@RequestParam(required = false) published: Boolean?): ResponseEntity<List<CurriculumSummaryResponse>> {
        val curricula = if (published == true) {
            curriculumService.getPublishedCurricula()
        } else {
            curriculumService.getAllCurricula()
        }
        return ResponseEntity.ok(curricula)
    }

    @GetMapping("/{id}")
    fun getCurriculumById(@PathVariable id: Long): ResponseEntity<CurriculumResponse> {
        val curriculum = curriculumService.getCurriculumById(id)
        return ResponseEntity.ok(curriculum)
    }

    @PutMapping("/{id}")
    fun updateCurriculum(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateCurriculumRequest
    ): ResponseEntity<CurriculumSummaryResponse> {
        val updated = curriculumService.updateCurriculum(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCurriculum(@PathVariable id: Long): ResponseEntity<MessageResponse> {
        val response = curriculumService.deleteCurriculum(id)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/publish")
    fun publishCurriculum(
        @PathVariable id: Long,
        @RequestBody request: PublishCurriculumRequest
    ): ResponseEntity<CurriculumSummaryResponse> {
        val response = curriculumService.publishCurriculum(id, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}/versions")
    fun getCurriculumVersions(@PathVariable id: Long): ResponseEntity<List<CurriculumVersionResponse>> {
        val versions = curriculumService.getCurriculumVersions(id)
        return ResponseEntity.ok(versions)
    }

    // Add items to curriculum

    @PostMapping("/{id}/domains/{domainId}")
    fun addDomainToCurriculum(
        @PathVariable id: Long,
        @PathVariable domainId: Long,
        @RequestBody request: AddItemToCurriculumRequest
    ): ResponseEntity<MessageResponse> {
        val response = curriculumService.addDomainToCurriculum(id, domainId, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{id}/categories/{categoryId}")
    fun addCategoryToCurriculum(
        @PathVariable id: Long,
        @PathVariable categoryId: Long,
        @RequestBody request: AddItemToCurriculumRequest
    ): ResponseEntity<MessageResponse> {
        val response = curriculumService.addCategoryToCurriculum(id, categoryId, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{id}/subcategories/{subcategoryId}")
    fun addSubcategoryToCurriculum(
        @PathVariable id: Long,
        @PathVariable subcategoryId: Long,
        @RequestBody request: AddItemToCurriculumRequest
    ): ResponseEntity<MessageResponse> {
        val response = curriculumService.addSubcategoryToCurriculum(id, subcategoryId, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{id}/skills/{skillId}")
    fun addSkillToCurriculum(
        @PathVariable id: Long,
        @PathVariable skillId: Long,
        @RequestBody request: AddItemToCurriculumRequest
    ): ResponseEntity<MessageResponse> {
        val response = curriculumService.addSkillToCurriculum(id, skillId, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{id}/microskills/{microskillId}")
    fun addMicroskillToCurriculum(
        @PathVariable id: Long,
        @PathVariable microskillId: Long,
        @RequestBody request: AddItemToCurriculumRequest
    ): ResponseEntity<MessageResponse> {
        val response = curriculumService.addMicroskillToCurriculum(id, microskillId, request)
        return ResponseEntity.ok(response)
    }
}
