package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.DomainSource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DomainSourceRepository : JpaRepository<DomainSource, Long> {
    fun findByDomainId(domainId: Long): List<DomainSource>
    fun findBySourceId(sourceId: Long): List<DomainSource>
}
