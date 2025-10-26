package com.skilltree.skilltree.controller.source

import com.skilltree.skilltree.dto.SourceResponse
import com.skilltree.skilltree.dto.SourceWithRelevanceResponse
import com.skilltree.skilltree.service.SourceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sources")
class SourceController(
    private val sourceService: SourceService
) {

    @GetMapping
    fun getAllSources(): ResponseEntity<List<SourceResponse>> {
        val sources = sourceService.getAllSources()
        return ResponseEntity.ok(sources)
    }

    @GetMapping("/{id}")
    fun getSourceById(@PathVariable id: Long): ResponseEntity<SourceResponse> {
        val source = sourceService.getSourceById(id)
        return if (source != null) {
            ResponseEntity.ok(source)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/domain/{domainId}")
    fun getSourcesForDomain(@PathVariable domainId: Long): ResponseEntity<List<SourceWithRelevanceResponse>> {
        val sources = sourceService.getSourcesForDomain(domainId)
        return ResponseEntity.ok(sources)
    }

    @GetMapping("/category/{categoryId}")
    fun getSourcesForCategory(@PathVariable categoryId: Long): ResponseEntity<List<SourceWithRelevanceResponse>> {
        val sources = sourceService.getSourcesForCategory(categoryId)
        return ResponseEntity.ok(sources)
    }

    @GetMapping("/subcategory/{subcategoryId}")
    fun getSourcesForSubcategory(@PathVariable subcategoryId: Long): ResponseEntity<List<SourceWithRelevanceResponse>> {
        val sources = sourceService.getSourcesForSubcategory(subcategoryId)
        return ResponseEntity.ok(sources)
    }

    @GetMapping("/skill/{skillId}")
    fun getSourcesForSkill(@PathVariable skillId: Long): ResponseEntity<List<SourceWithRelevanceResponse>> {
        val sources = sourceService.getSourcesForSkill(skillId)
        return ResponseEntity.ok(sources)
    }

    @GetMapping("/microskill/{microskillId}")
    fun getSourcesForMicroskill(@PathVariable microskillId: Long): ResponseEntity<List<SourceWithRelevanceResponse>> {
        val sources = sourceService.getSourcesForMicroskill(microskillId)
        return ResponseEntity.ok(sources)
    }
}
