package com.skilltree.skilltree.research.providers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.skilltree.skilltree.entity.Source
import com.skilltree.skilltree.entity.SourceType
import com.skilltree.skilltree.exception.ResearchSourceException
import com.skilltree.skilltree.research.ResearchSource
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class WikipediaResearchSource(
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
) : ResearchSource {

    override val sourceType: SourceType = SourceType.WIKIPEDIA

    private val wikipediaApiUrl = "https://en.wikipedia.org/w/api.php"

    override suspend fun search(query: String, maxResults: Int): List<Source> {
        return try {
            val response: String = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .scheme("https")
                        .host("en.wikipedia.org")
                        .path("/w/api.php")
                        .queryParam("action", "opensearch")
                        .queryParam("search", query)
                        .queryParam("limit", maxResults)
                        .queryParam("format", "json")
                        .build()
                }
                .retrieve()
                .awaitBody()

            parseWikipediaSearchResponse(response)
        } catch (e: Exception) {
            throw ResearchSourceException("Failed to search Wikipedia: ${e.message}", e)
        }
    }

    override suspend fun fetchDetails(url: String): Source? {
        return try {
            val title = extractPageTitle(url) ?: return null

            val response: String = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .scheme("https")
                        .host("en.wikipedia.org")
                        .path("/w/api.php")
                        .queryParam("action", "query")
                        .queryParam("prop", "extracts|info")
                        .queryParam("exintro", "true")
                        .queryParam("explaintext", "true")
                        .queryParam("titles", title)
                        .queryParam("inprop", "url")
                        .queryParam("format", "json")
                        .build()
                }
                .retrieve()
                .awaitBody()

            parseWikipediaDetailsResponse(response, url)
        } catch (e: Exception) {
            throw ResearchSourceException("Failed to fetch Wikipedia details: ${e.message}", e)
        }
    }

    override fun canHandle(url: String): Boolean {
        return url.contains("wikipedia.org")
    }

    private fun extractPageTitle(url: String): String? {
        val regex = """wikipedia\.org/wiki/([^#?]+)""".toRegex()
        return regex.find(url)?.groupValues?.get(1)?.replace("_", " ")
    }

    private fun parseWikipediaSearchResponse(jsonResponse: String): List<Source> {
        val sources = mutableListOf<Source>()
        val root: JsonNode = objectMapper.readTree(jsonResponse)

        // OpenSearch API returns: [query, [titles], [descriptions], [urls]]
        if (root.isArray && root.size() >= 4) {
            val titles = root[1]
            val descriptions = root[2]
            val urls = root[3]

            for (i in 0 until titles.size()) {
                val title = titles[i].asText()
                val description = if (i < descriptions.size()) descriptions[i].asText() else ""
                val url = if (i < urls.size()) urls[i].asText() else ""

                if (title.isNotBlank() && url.isNotBlank()) {
                    sources.add(
                        Source(
                            title = title,
                            url = url,
                            sourceType = SourceType.WIKIPEDIA,
                            summary = description,
                            excerpt = description.take(500)
                        )
                    )
                }
            }
        }

        return sources
    }

    private fun parseWikipediaDetailsResponse(jsonResponse: String, url: String): Source? {
        val root: JsonNode = objectMapper.readTree(jsonResponse)
        val pages = root.path("query").path("pages")

        pages.fields().forEach { (_, page) ->
            val title = page.path("title").asText()
            val extract = page.path("extract").asText()

            if (title.isNotBlank()) {
                return Source(
                    title = title,
                    url = url,
                    sourceType = SourceType.WIKIPEDIA,
                    summary = extract.take(1000),
                    excerpt = extract.take(500)
                )
            }
        }

        return null
    }
}
