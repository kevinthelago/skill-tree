package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Subcategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubcategoryRepository : JpaRepository<Subcategory, Long> {
    fun findByName(name: String): Optional<Subcategory>
    fun findByCategoryId(categoryId: Long): List<Subcategory>
    fun findByCategoryIdOrderByNameAsc(categoryId: Long): List<Subcategory>
    fun existsByNameAndCategoryId(name: String, categoryId: Long): Boolean
}
