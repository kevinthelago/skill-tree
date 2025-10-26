package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.Resource
import com.skilltree.skilltree.entity.ResourceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository : JpaRepository<Resource, Long> {
    fun findBySkillId(skillId: Long): List<Resource>
    fun findByMicroskillId(microskillId: Long): List<Resource>
    fun findBySkillIdOrderByDisplayOrderAsc(skillId: Long): List<Resource>
    fun findByMicroskillIdOrderByDisplayOrderAsc(microskillId: Long): List<Resource>
    fun findByResourceType(resourceType: ResourceType): List<Resource>
}
