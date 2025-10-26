package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Skill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SkillRepository : JpaRepository<Skill, Long> {
    fun findBySubcategoryId(subcategoryId: Long): List<Skill>
    fun findBySubcategoryIdOrderByLevelAsc(subcategoryId: Long): List<Skill>
    fun findAllByOrderByNameAsc(): List<Skill>
}
