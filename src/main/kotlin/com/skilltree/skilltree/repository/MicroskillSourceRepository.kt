package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.MicroskillSource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MicroskillSourceRepository : JpaRepository<MicroskillSource, Long> {
    fun findByMicroskillId(microskillId: Long): List<MicroskillSource>
    fun findBySourceId(sourceId: Long): List<MicroskillSource>
}
