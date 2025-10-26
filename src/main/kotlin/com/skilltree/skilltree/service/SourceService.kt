package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.SourceResponse
import com.skilltree.skilltree.dto.SourceWithRelevanceResponse
import com.skilltree.skilltree.entity.*
import com.skilltree.skilltree.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SourceService(
    private val sourceRepository: SourceRepository,
    private val domainSourceRepository: DomainSourceRepository,
    private val categorySourceRepository: CategorySourceRepository,
    private val subcategorySourceRepository: SubcategorySourceRepository,
    private val skillSourceRepository: SkillSourceRepository,
    private val microskillSourceRepository: MicroskillSourceRepository
) {

    @Transactional
    fun createSource(source: Source): Source {
        // Check if source with same URL already exists
        val existing = sourceRepository.findByUrl(source.url).orElse(null)
        if (existing != null) {
            return existing
        }
        return sourceRepository.save(source)
    }

    @Transactional
    fun linkSourceToDomain(
        source: Source,
        domain: Domain,
        relevanceScore: Double,
        relevantExcerpt: String?,
        usedForGeneration: Boolean = false
    ): DomainSource {
        val domainSource = DomainSource(
            domain = domain,
            source = source,
            relevanceScore = relevanceScore,
            relevantExcerpt = relevantExcerpt,
            usedForGeneration = usedForGeneration
        )
        return domainSourceRepository.save(domainSource)
    }

    @Transactional
    fun linkSourceToCategory(
        source: Source,
        category: Category,
        relevanceScore: Double,
        relevantExcerpt: String?,
        usedForGeneration: Boolean = false
    ): CategorySource {
        val categorySource = CategorySource(
            category = category,
            source = source,
            relevanceScore = relevanceScore,
            relevantExcerpt = relevantExcerpt,
            usedForGeneration = usedForGeneration
        )
        return categorySourceRepository.save(categorySource)
    }

    @Transactional
    fun linkSourceToSubcategory(
        source: Source,
        subcategory: Subcategory,
        relevanceScore: Double,
        relevantExcerpt: String?,
        usedForGeneration: Boolean = false
    ): SubcategorySource {
        val subcategorySource = SubcategorySource(
            subcategory = subcategory,
            source = source,
            relevanceScore = relevanceScore,
            relevantExcerpt = relevantExcerpt,
            usedForGeneration = usedForGeneration
        )
        return subcategorySourceRepository.save(subcategorySource)
    }

    @Transactional
    fun linkSourceToSkill(
        source: Source,
        skill: Skill,
        relevanceScore: Double,
        relevantExcerpt: String?,
        usedForGeneration: Boolean = false
    ): SkillSource {
        val skillSource = SkillSource(
            skill = skill,
            source = source,
            relevanceScore = relevanceScore,
            relevantExcerpt = relevantExcerpt,
            usedForGeneration = usedForGeneration
        )
        return skillSourceRepository.save(skillSource)
    }

    @Transactional
    fun linkSourceToMicroskill(
        source: Source,
        microskill: Microskill,
        relevanceScore: Double,
        relevantExcerpt: String?,
        usedForGeneration: Boolean = false
    ): MicroskillSource {
        val microskillSource = MicroskillSource(
            microskill = microskill,
            source = source,
            relevanceScore = relevanceScore,
            relevantExcerpt = relevantExcerpt,
            usedForGeneration = usedForGeneration
        )
        return microskillSourceRepository.save(microskillSource)
    }

    @Transactional(readOnly = true)
    fun getSourcesForDomain(domainId: Long): List<SourceWithRelevanceResponse> {
        val domainSources = domainSourceRepository.findByDomainId(domainId)
        return domainSources.map { domainSource ->
            val source = domainSource.source
            SourceWithRelevanceResponse(
                id = source.id,
                title = source.title,
                url = source.url,
                sourceType = source.sourceType,
                summary = source.summary,
                authors = source.authors,
                publicationDate = source.publicationDate,
                excerpt = source.excerpt,
                createdAt = source.createdAt,
                relevanceScore = domainSource.relevanceScore,
                relevantExcerpt = domainSource.relevantExcerpt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSourcesForCategory(categoryId: Long): List<SourceWithRelevanceResponse> {
        val categorySources = categorySourceRepository.findByCategoryId(categoryId)
        return categorySources.map { categorySource ->
            val source = categorySource.source
            SourceWithRelevanceResponse(
                id = source.id,
                title = source.title,
                url = source.url,
                sourceType = source.sourceType,
                summary = source.summary,
                authors = source.authors,
                publicationDate = source.publicationDate,
                excerpt = source.excerpt,
                createdAt = source.createdAt,
                relevanceScore = categorySource.relevanceScore,
                relevantExcerpt = categorySource.relevantExcerpt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSourcesForSubcategory(subcategoryId: Long): List<SourceWithRelevanceResponse> {
        val subcategorySources = subcategorySourceRepository.findBySubcategoryId(subcategoryId)
        return subcategorySources.map { subcategorySource ->
            val source = subcategorySource.source
            SourceWithRelevanceResponse(
                id = source.id,
                title = source.title,
                url = source.url,
                sourceType = source.sourceType,
                summary = source.summary,
                authors = source.authors,
                publicationDate = source.publicationDate,
                excerpt = source.excerpt,
                createdAt = source.createdAt,
                relevanceScore = subcategorySource.relevanceScore,
                relevantExcerpt = subcategorySource.relevantExcerpt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSourcesForSkill(skillId: Long): List<SourceWithRelevanceResponse> {
        val skillSources = skillSourceRepository.findBySkillId(skillId)
        return skillSources.map { skillSource ->
            val source = skillSource.source
            SourceWithRelevanceResponse(
                id = source.id,
                title = source.title,
                url = source.url,
                sourceType = source.sourceType,
                summary = source.summary,
                authors = source.authors,
                publicationDate = source.publicationDate,
                excerpt = source.excerpt,
                createdAt = source.createdAt,
                relevanceScore = skillSource.relevanceScore,
                relevantExcerpt = skillSource.relevantExcerpt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getSourcesForMicroskill(microskillId: Long): List<SourceWithRelevanceResponse> {
        val microskillSources = microskillSourceRepository.findByMicroskillId(microskillId)
        return microskillSources.map { microskillSource ->
            val source = microskillSource.source
            SourceWithRelevanceResponse(
                id = source.id,
                title = source.title,
                url = source.url,
                sourceType = source.sourceType,
                summary = source.summary,
                authors = source.authors,
                publicationDate = source.publicationDate,
                excerpt = source.excerpt,
                createdAt = source.createdAt,
                relevanceScore = microskillSource.relevanceScore,
                relevantExcerpt = microskillSource.relevantExcerpt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getAllSources(): List<SourceResponse> {
        return sourceRepository.findAll().map { SourceResponse.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun getSourceById(id: Long): SourceResponse? {
        return sourceRepository.findById(id).map { SourceResponse.fromEntity(it) }.orElse(null)
    }
}
