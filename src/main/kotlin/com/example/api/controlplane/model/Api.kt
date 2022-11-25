package com.example.api.controlplane.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Api @JvmOverloads constructor(
    val name: String,
    val teamId: Int,
    var retries: Int? = null,
    var rateLimit: Int? = null,
    var connectionTimeout: Long? = null,
    @Id
    val id: Int? = null,
    val protocols: String = "http",
    val type: String = "REST"
)