package com.skilltree.skilltree.research

import com.skilltree.skilltree.entity.Source
import com.skilltree.skilltree.entity.SourceType

/**
 * Interface for research source providers
 * Implementations will fetch content from various sources like Arxiv, Wikipedia, etc.
 */
interface ResearchSource {
    /**
     * The type of source this provider handles
     */
    val sourceType: SourceType

    /**
     * Search for sources based on a query
     * @param query The search query
     * @param maxResults Maximum number of results to return
     * @return List of sources found
     */
    suspend fun search(query: String, maxResults: Int = 10): List<Source>

    /**
     * Fetch detailed information about a specific source by URL
     * @param url The URL of the source
     * @return Source with detailed information
     */
    suspend fun fetchDetails(url: String): Source?

    /**
     * Check if this provider can handle a given URL
     * @param url The URL to check
     * @return true if this provider can handle the URL
     */
    fun canHandle(url: String): Boolean
}
