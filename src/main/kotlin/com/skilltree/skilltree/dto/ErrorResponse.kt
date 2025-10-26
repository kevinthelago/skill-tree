package com.skilltree.skilltree.dto

data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val details: Map<String, Any>? = null
)
