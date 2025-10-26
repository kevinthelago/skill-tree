package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Domain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DomainRepository : JpaRepository<Domain, Long> {
    fun findByName(name: String): Optional<Domain>
    fun findAllByOrderByNameAsc(): List<Domain>
    fun existsByName(name: String): Boolean
}
