package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Optional<Category>
    fun findByDomainId(domainId: Long): List<Category>
    fun findByDomainIdOrderByNameAsc(domainId: Long): List<Category>
    fun existsByNameAndDomainId(name: String, domainId: Long): Boolean
}
