package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "subcategories")
data class Subcategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(length = 500)
    var description: String? = null,

    @Column(columnDefinition = "TEXT")
    var prompt: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @OneToMany(mappedBy = "subcategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val skills: MutableList<Skill> = mutableListOf(),

    @OneToMany(mappedBy = "subcategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sources: MutableList<SubcategorySource> = mutableListOf(),

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
