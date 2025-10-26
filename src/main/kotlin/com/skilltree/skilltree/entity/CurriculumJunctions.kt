package com.skilltree.skilltree.entity

import jakarta.persistence.*

@Entity
@Table(name = "curriculum_domains")
data class CurriculumDomain(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    var curriculum: Curriculum,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    var domain: Domain,

    @Column(nullable = false)
    var sequenceOrder: Int = 0,

    @Column(nullable = false)
    var required: Boolean = true
)

@Entity
@Table(name = "curriculum_categories")
data class CurriculumCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    var curriculum: Curriculum,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(nullable = false)
    var sequenceOrder: Int = 0,

    @Column(nullable = false)
    var required: Boolean = true
)

@Entity
@Table(name = "curriculum_subcategories")
data class CurriculumSubcategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    var curriculum: Curriculum,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    var subcategory: Subcategory,

    @Column(nullable = false)
    var sequenceOrder: Int = 0,

    @Column(nullable = false)
    var required: Boolean = true
)

@Entity
@Table(name = "curriculum_skills")
data class CurriculumSkill(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    var curriculum: Curriculum,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    var skill: Skill,

    @Column(nullable = false)
    var sequenceOrder: Int = 0,

    @Column(nullable = false)
    var required: Boolean = true
)

@Entity
@Table(name = "curriculum_microskills")
data class CurriculumMicroskill(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    var curriculum: Curriculum,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microskill_id", nullable = false)
    var microskill: Microskill,

    @Column(nullable = false)
    var sequenceOrder: Int = 0,

    @Column(nullable = false)
    var required: Boolean = true
)
