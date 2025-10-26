package com.skilltree.skilltree.research.providers

import com.skilltree.skilltree.entity.Source
import com.skilltree.skilltree.entity.SourceType
import com.skilltree.skilltree.exception.ResearchSourceException
import com.skilltree.skilltree.research.ResearchSource
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
class WebSearchResearchSource(
    private val webClient: WebClient
) : ResearchSource {

    override val sourceType: SourceType = SourceType.WEB_SEARCH

    override suspend fun search(query: String, maxResults: Int): List<Source> {
        // NOTE: This is a placeholder implementation
        // In production, this would integrate with a real search API like Google Custom Search,
        // Bing Search API, or DuckDuckGo API

        return try {
            // For now, return empty list with a note
            // TODO: Integrate with actual search API
            emptyList()
        } catch (e: Exception) {
            throw ResearchSourceException("Failed to perform web search: ${e.message}", e)
        }
    }

    override suspend fun fetchDetails(url: String): Source? {
        return try {
            // Fetch the page content
            val response: String = webClient.get()
                .uri(url)
                .retrieve()
                .awaitBody()

            // Extract title and description from HTML
            val title = extractTitle(response, url)
            val description = extractDescription(response)

            Source(
                title = title,
                url = url,
                sourceType = SourceType.WEB_SEARCH,
                summary = description,
                excerpt = description.take(500)
            )
        } catch (e: Exception) {
            throw ResearchSourceException("Failed to fetch web page details: ${e.message}", e)
        }
    }

    override fun canHandle(url: String): Boolean {
        // Can handle any HTTP/HTTPS URL
        return url.startsWith("http://") || url.startsWith("https://")
    }

    private fun extractTitle(html: String, fallbackUrl: String): String {
        val titleRegex = """<title[^>]*>(.*?)</title>""".toRegex(RegexOption.IGNORE_CASE)
        val match = titleRegex.find(html)
        return match?.groupValues?.get(1)?.trim() ?: fallbackUrl
    }

    private fun extractDescription(html: String): String {
        // Try to extract meta description
        val metaDescRegex = """<meta[^>]*name=["']description["'][^>]*content=["']([^"']*)["']""".toRegex(RegexOption.IGNORE_CASE)
        val metaMatch = metaDescRegex.find(html)
        if (metaMatch != null) {
            return metaMatch.groupValues[1].trim()
        }

        // Try to extract first paragraph
        val paragraphRegex = """<p[^>]*>(.*?)</p>""".toRegex(RegexOption.IGNORE_CASE)
        val paragraphMatch = paragraphRegex.find(html)
        if (paragraphMatch != null) {
            val text = paragraphMatch.groupValues[1]
                .replace("<[^>]*>".toRegex(), "")
                .trim()
            if (text.length > 50) {
                return text.take(1000)
            }
        }

        return "No description available"
    }
}
