package com.api.project.repository;

interface FileRepository {

    fun getDocuments(ids: Collection<String>): Map<String, ByteArray>
    fun getDocument(id: String): ByteArray?
    fun putDocument(documentId: String, document: ByteArray)
}