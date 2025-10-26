package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "ai_agent_configs")
data class AIAgentConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 20)
    @Enumerated(EnumType.STRING)
    val agentType: AIAgentType,

    @Column(nullable = false, length = 500)
    var apiEndpoint: String,

    @Column(nullable = false, length = 100)
    var defaultModel: String,

    @Column(columnDefinition = "TEXT")
    var systemPrompt: String? = null,

    @Column(nullable = false)
    var active: Boolean = true,

    @Column(length = 1000)
    var encryptedCredentials: String? = null,

    @Column(nullable = false)
    var maxTokens: Int = 4096,

    @Column(nullable = false)
    var temperature: Double = 0.7,

    @Column(nullable = false)
    var rateLimit: Int = 60,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
