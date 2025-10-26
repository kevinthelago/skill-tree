package com.skilltree.skilltree.controller.domain

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.DomainService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/domains")
class DomainController(
    private val domainService: DomainService
) {
    private val logger = LoggerFactory.getLogger(DomainController::class.java)

    @PostMapping
    fun createDomain(@Valid @RequestBody request: CreateDomainRequest): ResponseEntity<DomainResponse> {
        logger.info("POST /api/domains - Creating domain: ${request.name}")
        val domain = domainService.createDomain(request)
        logger.info("POST /api/domains - Domain created with ID: ${domain.id}")
        return ResponseEntity.ok(domain)
    }

    @GetMapping("/{id}")
    fun getDomain(@PathVariable id: Long): ResponseEntity<DomainResponse> {
        logger.info("GET /api/domains/$id - Request received")
        val domain = domainService.getDomain(id)
        logger.info("GET /api/domains/$id - Completed successfully")
        return ResponseEntity.ok(domain)
    }

    @GetMapping
    fun getAllDomains(): ResponseEntity<DomainsResponse> {
        logger.info("GET /api/domains - Request received")
        val response = domainService.getAllDomains()
        logger.info("GET /api/domains - Retrieved ${response.total} domains")
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun updateDomain(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateDomainRequest
    ): ResponseEntity<DomainResponse> {
        logger.info("PUT /api/domains/$id - Updating domain")
        val domain = domainService.updateDomain(id, request)
        logger.info("PUT /api/domains/$id - Domain updated successfully")
        return ResponseEntity.ok(domain)
    }

    @DeleteMapping("/{id}")
    fun deleteDomain(@PathVariable id: Long): ResponseEntity<DeleteResponse> {
        logger.info("DELETE /api/domains/$id - Request received")
        val response = domainService.deleteDomain(id)
        logger.info("DELETE /api/domains/$id - Domain deleted successfully")
        return ResponseEntity.ok(response)
    }
}
