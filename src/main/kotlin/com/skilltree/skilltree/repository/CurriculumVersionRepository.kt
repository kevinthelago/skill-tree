package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.CurriculumVersion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CurriculumVersionRepository : JpaRepository<CurriculumVersion, Long> {
    fun findByCurriculumId(curriculumId: Long): List<CurriculumVersion>
    fun findByCurriculumIdAndVersion(curriculumId: Long, version: Int): CurriculumVersion?
}
