package com.example.myfoodapp.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String,
    val email: String,
    val role: String, // "buyer" or "cook"
    val phoneNumber: String,
    val address: String? = null, // Only for cooks
    val createdAt: String = ""
)

enum class UserRole(val value: String) {
    BUYER("buyer"),
    COOK("cook")
}