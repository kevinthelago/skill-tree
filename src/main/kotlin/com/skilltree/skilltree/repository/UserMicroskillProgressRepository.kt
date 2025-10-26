package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.UserMicroskillProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserMicroskillProgressRepository : JpaRepository<UserMicroskillProgress, Long> {
    fun findByUserIdAndMicroskillId(userId: Long, microskillId: Long): Optional<UserMicroskillProgress>
    fun findAllByUserId(userId: Long): List<UserMicroskillProgress>
    fun existsByUserIdAndMicroskillId(userId: Long, microskillId: Long): Boolean
}
