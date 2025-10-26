package com.skilltree.skilltree.research.providers

import com.skilltree.skilltree.entity.Source
import com.skilltree.skilltree.entity.SourceType
import com.skilltree.skilltree.exception.ResearchSourceException
import com.skilltree.skilltree.research.ResearchSource
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class ArxivResearchSource(
    private val webClient: WebClient
) : ResearchSource {

    override val sourceType: SourceType = SourceType.ARXIV

    private val arxivApiUrl = "http://export.arxiv.org/api/query"

    override suspend fun search(query: String, maxResults: Int): List<Source> {
        return try {
            val response: String = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .scheme("http")
                        .host("export.arxiv.org")
                        .path("/api/query")
                        .queryParam("search_query", "all:$query")
                        .queryParam("max_results", maxResults)
                        .queryParam("sortBy", "relevance")
                        .queryParam("sortOrder", "descending")
                        .build()
                }
                .retrieve()
                .awaitBody()

            parseArxivResponse(response)
        } catch (e: Exception) {
            throw ResearchSourceException("Failed to search Arxiv: ${e.message}", e)
        }
    }

    override suspend fun fetchDetails(url: String): Source? {
        return try {
            val arxivId = extractArxivId(url) ?: return null
            val response: String = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .scheme("http")
                        .host("export.arxiv.org")
                        .path("/api/query")
                        .queryParam("id_list", arxivId)
                        .build()
                }
                .retrieve()
                .awaitBody()

            parseArxivResponse(response).firstOrNull()
        } catch (e: Exception) {
            throw ResearchSourceException("Failed to fetch Arxiv details: ${e.message}", e)
        }
    }

    override fun canHandle(url: String): Boolean {
        return url.contains("arxiv.org")
    }

    private fun extractArxivId(url: String): String? {
        val regex = """arxiv\.org/(?:abs|pdf)/(\d+\.\d+)""".toRegex()
        return regex.find(url)?.groupValues?.get(1)
    }

    private fun parseArxivResponse(xmlResponse: String): List<Source> {
        val sources = mutableListOf<Source>()

        // Simple XML parsing - extract entries
        val entryRegex = """<entry>(.*?)</entry>""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val entries = entryRegex.findAll(xmlResponse)

        for (entry in entries) {
            val entryContent = entry.groupValues[1]

            val title = extractXmlTag(entryContent, "title")?.trim()?.replace("\\s+".toRegex(), " ") ?: continue
            val summary = extractXmlTag(entryContent, "summary")?.trim()?.replace("\\s+".toRegex(), " ") ?: ""
            val id = extractXmlTag(entryContent, "id") ?: continue
            val published = extractXmlTag(entryContent, "published")

            // Extract all authors
            val authorRegex = """<name>(.*?)</name>""".toRegex()
            val authors = authorRegex.findAll(entryContent)
                .mapNotNull { it.groupValues.getOrNull(1) }
                .joinToString(", ")

            val publicationDate = published?.let {
                try {
                    LocalDate.parse(it.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE)
                } catch (e: Exception) {
                    null
                }
            }

            sources.add(
                Source(
                    title = title,
                    url = id,
                    sourceType = SourceType.ARXIV,
                    summary = summary,
                    authors = authors.ifBlank { null },
                    publicationDate = publicationDate,
                    excerpt = summary.take(500)
                )
            )
        }

        return sources
    }

    private fun extractXmlTag(content: String, tagName: String): String? {
        val regex = """<$tagName[^>]*>(.*?)</$tagName>""".toRegex(RegexOption.DOT_MATCHES_ALL)
        return regex.find(content)?.groupValues?.get(1)
    }
}
