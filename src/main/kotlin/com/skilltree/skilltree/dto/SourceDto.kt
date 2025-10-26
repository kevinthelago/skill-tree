package com.skilltree.skilltree.dto

import com.skilltree.skilltree.entity.Source
import com.skilltree.skilltree.entity.SourceType
import java.time.LocalDate
import java.time.LocalDateTime

data class SourceResponse(
    val id: Long,
    val title: String,
    val url: String,
    val sourceType: SourceType,
    val summary: String?,
    val authors: String?,
    val publicationDate: LocalDate?,
    val excerpt: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(source: Source): SourceResponse {
            return SourceResponse(
                id = source.id,
                title = source.title,
                url = source.url,
                sourceType = source.sourceType,
                summary = source.summary,
                authors = source.authors,
                publicationDate = source.publicationDate,
                excerpt = source.excerpt,
                createdAt = source.createdAt
            )
        }
    }
}

data class SourceWithRelevanceResponse(
    val id: Long,
    val title: String,
    val url: String,
    val sourceType: SourceType,
    val summary: String?,
    val authors: String?,
    val publicationDate: LocalDate?,
    val excerpt: String?,
    val createdAt: LocalDateTime,
    val relevanceScore: Double,
    val relevantExcerpt: String?
)
