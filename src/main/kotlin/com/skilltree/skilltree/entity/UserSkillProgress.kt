package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_skill_progress")
data class UserSkillProgress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    val skill: Skill,

    @Column(nullable = false)
    var completed: Boolean = false,

    @Column(nullable = false)
    var progressPercentage: Double = 0.0,

    @Column(nullable = false)
    var lastAccessed: LocalDateTime = LocalDateTime.now(),

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
