package com.example.api.controlplane.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class RouteHeader @JvmOverloads constructor(
    val routeId: Int,
    val name: String,
    val value: String,
    @Id
    var id: Int? = null,
)
