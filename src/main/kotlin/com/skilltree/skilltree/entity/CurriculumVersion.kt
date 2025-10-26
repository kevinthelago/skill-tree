package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "curriculum_versions")
data class CurriculumVersion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    val curriculum: Curriculum,

    @Column(nullable = false)
    val version: Int,

    @Column(columnDefinition = "TEXT")
    val snapshotData: String,

    @Column(length = 500)
    var changeDescription: String? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
