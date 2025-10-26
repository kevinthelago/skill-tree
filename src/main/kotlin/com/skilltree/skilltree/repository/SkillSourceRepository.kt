package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.SkillSource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SkillSourceRepository : JpaRepository<SkillSource, Long> {
    fun findBySkillId(skillId: Long): List<SkillSource>
    fun findBySourceId(sourceId: Long): List<SkillSource>
}
