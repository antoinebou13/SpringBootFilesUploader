package com.api.demo.controller

import com.api.demo.constants.*
import com.api.demo.controller.dto.FileDTO
import com.api.demo.service.FilesService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.regex.Matcher
import java.util.regex.Pattern

@RestController
@RequestMapping("/files")
class FilesController(val filesService: FilesService){

    private val log = LoggerFactory.getLogger(FilesController::class.java)
    private val docFileExtensionPattern: Pattern = Pattern.compile(DOC_FILE_EXTENSION_REGEX)

    @PostMapping("/documents")
    fun addDocument(@RequestPart("document") document: MultipartFile?): ResponseEntity<FileDTO> {
        if (document == null) {
            throw Exception("No document received ")
        }

        val fileName = document.originalFilename ?: throw Exception("Missing document file required")
        val matcher: Matcher = docFileExtensionPattern.matcher(fileName)
        if (!matcher.matches()) {
            throw Exception("Document [$fileName] received has an unsupported extension type.")
        }

        if (document.size > MAX_DOCUMENT_FILE_SIZE) {
            throw Exception("Document [$fileName] received has a file size greater than $MAX_DOCUMENT_FILE_SIZE MB.")
        }

        val newFileModel = filesService.addDocument(
            document = document.bytes,
            fileName = document.originalFilename!!,
            extType = filesService.getExtType(document.originalFilename!!),
            contentType = document.contentType!!,
            size = document.size!!)

        return ResponseEntity(
            FileDTO(
            fileId = newFileModel.fileId,
            fileName = newFileModel.fileName),
            HttpStatus.OK)
    }

}