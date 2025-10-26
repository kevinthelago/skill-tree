package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.UserSkillProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSkillProgressRepository : JpaRepository<UserSkillProgress, Long> {
    fun findByUserIdAndSkillId(userId: Long, skillId: Long): Optional<UserSkillProgress>
    fun findAllByUserId(userId: Long): List<UserSkillProgress>
    fun existsByUserIdAndSkillId(userId: Long, skillId: Long): Boolean
}
