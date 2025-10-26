package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "skills")
data class Skill(
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
    @JoinColumn(name = "subcategory_id", nullable = false)
    var subcategory: Subcategory,

    @OneToMany(mappedBy = "skill", cascade = [CascadeType.ALL], orphanRemoval = true)
    val microskills: MutableList<Microskill> = mutableListOf(),

    @OneToMany(mappedBy = "skill", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sources: MutableList<SkillSource> = mutableListOf(),

    @OneToMany(mappedBy = "skill", cascade = [CascadeType.ALL], orphanRemoval = true)
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
