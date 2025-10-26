package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.Subcategory
import com.skilltree.skilltree.exception.CategoryNotFoundException
import com.skilltree.skilltree.exception.DuplicateUserException
import com.skilltree.skilltree.exception.SubcategoryNotFoundException
import com.skilltree.skilltree.repository.CategoryRepository
import com.skilltree.skilltree.repository.SubcategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubcategoryService(
    private val subcategoryRepository: SubcategoryRepository,
    private val categoryRepository: CategoryRepository
) {
    private val logger = LoggerFactory.getLogger(SubcategoryService::class.java)

    @Transactional
    fun createSubcategory(request: CreateSubcategoryRequest): SubcategoryResponse {
        logger.info("Creating subcategory: ${request.name}")

        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow {
                logger.error("Category not found with ID: ${request.categoryId}")
                CategoryNotFoundException("Category not found with ID: ${request.categoryId}")
            }

        if (subcategoryRepository.existsByNameAndCategoryId(request.name, request.categoryId)) {
            throw DuplicateUserException("Subcategory with name '${request.name}' already exists in this category")
        }

        val subcategory = Subcategory(
            name = request.name,
            description = request.description,
            prompt = request.prompt,
            category = category
        )

        val savedSubcategory = subcategoryRepository.save(subcategory)
        logger.info("Subcategory created successfully with ID: ${savedSubcategory.id}")

        return toSubcategoryResponse(savedSubcategory)
    }

    fun getSubcategory(id: Long): SubcategoryResponse {
        logger.info("Fetching subcategory with ID: $id")

        val subcategory = subcategoryRepository.findById(id)
            .orElseThrow {
                logger.error("Subcategory not found with ID: $id")
                SubcategoryNotFoundException("Subcategory not found with ID: $id")
            }

        return toSubcategoryResponse(subcategory)
    }

    fun getAllSubcategories(categoryId: Long?): SubcategoriesResponse {
        logger.info("Fetching all subcategories - categoryId: $categoryId")

        val subcategories = if (categoryId != null) {
            subcategoryRepository.findByCategoryIdOrderByNameAsc(categoryId)
        } else {
            subcategoryRepository.findAll()
        }

        logger.info("Retrieved ${subcategories.size} subcategories")

        return SubcategoriesResponse(
            subcategories = subcategories.map { toSubcategoryResponse(it) },
            total = subcategories.size
        )
    }

    @Transactional
    fun updateSubcategory(id: Long, request: UpdateSubcategoryRequest): SubcategoryResponse {
        logger.info("Updating subcategory with ID: $id")

        val subcategory = subcategoryRepository.findById(id)
            .orElseThrow {
                logger.error("Subcategory not found with ID: $id")
                SubcategoryNotFoundException("Subcategory not found with ID: $id")
            }

        request.name?.let {
            if (subcategoryRepository.existsByNameAndCategoryId(it, subcategory.category.id) && it != subcategory.name) {
                throw DuplicateUserException("Subcategory with name '$it' already exists in this category")
            }
            subcategory.name = it
        }
        request.description?.let { subcategory.description = it }
        request.prompt?.let { subcategory.prompt = it }
        request.categoryId?.let { categoryId ->
            val category = categoryRepository.findById(categoryId)
                .orElseThrow {
                    logger.error("Category not found with ID: $categoryId")
                    CategoryNotFoundException("Category not found with ID: $categoryId")
                }
            subcategory.category = category
        }

        val updatedSubcategory = subcategoryRepository.save(subcategory)
        logger.info("Subcategory updated successfully with ID: ${updatedSubcategory.id}")

        return toSubcategoryResponse(updatedSubcategory)
    }

    @Transactional
    fun deleteSubcategory(id: Long): DeleteResponse {
        logger.info("Deleting subcategory with ID: $id")

        if (!subcategoryRepository.existsById(id)) {
            logger.error("Subcategory not found with ID: $id")
            throw SubcategoryNotFoundException("Subcategory not found with ID: $id")
        }

        subcategoryRepository.deleteById(id)
        logger.info("Subcategory deleted successfully with ID: $id")

        return DeleteResponse(
            success = true,
            message = "Subcategory deleted successfully"
        )
    }

    private fun toSubcategoryResponse(subcategory: Subcategory): SubcategoryResponse {
        return SubcategoryResponse(
            id = subcategory.id,
            name = subcategory.name,
            description = subcategory.description,
            categoryId = subcategory.category.id,
            categoryName = subcategory.category.name,
            createdAt = subcategory.createdAt,
            updatedAt = subcategory.updatedAt
        )
    }
}
