package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "microskills")
data class Microskill(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(length = 500)
    var description: String? = null,

    @Column(columnDefinition = "TEXT")
    var prompt: String? = null,

    @Column(nullable = false)
    var level: Int = 1,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    var skill: Skill,

    @OneToMany(mappedBy = "microskill", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sources: MutableList<MicroskillSource> = mutableListOf(),

    @OneToMany(mappedBy = "microskill", cascade = [CascadeType.ALL], orphanRemoval = true)
    val resources: MutableList<Resource> = mutableListOf(),

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
