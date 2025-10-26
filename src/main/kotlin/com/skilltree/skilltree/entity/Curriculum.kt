package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "curricula")
data class Curriculum(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(columnDefinition = "TEXT")
    var prompt: String? = null,

    @Column(nullable = false)
    var version: Int = 1,

    @Column(nullable = false)
    var published: Boolean = false,

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL], orphanRemoval = true)
    val domains: MutableList<CurriculumDomain> = mutableListOf(),

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL], orphanRemoval = true)
    val categories: MutableList<CurriculumCategory> = mutableListOf(),

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL], orphanRemoval = true)
    val subcategories: MutableList<CurriculumSubcategory> = mutableListOf(),

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL], orphanRemoval = true)
    val skills: MutableList<CurriculumSkill> = mutableListOf(),

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL], orphanRemoval = true)
    val microskills: MutableList<CurriculumMicroskill> = mutableListOf(),

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL], orphanRemoval = true)
    val versions: MutableList<CurriculumVersion> = mutableListOf(),

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
