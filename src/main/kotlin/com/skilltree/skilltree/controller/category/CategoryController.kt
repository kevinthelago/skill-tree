package com.skilltree.skilltree.controller.category

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.CategoryService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    private val logger = LoggerFactory.getLogger(CategoryController::class.java)

    @PostMapping
    fun createCategory(@Valid @RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        logger.info("POST /api/categories - Creating category: ${request.name}")
        val category = categoryService.createCategory(request)
        logger.info("POST /api/categories - Category created with ID: ${category.id}")
        return ResponseEntity.ok(category)
    }

    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): ResponseEntity<CategoryResponse> {
        logger.info("GET /api/categories/$id - Request received")
        val category = categoryService.getCategory(id)
        logger.info("GET /api/categories/$id - Completed successfully")
        return ResponseEntity.ok(category)
    }

    @GetMapping
    fun getAllCategories(@RequestParam(required = false) domainId: Long?): ResponseEntity<CategoriesResponse> {
        logger.info("GET /api/categories - domainId: $domainId")
        val response = categoryService.getAllCategories(domainId)
        logger.info("GET /api/categories - Retrieved ${response.total} categories")
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        logger.info("PUT /api/categories/$id - Updating category")
        val category = categoryService.updateCategory(id, request)
        logger.info("PUT /api/categories/$id - Category updated successfully")
        return ResponseEntity.ok(category)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<DeleteResponse> {
        logger.info("DELETE /api/categories/$id - Request received")
        val response = categoryService.deleteCategory(id)
        logger.info("DELETE /api/categories/$id - Category deleted successfully")
        return ResponseEntity.ok(response)
    }
}
