package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class AIAgentType {
    GEMINI,
    OPENAI,
    CLAUDE,
    HUGGINGFACE,
    COHERE,
    CUSTOM
}

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 50)
    val username: String,

    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, length = 20)
    val role: String = "USER",

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    var preferredAiAgent: AIAgentType? = null,

    @Column(length = 500)
    var aiApiKey: String? = null,

    @Column(length = 500)
    var thumbnailUrl: String? = null,

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
