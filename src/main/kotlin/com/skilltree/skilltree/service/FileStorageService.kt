package com.skilltree.skilltree.service

import com.skilltree.skilltree.entity.ResourceType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class FileStorageService(
    @Value("\${file.upload.dir:uploads/resources}")
    private val uploadDir: String
) {

    private val logger = LoggerFactory.getLogger(FileStorageService::class.java)
    private val fileStorageLocation: Path = Paths.get(uploadDir).toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(fileStorageLocation)
            logger.info("File storage location initialized at: $fileStorageLocation")
        } catch (ex: Exception) {
            throw RuntimeException("Could not create the directory where uploaded files will be stored.", ex)
        }
    }

    fun storeFile(file: MultipartFile, resourceType: ResourceType): FileMetadata {
        val originalFileName = file.originalFilename ?: throw IllegalArgumentException("File must have a name")

        // Generate unique filename
        val fileExtension = originalFileName.substringAfterLast('.', "")
        val uniqueFileName = "${UUID.randomUUID()}_${System.currentTimeMillis()}.$fileExtension"

        try {
            // Check if the filename contains invalid characters
            if (uniqueFileName.contains("..")) {
                throw IllegalArgumentException("Filename contains invalid path sequence: $uniqueFileName")
            }

            val targetLocation = fileStorageLocation.resolve(uniqueFileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

            logger.info("File stored successfully: $uniqueFileName (original: $originalFileName)")

            return FileMetadata(
                filePath = uniqueFileName,
                fileName = originalFileName,
                fileSize = file.size,
                mimeType = file.contentType ?: "application/octet-stream"
            )
        } catch (ex: IOException) {
            logger.error("Failed to store file: $originalFileName", ex)
            throw RuntimeException("Failed to store file: $originalFileName", ex)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource = UrlResource(filePath.toUri())

            if (resource.exists() && resource.isReadable) {
                return resource
            } else {
                throw RuntimeException("File not found: $fileName")
            }
        } catch (ex: Exception) {
            logger.error("File not found: $fileName", ex)
            throw RuntimeException("File not found: $fileName", ex)
        }
    }

    fun deleteFile(fileName: String): Boolean {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            Files.deleteIfExists(filePath)
            logger.info("File deleted successfully: $fileName")
            true
        } catch (ex: Exception) {
            logger.error("Failed to delete file: $fileName", ex)
            false
        }
    }

    fun getFileSize(fileName: String): Long {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            Files.size(filePath)
        } catch (ex: Exception) {
            0L
        }
    }

    data class FileMetadata(
        val filePath: String,
        val fileName: String,
        val fileSize: Long,
        val mimeType: String
    )
}
