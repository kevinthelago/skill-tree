package com.skilltree.skilltree.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.*
import com.skilltree.skilltree.exception.InvalidHierarchyException
import com.skilltree.skilltree.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CurriculumService(
    private val curriculumRepository: CurriculumRepository,
    private val curriculumVersionRepository: CurriculumVersionRepository,
    private val curriculumDomainRepository: CurriculumDomainRepository,
    private val curriculumCategoryRepository: CurriculumCategoryRepository,
    private val curriculumSubcategoryRepository: CurriculumSubcategoryRepository,
    private val curriculumSkillRepository: CurriculumSkillRepository,
    private val curriculumMicroskillRepository: CurriculumMicroskillRepository,
    private val domainRepository: DomainRepository,
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository,
    private val skillRepository: SkillRepository,
    private val microskillRepository: MicroskillRepository,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(CurriculumService::class.java)

    @Transactional
    fun createCurriculum(request: CreateCurriculumRequest): CurriculumSummaryResponse {
        logger.info("Creating curriculum: ${request.name}")

        if (curriculumRepository.existsByName(request.name)) {
            throw InvalidHierarchyException("Curriculum with name '${request.name}' already exists")
        }

        val curriculum = Curriculum(
            name = request.name,
            description = request.description,
            prompt = request.prompt
        )

        val saved = curriculumRepository.save(curriculum)
        logger.info("Curriculum created successfully with ID: ${saved.id}")

        return toCurriculumSummaryResponse(saved)
    }

    @Transactional(readOnly = true)
    fun getCurriculumById(id: Long): CurriculumResponse {
        val curriculum = curriculumRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $id") }

        return toCurriculumResponse(curriculum)
    }

    @Transactional(readOnly = true)
    fun getAllCurricula(): List<CurriculumSummaryResponse> {
        return curriculumRepository.findAll().map { toCurriculumSummaryResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getPublishedCurricula(): List<CurriculumSummaryResponse> {
        return curriculumRepository.findByPublished(true).map { toCurriculumSummaryResponse(it) }
    }

    @Transactional
    fun updateCurriculum(id: Long, request: UpdateCurriculumRequest): CurriculumSummaryResponse {
        logger.info("Updating curriculum with ID: $id")

        val curriculum = curriculumRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $id") }

        request.name?.let {
            if (it != curriculum.name && curriculumRepository.existsByName(it)) {
                throw InvalidHierarchyException("Curriculum with name '$it' already exists")
            }
            curriculum.name = it
        }
        request.description?.let { curriculum.description = it }
        request.prompt?.let { curriculum.prompt = it }
        request.published?.let { curriculum.published = it }

        val updated = curriculumRepository.save(curriculum)
        logger.info("Curriculum updated successfully with ID: ${updated.id}")

        return toCurriculumSummaryResponse(updated)
    }

    @Transactional
    fun publishCurriculum(id: Long, request: PublishCurriculumRequest): CurriculumSummaryResponse {
        logger.info("Publishing curriculum with ID: $id")

        val curriculum = curriculumRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $id") }

        // Create version snapshot
        createVersionSnapshot(curriculum, request.changeDescription)

        // Increment version and publish
        curriculum.version += 1
        curriculum.published = true

        val updated = curriculumRepository.save(curriculum)
        logger.info("Curriculum published as version ${updated.version}")

        return toCurriculumSummaryResponse(updated)
    }

    @Transactional
    fun deleteCurriculum(id: Long): MessageResponse {
        if (!curriculumRepository.existsById(id)) {
            throw InvalidHierarchyException("Curriculum not found with ID: $id")
        }

        curriculumRepository.deleteById(id)
        logger.info("Curriculum deleted with ID: $id")

        return MessageResponse(message = "Curriculum deleted successfully")
    }

    // Add items to curriculum

    @Transactional
    fun addDomainToCurriculum(curriculumId: Long, domainId: Long, request: AddItemToCurriculumRequest): MessageResponse {
        val curriculum = curriculumRepository.findById(curriculumId)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $curriculumId") }

        val domain = domainRepository.findById(domainId)
            .orElseThrow { InvalidHierarchyException("Domain not found with ID: $domainId") }

        val curriculumDomain = CurriculumDomain(
            curriculum = curriculum,
            domain = domain,
            sequenceOrder = request.sequenceOrder,
            required = request.required
        )

        curriculumDomainRepository.save(curriculumDomain)
        logger.info("Domain $domainId added to curriculum $curriculumId")

        return MessageResponse(message = "Domain added to curriculum successfully")
    }

    @Transactional
    fun addCategoryToCurriculum(curriculumId: Long, categoryId: Long, request: AddItemToCurriculumRequest): MessageResponse {
        val curriculum = curriculumRepository.findById(curriculumId)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $curriculumId") }

        val category = categoryRepository.findById(categoryId)
            .orElseThrow { InvalidHierarchyException("Category not found with ID: $categoryId") }

        val curriculumCategory = CurriculumCategory(
            curriculum = curriculum,
            category = category,
            sequenceOrder = request.sequenceOrder,
            required = request.required
        )

        curriculumCategoryRepository.save(curriculumCategory)
        logger.info("Category $categoryId added to curriculum $curriculumId")

        return MessageResponse(message = "Category added to curriculum successfully")
    }

    @Transactional
    fun addSubcategoryToCurriculum(curriculumId: Long, subcategoryId: Long, request: AddItemToCurriculumRequest): MessageResponse {
        val curriculum = curriculumRepository.findById(curriculumId)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $curriculumId") }

        val subcategory = subcategoryRepository.findById(subcategoryId)
            .orElseThrow { InvalidHierarchyException("Subcategory not found with ID: $subcategoryId") }

        val curriculumSubcategory = CurriculumSubcategory(
            curriculum = curriculum,
            subcategory = subcategory,
            sequenceOrder = request.sequenceOrder,
            required = request.required
        )

        curriculumSubcategoryRepository.save(curriculumSubcategory)
        logger.info("Subcategory $subcategoryId added to curriculum $curriculumId")

        return MessageResponse(message = "Subcategory added to curriculum successfully")
    }

    @Transactional
    fun addSkillToCurriculum(curriculumId: Long, skillId: Long, request: AddItemToCurriculumRequest): MessageResponse {
        val curriculum = curriculumRepository.findById(curriculumId)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $curriculumId") }

        val skill = skillRepository.findById(skillId)
            .orElseThrow { InvalidHierarchyException("Skill not found with ID: $skillId") }

        val curriculumSkill = CurriculumSkill(
            curriculum = curriculum,
            skill = skill,
            sequenceOrder = request.sequenceOrder,
            required = request.required
        )

        curriculumSkillRepository.save(curriculumSkill)
        logger.info("Skill $skillId added to curriculum $curriculumId")

        return MessageResponse(message = "Skill added to curriculum successfully")
    }

    @Transactional
    fun addMicroskillToCurriculum(curriculumId: Long, microskillId: Long, request: AddItemToCurriculumRequest): MessageResponse {
        val curriculum = curriculumRepository.findById(curriculumId)
            .orElseThrow { InvalidHierarchyException("Curriculum not found with ID: $curriculumId") }

        val microskill = microskillRepository.findById(microskillId)
            .orElseThrow { InvalidHierarchyException("Microskill not found with ID: $microskillId") }

        val curriculumMicroskill = CurriculumMicroskill(
            curriculum = curriculum,
            microskill = microskill,
            sequenceOrder = request.sequenceOrder,
            required = request.required
        )

        curriculumMicroskillRepository.save(curriculumMicroskill)
        logger.info("Microskill $microskillId added to curriculum $curriculumId")

        return MessageResponse(message = "Microskill added to curriculum successfully")
    }

    // Version management

    @Transactional(readOnly = true)
    fun getCurriculumVersions(curriculumId: Long): List<CurriculumVersionResponse> {
        return curriculumVersionRepository.findByCurriculumId(curriculumId).map {
            CurriculumVersionResponse(
                id = it.id,
                version = it.version,
                changeDescription = it.changeDescription,
                createdAt = it.createdAt
            )
        }
    }

    private fun createVersionSnapshot(curriculum: Curriculum, changeDescription: String?) {
        // Create a simple JSON snapshot of curriculum structure
        val snapshot = mapOf(
            "name" to curriculum.name,
            "description" to curriculum.description,
            "version" to curriculum.version,
            "totalDomains" to curriculum.domains.size,
            "totalCategories" to curriculum.categories.size,
            "totalSubcategories" to curriculum.subcategories.size,
            "totalSkills" to curriculum.skills.size,
            "totalMicroskills" to curriculum.microskills.size
        )

        val version = CurriculumVersion(
            curriculum = curriculum,
            version = curriculum.version,
            snapshotData = objectMapper.writeValueAsString(snapshot),
            changeDescription = changeDescription
        )

        curriculumVersionRepository.save(version)
    }

    private fun toCurriculumResponse(curriculum: Curriculum): CurriculumResponse {
        return CurriculumResponse(
            id = curriculum.id,
            name = curriculum.name,
            description = curriculum.description,
            version = curriculum.version,
            published = curriculum.published,
            domains = curriculum.domains.map { toDomainWithOrder(it) }.sortedBy { it.sequenceOrder },
            categories = curriculum.categories.map { toCategoryWithOrder(it) }.sortedBy { it.sequenceOrder },
            subcategories = curriculum.subcategories.map { toSubcategoryWithOrder(it) }.sortedBy { it.sequenceOrder },
            skills = curriculum.skills.map { toSkillWithOrder(it) }.sortedBy { it.sequenceOrder },
            microskills = curriculum.microskills.map { toMicroskillWithOrder(it) }.sortedBy { it.sequenceOrder },
            createdAt = curriculum.createdAt,
            updatedAt = curriculum.updatedAt
        )
    }

    private fun toCurriculumSummaryResponse(curriculum: Curriculum): CurriculumSummaryResponse {
        return CurriculumSummaryResponse(
            id = curriculum.id,
            name = curriculum.name,
            description = curriculum.description,
            version = curriculum.version,
            published = curriculum.published,
            totalDomains = curriculum.domains.size,
            totalCategories = curriculum.categories.size,
            totalSubcategories = curriculum.subcategories.size,
            totalSkills = curriculum.skills.size,
            totalMicroskills = curriculum.microskills.size,
            createdAt = curriculum.createdAt,
            updatedAt = curriculum.updatedAt
        )
    }

    private fun toDomainWithOrder(cd: CurriculumDomain): DomainWithOrder {
        return DomainWithOrder(
            id = cd.domain.id,
            name = cd.domain.name,
            description = cd.domain.description,
            sequenceOrder = cd.sequenceOrder,
            required = cd.required
        )
    }

    private fun toCategoryWithOrder(cc: CurriculumCategory): CategoryWithOrder {
        return CategoryWithOrder(
            id = cc.category.id,
            name = cc.category.name,
            description = cc.category.description,
            domainId = cc.category.domain.id,
            domainName = cc.category.domain.name,
            sequenceOrder = cc.sequenceOrder,
            required = cc.required
        )
    }

    private fun toSubcategoryWithOrder(cs: CurriculumSubcategory): SubcategoryWithOrder {
        return SubcategoryWithOrder(
            id = cs.subcategory.id,
            name = cs.subcategory.name,
            description = cs.subcategory.description,
            categoryId = cs.subcategory.category.id,
            categoryName = cs.subcategory.category.name,
            sequenceOrder = cs.sequenceOrder,
            required = cs.required
        )
    }

    private fun toSkillWithOrder(cs: CurriculumSkill): SkillWithOrder {
        return SkillWithOrder(
            id = cs.skill.id,
            name = cs.skill.name,
            description = cs.skill.description,
            level = cs.skill.level,
            subcategoryId = cs.skill.subcategory.id,
            subcategoryName = cs.skill.subcategory.name,
            sequenceOrder = cs.sequenceOrder,
            required = cs.required
        )
    }

    private fun toMicroskillWithOrder(cm: CurriculumMicroskill): MicroskillWithOrder {
        return MicroskillWithOrder(
            id = cm.microskill.id,
            name = cm.microskill.name,
            description = cm.microskill.description,
            level = cm.microskill.level,
            skillId = cm.microskill.skill.id,
            skillName = cm.microskill.skill.name,
            sequenceOrder = cm.sequenceOrder,
            required = cm.required
        )
    }
}
