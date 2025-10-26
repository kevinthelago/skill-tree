package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.UserSubcategoryProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSubcategoryProgressRepository : JpaRepository<UserSubcategoryProgress, Long> {
    fun findByUserIdAndSubcategoryId(userId: Long, subcategoryId: Long): Optional<UserSubcategoryProgress>
    fun findAllByUserId(userId: Long): List<UserSubcategoryProgress>
    fun existsByUserIdAndSubcategoryId(userId: Long, subcategoryId: Long): Boolean
}
