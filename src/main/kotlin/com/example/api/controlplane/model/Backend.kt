package com.example.api.controlplane.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Backend @JvmOverloads constructor(
    val apiId: Int,
    val url: String,
    @Id
    var id: Int? = null,
    var weight: Int = 100
)
