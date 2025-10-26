package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.UserCategoryProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserCategoryProgressRepository : JpaRepository<UserCategoryProgress, Long> {
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long): Optional<UserCategoryProgress>
    fun findAllByUserId(userId: Long): List<UserCategoryProgress>
    fun existsByUserIdAndCategoryId(userId: Long, categoryId: Long): Boolean
}
