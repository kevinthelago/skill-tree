package com.skilltree.skilltree.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "resources")
data class Resource(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 200)
    var title: String,

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    val resourceType: ResourceType,

    @Column(columnDefinition = "TEXT")
    var content: String,

    @Column(length = 500)
    var url: String? = null,

    @Column(length = 500)
    var filePath: String? = null,

    @Column(length = 255)
    var fileName: String? = null,

    var fileSize: Long? = null,

    @Column(length = 100)
    var mimeType: String? = null,

    @Column(nullable = false)
    var displayOrder: Int = 0,

    @Column(columnDefinition = "TEXT")
    var metadata: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    var skill: Skill? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microskill_id")
    var microskill: Microskill? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class ResourceType {
    TEXT,
    MARKDOWN,
    IMAGE,
    VIDEO,
    EQUATION,
    CODE,
    PDF,
    LINK,
    AUDIO,
    INTERACTIVE,
    FILE
}
