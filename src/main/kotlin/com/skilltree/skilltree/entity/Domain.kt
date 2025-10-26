package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "domains")
data class Domain(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 100)
    var name: String,

    @Column(length = 500)
    var description: String? = null,

    @Column(columnDefinition = "TEXT")
    var prompt: String? = null,

    @OneToMany(mappedBy = "domain", cascade = [CascadeType.ALL], orphanRemoval = true)
    val categories: MutableList<Category> = mutableListOf(),

    @OneToMany(mappedBy = "domain", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sources: MutableList<DomainSource> = mutableListOf(),

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
