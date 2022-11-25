package com.example.api.controlplane.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Route @JvmOverloads constructor(
    val name: String,
    val apiId: Int,
    @Id
    val id: Int? = null,
    var protocol: String = "https",
    var path: String? = null,
    var pathPattern: String? = null,
    var method: String = "GET",
    var retries: Int? = null,
    var rateLimit: Int? = null,
    var connectionTimeout: Long? = null,
    var cacheEnabled: Boolean = false
)
