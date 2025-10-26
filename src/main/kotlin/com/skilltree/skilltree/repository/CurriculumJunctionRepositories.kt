package com.skilltree.skilltree.repository

import com.skilltree.skilltree.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CurriculumDomainRepository : JpaRepository<CurriculumDomain, Long> {
    fun findByCurriculumId(curriculumId: Long): List<CurriculumDomain>
    fun findByDomainId(domainId: Long): List<CurriculumDomain>
    fun deleteByCurriculumIdAndDomainId(curriculumId: Long, domainId: Long)
}

@Repository
interface CurriculumCategoryRepository : JpaRepository<CurriculumCategory, Long> {
    fun findByCurriculumId(curriculumId: Long): List<CurriculumCategory>
    fun findByCategoryId(categoryId: Long): List<CurriculumCategory>
    fun deleteByCurriculumIdAndCategoryId(curriculumId: Long, categoryId: Long)
}

@Repository
interface CurriculumSubcategoryRepository : JpaRepository<CurriculumSubcategory, Long> {
    fun findByCurriculumId(curriculumId: Long): List<CurriculumSubcategory>
    fun findBySubcategoryId(subcategoryId: Long): List<CurriculumSubcategory>
    fun deleteByCurriculumIdAndSubcategoryId(curriculumId: Long, subcategoryId: Long)
}

@Repository
interface CurriculumSkillRepository : JpaRepository<CurriculumSkill, Long> {
    fun findByCurriculumId(curriculumId: Long): List<CurriculumSkill>
    fun findBySkillId(skillId: Long): List<CurriculumSkill>
    fun deleteByCurriculumIdAndSkillId(curriculumId: Long, skillId: Long)
}

@Repository
interface CurriculumMicroskillRepository : JpaRepository<CurriculumMicroskill, Long> {
    fun findByCurriculumId(curriculumId: Long): List<CurriculumMicroskill>
    fun findByMicroskillId(microskillId: Long): List<CurriculumMicroskill>
    fun deleteByCurriculumIdAndMicroskillId(curriculumId: Long, microskillId: Long)
}
