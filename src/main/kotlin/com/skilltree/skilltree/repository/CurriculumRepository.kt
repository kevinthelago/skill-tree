package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Curriculum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CurriculumRepository : JpaRepository<Curriculum, Long> {
    fun findByName(name: String): Curriculum?
    fun existsByName(name: String): Boolean
    fun findByPublished(published: Boolean): List<Curriculum>
}
