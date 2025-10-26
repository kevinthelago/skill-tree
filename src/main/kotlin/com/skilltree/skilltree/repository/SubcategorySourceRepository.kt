package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.SubcategorySource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubcategorySourceRepository : JpaRepository<SubcategorySource, Long> {
    fun findBySubcategoryId(subcategoryId: Long): List<SubcategorySource>
    fun findBySourceId(sourceId: Long): List<SubcategorySource>
}
