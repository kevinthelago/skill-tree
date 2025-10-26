package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.Category
import com.skilltree.skilltree.exception.CategoryNotFoundException
import com.skilltree.skilltree.exception.DomainNotFoundException
import com.skilltree.skilltree.exception.DuplicateUserException
import com.skilltree.skilltree.repository.CategoryRepository
import com.skilltree.skilltree.repository.DomainRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val domainRepository: DomainRepository
) {
    private val logger = LoggerFactory.getLogger(CategoryService::class.java)

    @Transactional
    fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        logger.info("Creating category: ${request.name}")

        val domain = domainRepository.findById(request.domainId)
            .orElseThrow {
                logger.error("Domain not found with ID: ${request.domainId}")
                DomainNotFoundException("Domain not found with ID: ${request.domainId}")
            }

        if (categoryRepository.existsByNameAndDomainId(request.name, request.domainId)) {
            throw DuplicateUserException("Category with name '${request.name}' already exists in this domain")
        }

        val category = Category(
            name = request.name,
            description = request.description,
            prompt = request.prompt,
            domain = domain
        )

        val savedCategory = categoryRepository.save(category)
        logger.info("Category created successfully with ID: ${savedCategory.id}")

        return toCategoryResponse(savedCategory)
    }

    fun getCategory(id: Long): CategoryResponse {
        logger.info("Fetching category with ID: $id")

        val category = categoryRepository.findById(id)
            .orElseThrow {
                logger.error("Category not found with ID: $id")
                CategoryNotFoundException("Category not found with ID: $id")
            }

        return toCategoryResponse(category)
    }

    fun getAllCategories(domainId: Long?): CategoriesResponse {
        logger.info("Fetching all categories - domainId: $domainId")

        val categories = if (domainId != null) {
            categoryRepository.findByDomainIdOrderByNameAsc(domainId)
        } else {
            categoryRepository.findAll()
        }

        logger.info("Retrieved ${categories.size} categories")

        return CategoriesResponse(
            categories = categories.map { toCategoryResponse(it) },
            total = categories.size
        )
    }

    @Transactional
    fun updateCategory(id: Long, request: UpdateCategoryRequest): CategoryResponse {
        logger.info("Updating category with ID: $id")

        val category = categoryRepository.findById(id)
            .orElseThrow {
                logger.error("Category not found with ID: $id")
                CategoryNotFoundException("Category not found with ID: $id")
            }

        request.name?.let {
            if (categoryRepository.existsByNameAndDomainId(it, category.domain.id) && it != category.name) {
                throw DuplicateUserException("Category with name '$it' already exists in this domain")
            }
            category.name = it
        }
        request.description?.let { category.description = it }
        request.prompt?.let { category.prompt = it }
        request.domainId?.let { domainId ->
            val domain = domainRepository.findById(domainId)
                .orElseThrow {
                    logger.error("Domain not found with ID: $domainId")
                    DomainNotFoundException("Domain not found with ID: $domainId")
                }
            category.domain = domain
        }

        val updatedCategory = categoryRepository.save(category)
        logger.info("Category updated successfully with ID: ${updatedCategory.id}")

        return toCategoryResponse(updatedCategory)
    }

    @Transactional
    fun deleteCategory(id: Long): DeleteResponse {
        logger.info("Deleting category with ID: $id")

        if (!categoryRepository.existsById(id)) {
            logger.error("Category not found with ID: $id")
            throw CategoryNotFoundException("Category not found with ID: $id")
        }

        categoryRepository.deleteById(id)
        logger.info("Category deleted successfully with ID: $id")

        return DeleteResponse(
            success = true,
            message = "Category deleted successfully"
        )
    }

    private fun toCategoryResponse(category: Category): CategoryResponse {
        return CategoryResponse(
            id = category.id,
            name = category.name,
            description = category.description,
            domainId = category.domain.id,
            domainName = category.domain.name,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )
    }
}
