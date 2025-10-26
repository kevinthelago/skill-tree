package com.skilltree.skilltree.controller.subcategory

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.SubcategoryService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subcategories")
class SubcategoryController(
    private val subcategoryService: SubcategoryService
) {
    private val logger = LoggerFactory.getLogger(SubcategoryController::class.java)

    @PostMapping
    fun createSubcategory(@Valid @RequestBody request: CreateSubcategoryRequest): ResponseEntity<SubcategoryResponse> {
        logger.info("POST /api/subcategories - Creating subcategory: ${request.name}")
        val subcategory = subcategoryService.createSubcategory(request)
        logger.info("POST /api/subcategories - Subcategory created with ID: ${subcategory.id}")
        return ResponseEntity.ok(subcategory)
    }

    @GetMapping("/{id}")
    fun getSubcategory(@PathVariable id: Long): ResponseEntity<SubcategoryResponse> {
        logger.info("GET /api/subcategories/$id - Request received")
        val subcategory = subcategoryService.getSubcategory(id)
        logger.info("GET /api/subcategories/$id - Completed successfully")
        return ResponseEntity.ok(subcategory)
    }

    @GetMapping
    fun getAllSubcategories(@RequestParam(required = false) categoryId: Long?): ResponseEntity<SubcategoriesResponse> {
        logger.info("GET /api/subcategories - categoryId: $categoryId")
        val response = subcategoryService.getAllSubcategories(categoryId)
        logger.info("GET /api/subcategories - Retrieved ${response.total} subcategories")
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateSubcategory(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateSubcategoryRequest
    ): ResponseEntity<SubcategoryResponse> {
        logger.info("PUT /api/subcategories/$id - Updating subcategory")
        val subcategory = subcategoryService.updateSubcategory(id, request)
        logger.info("PUT /api/subcategories/$id - Subcategory updated successfully")
        return ResponseEntity.ok(subcategory)
    }

    @DeleteMapping("/{id}")
    fun deleteSubcategory(@PathVariable id: Long): ResponseEntity<DeleteResponse> {
        logger.info("DELETE /api/subcategories/$id - Request received")
        val response = subcategoryService.deleteSubcategory(id)
        logger.info("DELETE /api/subcategories/$id - Subcategory deleted successfully")
        return ResponseEntity.ok(response)
    }
}
