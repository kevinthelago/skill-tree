package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "sources")
data class Source(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 500)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val url: String,

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    val sourceType: SourceType,

    @Column(columnDefinition = "TEXT")
    var summary: String? = null,

    @Column(columnDefinition = "TEXT")
    var authors: String? = null,

    val publicationDate: LocalDate? = null,

    var relevanceScore: Double? = null,

    @Column(columnDefinition = "TEXT")
    var excerpt: String? = null,

    @Column(columnDefinition = "TEXT")
    var metadata: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class SourceType {
    ARXIV,
    WIKIPEDIA,
    OPEN_LIBRARY,
    WEB_SEARCH,
    YOUTUBE,
    COURSERA,
    KHAN_ACADEMY,
    MIT_OCW,
    TEXTBOOK,
    RESEARCH_PAPER,
    BLOG,
    DOCUMENTATION,
    OTHER
}
