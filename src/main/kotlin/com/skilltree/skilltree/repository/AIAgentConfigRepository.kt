package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.AIAgentConfig
import com.skilltree.skilltree.entity.AIAgentType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AIAgentConfigRepository : JpaRepository<AIAgentConfig, Long> {
    fun findByAgentType(agentType: AIAgentType): AIAgentConfig?
    fun findByActive(active: Boolean): List<AIAgentConfig>
    fun existsByAgentType(agentType: AIAgentType): Boolean
}
