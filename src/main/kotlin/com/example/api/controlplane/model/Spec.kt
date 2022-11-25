package com.example.api.controlplane.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Spec @JvmOverloads constructor(
    val spec: String,
    val apiId: Int,
    val version: Int,
    @Id
    val id: Int? = null,
    val lifeCycleStatus: String = "BUILD",
)
