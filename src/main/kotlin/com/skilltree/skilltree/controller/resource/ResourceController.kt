package com.skilltree.skilltree.controller.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.skilltree.skilltree.dto.CreateResourceRequest
import com.skilltree.skilltree.dto.ResourceResponse
import com.skilltree.skilltree.dto.UpdateResourceOrderRequest
import com.skilltree.skilltree.dto.UpdateResourceRequest
import com.skilltree.skilltree.service.ResourceService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/resources")
class ResourceController(
    private val resourceService: ResourceService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(ResourceController::class.java)

    @PostMapping("/skill/{skillId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createResourceForSkill(
        @PathVariable skillId: Long,
        @RequestPart("resource") resourceJson: String,
        @RequestPart("file", required = false) file: MultipartFile?
    ): ResponseEntity<ResourceResponse> {
        val request = objectMapper.readValue(resourceJson, CreateResourceRequest::class.java)
        val response = resourceService.createResourceForSkill(skillId, request, file)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/microskill/{microskillId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createResourceForMicroskill(
        @PathVariable microskillId: Long,
        @RequestPart("resource") resourceJson: String,
        @RequestPart("file", required = false) file: MultipartFile?
    ): ResponseEntity<ResourceResponse> {
        val request = objectMapper.readValue(resourceJson, CreateResourceRequest::class.java)
        val response = resourceService.createResourceForMicroskill(microskillId, request, file)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/skill/{skillId}")
    fun getResourcesForSkill(@PathVariable skillId: Long): ResponseEntity<List<ResourceResponse>> {
        val resources = resourceService.getResourcesForSkill(skillId)
        return ResponseEntity.ok(resources)
    }

    @GetMapping("/microskill/{microskillId}")
    fun getResourcesForMicroskill(@PathVariable microskillId: Long): ResponseEntity<List<ResourceResponse>> {
        val resources = resourceService.getResourcesForMicroskill(microskillId)
        return ResponseEntity.ok(resources)
    }

    @GetMapping("/{id}")
    fun getResourceById(@PathVariable id: Long): ResponseEntity<ResourceResponse> {
        val resource = resourceService.getResourceById(id)
        return ResponseEntity.ok(resource)
    }

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateResource(
        @PathVariable id: Long,
        @RequestPart("resource") resourceJson: String,
        @RequestPart("file", required = false) file: MultipartFile?
    ): ResponseEntity<ResourceResponse> {
        val request = objectMapper.readValue(resourceJson, UpdateResourceRequest::class.java)
        val response = resourceService.updateResource(id, request, file)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}/order")
    fun updateResourceOrder(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateResourceOrderRequest
    ): ResponseEntity<ResourceResponse> {
        val response = resourceService.updateResourceOrder(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteResource(@PathVariable id: Long): ResponseEntity<ResourceService.MessageResponse> {
        val response = resourceService.deleteResource(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}/download")
    fun downloadFile(
        @PathVariable id: Long,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {
        val file = resourceService.loadFile(id)
        val resource = resourceService.getResourceById(id)

        var contentType: String? = null
        try {
            contentType = request.servletContext.getMimeType(file.file.absolutePath)
        } catch (ex: Exception) {
            logger.info("Could not determine file type.")
        }

        if (contentType == null) {
            contentType = resource.mimeType ?: "application/octet-stream"
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${resource.fileName}\"")
            .body(file)
    }
}
