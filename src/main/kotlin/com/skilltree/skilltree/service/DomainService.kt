package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.entity.Domain
import com.skilltree.skilltree.exception.DomainNotFoundException
import com.skilltree.skilltree.exception.DuplicateUserException
import com.skilltree.skilltree.repository.DomainRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DomainService(
    private val domainRepository: DomainRepository
) {
    private val logger = LoggerFactory.getLogger(DomainService::class.java)

    @Transactional
    fun createDomain(request: CreateDomainRequest): DomainResponse {
        logger.info("Creating domain: ${request.name}")

        if (domainRepository.existsByName(request.name)) {
            throw DuplicateUserException("Domain with name '${request.name}' already exists")
        }

        val domain = Domain(
            name = request.name,
            description = request.description,
            prompt = request.prompt
        )

        val savedDomain = domainRepository.save(domain)
        logger.info("Domain created successfully with ID: ${savedDomain.id}")

        return toDomainResponse(savedDomain)
    }

    fun getDomain(id: Long): DomainResponse {
        logger.info("Fetching domain with ID: $id")

        val domain = domainRepository.findById(id)
            .orElseThrow {
                logger.error("Domain not found with ID: $id")
                DomainNotFoundException("Domain not found with ID: $id")
            }

        return toDomainResponse(domain)
    }

    fun getAllDomains(): DomainsResponse {
        logger.info("Fetching all domains")

        val domains = domainRepository.findAllByOrderByNameAsc()
        logger.info("Retrieved ${domains.size} domains")

        return DomainsResponse(
            domains = domains.map { toDomainResponse(it) },
            total = domains.size
        )
    }

    @Transactional
    fun updateDomain(id: Long, request: UpdateDomainRequest): DomainResponse {
        logger.info("Updating domain with ID: $id")

        val domain = domainRepository.findById(id)
            .orElseThrow {
                logger.error("Domain not found with ID: $id")
                DomainNotFoundException("Domain not found with ID: $id")
            }

        request.name?.let {
            if (domainRepository.existsByName(it) && it != domain.name) {
                throw DuplicateUserException("Domain with name '$it' already exists")
            }
            domain.name = it
        }
        request.description?.let { domain.description = it }
        request.prompt?.let { domain.prompt = it }

        val updatedDomain = domainRepository.save(domain)
        logger.info("Domain updated successfully with ID: ${updatedDomain.id}")

        return toDomainResponse(updatedDomain)
    }

    @Transactional
    fun deleteDomain(id: Long): DeleteResponse {
        logger.info("Deleting domain with ID: $id")

        if (!domainRepository.existsById(id)) {
            logger.error("Domain not found with ID: $id")
            throw DomainNotFoundException("Domain not found with ID: $id")
        }

        domainRepository.deleteById(id)
        logger.info("Domain deleted successfully with ID: $id")

        return DeleteResponse(
            success = true,
            message = "Domain deleted successfully"
        )
    }

    private fun toDomainResponse(domain: Domain): DomainResponse {
        return DomainResponse(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
}
