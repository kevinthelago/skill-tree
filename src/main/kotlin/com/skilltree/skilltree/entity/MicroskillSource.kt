package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "microskill_sources")
data class MicroskillSource(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microskill_id", nullable = false)
    val microskill: Microskill,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    val source: Source,

    val relevanceScore: Double,

    @Column(columnDefinition = "TEXT")
    val relevantExcerpt: String? = null,

    val usedForGeneration: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
