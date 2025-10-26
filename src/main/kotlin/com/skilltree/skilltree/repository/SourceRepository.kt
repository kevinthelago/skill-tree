package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Source
import com.skilltree.skilltree.entity.SourceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SourceRepository : JpaRepository<Source, Long> {
    fun findBySourceType(sourceType: SourceType): List<Source>
    fun findByTitleContainingIgnoreCase(title: String): List<Source>
    fun findByUrl(url: String): Optional<Source>
    fun existsByUrl(url: String): Boolean
}
