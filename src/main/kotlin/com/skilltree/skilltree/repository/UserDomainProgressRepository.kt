package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.UserDomainProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDomainProgressRepository : JpaRepository<UserDomainProgress, Long> {
    fun findByUserIdAndDomainId(userId: Long, domainId: Long): Optional<UserDomainProgress>
    fun findAllByUserId(userId: Long): List<UserDomainProgress>
    fun existsByUserIdAndDomainId(userId: Long, domainId: Long): Boolean
}
