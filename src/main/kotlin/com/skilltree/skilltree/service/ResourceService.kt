package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.CreateResourceRequest
import com.skilltree.skilltree.dto.ResourceResponse
import com.skilltree.skilltree.dto.UpdateResourceOrderRequest
import com.skilltree.skilltree.dto.UpdateResourceRequest
import com.skilltree.skilltree.entity.Resource
import com.skilltree.skilltree.entity.ResourceType
import com.skilltree.skilltree.exception.InvalidHierarchyException
import com.skilltree.skilltree.repository.MicroskillRepository
import com.skilltree.skilltree.repository.ResourceRepository
import com.skilltree.skilltree.repository.SkillRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Service
class ResourceService(
    private val resourceRepository: ResourceRepository,
    private val skillRepository: SkillRepository,
    private val microskillRepository: MicroskillRepository,
    private val fileStorageService: FileStorageService
) {

    private val logger = LoggerFactory.getLogger(ResourceService::class.java)

    @Transactional
    fun createResourceForSkill(
        skillId: Long,
        request: CreateResourceRequest,
        file: MultipartFile?
    ): ResourceResponse {
        logger.info("Creating resource for skill ID: $skillId")

        val skill = skillRepository.findById(skillId)
            .orElseThrow { InvalidHierarchyException("Skill not found with ID: $skillId") }

        val resource = Resource(
            title = request.title,
            resourceType = request.resourceType,
            content = request.content,
            url = request.url,
            displayOrder = request.displayOrder,
            metadata = request.metadata,
            skill = skill
        )

        // Handle file upload if present
        file?.let {
            val fileMetadata = fileStorageService.storeFile(it, request.resourceType)
            resource.filePath = fileMetadata.filePath
            resource.fileName = fileMetadata.fileName
            resource.fileSize = fileMetadata.fileSize
            resource.mimeType = fileMetadata.mimeType
        }

        val saved = resourceRepository.save(resource)
        logger.info("Resource created successfully with ID: ${saved.id}")

        return toResourceResponse(saved)
    }

    @Transactional
    fun createResourceForMicroskill(
        microskillId: Long,
        request: CreateResourceRequest,
        file: MultipartFile?
    ): ResourceResponse {
        logger.info("Creating resource for microskill ID: $microskillId")

        val microskill = microskillRepository.findById(microskillId)
            .orElseThrow { InvalidHierarchyException("Microskill not found with ID: $microskillId") }

        val resource = Resource(
            title = request.title,
            resourceType = request.resourceType,
            content = request.content,
            url = request.url,
            displayOrder = request.displayOrder,
            metadata = request.metadata,
            microskill = microskill
        )

        // Handle file upload if present
        file?.let {
            val fileMetadata = fileStorageService.storeFile(it, request.resourceType)
            resource.filePath = fileMetadata.filePath
            resource.fileName = fileMetadata.fileName
            resource.fileSize = fileMetadata.fileSize
            resource.mimeType = fileMetadata.mimeType
        }

        val saved = resourceRepository.save(resource)
        logger.info("Resource created successfully with ID: ${saved.id}")

        return toResourceResponse(saved)
    }

    @Transactional(readOnly = true)
    fun getResourcesForSkill(skillId: Long): List<ResourceResponse> {
        return resourceRepository.findBySkillIdOrderByDisplayOrderAsc(skillId)
            .map { toResourceResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getResourcesForMicroskill(microskillId: Long): List<ResourceResponse> {
        return resourceRepository.findByMicroskillIdOrderByDisplayOrderAsc(microskillId)
            .map { toResourceResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getResourceById(id: Long): ResourceResponse {
        val resource = resourceRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Resource not found with ID: $id") }

        return toResourceResponse(resource)
    }

    @Transactional
    fun updateResource(
        id: Long,
        request: UpdateResourceRequest,
        file: MultipartFile?
    ): ResourceResponse {
        logger.info("Updating resource with ID: $id")

        val resource = resourceRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Resource not found with ID: $id") }

        request.title?.let { resource.title = it }
        request.content?.let { resource.content = it }
        request.url?.let { resource.url = it }
        request.displayOrder?.let { resource.displayOrder = it }
        request.metadata?.let { resource.metadata = it }

        // Handle file upload if present
        file?.let {
            // Delete old file if exists
            resource.filePath?.let { oldPath ->
                fileStorageService.deleteFile(oldPath)
            }

            // Store new file
            val fileMetadata = fileStorageService.storeFile(it, resource.resourceType)
            resource.filePath = fileMetadata.filePath
            resource.fileName = fileMetadata.fileName
            resource.fileSize = fileMetadata.fileSize
            resource.mimeType = fileMetadata.mimeType
        }

        val updated = resourceRepository.save(resource)
        logger.info("Resource updated successfully with ID: ${updated.id}")

        return toResourceResponse(updated)
    }

    @Transactional
    fun updateResourceOrder(id: Long, request: UpdateResourceOrderRequest): ResourceResponse {
        logger.info("Updating resource order for ID: $id")

        val resource = resourceRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Resource not found with ID: $id") }

        resource.displayOrder = request.displayOrder

        val updated = resourceRepository.save(resource)
        logger.info("Resource order updated successfully")

        return toResourceResponse(updated)
    }

    @Transactional
    fun deleteResource(id: Long): MessageResponse {
        val resource = resourceRepository.findById(id)
            .orElseThrow { InvalidHierarchyException("Resource not found with ID: $id") }

        // Delete associated file if exists
        resource.filePath?.let { filePath ->
            fileStorageService.deleteFile(filePath)
        }

        resourceRepository.delete(resource)
        logger.info("Resource deleted with ID: $id")

        return MessageResponse(message = "Resource deleted successfully")
    }

    fun loadFile(resourceId: Long): org.springframework.core.io.Resource {
        val resource = resourceRepository.findById(resourceId)
            .orElseThrow { InvalidHierarchyException("Resource not found with ID: $resourceId") }

        val filePath = resource.filePath
            ?: throw InvalidHierarchyException("Resource has no associated file")

        return fileStorageService.loadFileAsResource(filePath)
    }

    private fun toResourceResponse(resource: Resource): ResourceResponse {
        val downloadUrl = resource.filePath?.let {
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/resources/")
                .path(resource.id.toString())
                .path("/download")
                .toUriString()
        }

        return ResourceResponse(
            id = resource.id,
            title = resource.title,
            resourceType = resource.resourceType,
            content = resource.content,
            url = resource.url,
            filePath = resource.filePath,
            fileName = resource.fileName,
            fileSize = resource.fileSize,
            mimeType = resource.mimeType,
            displayOrder = resource.displayOrder,
            metadata = resource.metadata,
            downloadUrl = downloadUrl,
            createdAt = resource.createdAt
        )
    }

    data class MessageResponse(
        val message: String
    )
}
