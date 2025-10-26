package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Microskill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MicroskillRepository : JpaRepository<Microskill, Long> {
    fun findBySkillId(skillId: Long): List<Microskill>
    fun findBySkillIdOrderByLevelAsc(skillId: Long): List<Microskill>
}
