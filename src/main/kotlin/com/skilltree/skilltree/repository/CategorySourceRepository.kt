package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.CategorySource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategorySourceRepository : JpaRepository<CategorySource, Long> {
    fun findByCategoryId(categoryId: Long): List<CategorySource>
    fun findBySourceId(sourceId: Long): List<CategorySource>
    fun findByCategory_Domain_Id(domainId: Long): List<CategorySource>
}
