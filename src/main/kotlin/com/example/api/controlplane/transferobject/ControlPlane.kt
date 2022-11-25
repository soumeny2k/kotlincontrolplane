package com.example.api.controlplane.transferobject

sealed class ControlPlane {

    data class ApiCreate @JvmOverloads constructor(
        val name: String,
        val teamId: Int,
        var protocol: String = "https",
        var type: String? = "REST",
        var retries: Int? = null,
        var rateLimit: Int? = null,
        var connectionTimeout: Long? = null,
        var backends: List<Backend> = emptyList(),
    )

    data class Api @JvmOverloads constructor(
        val name: String,
        var protocol: String,
        var type: String,
        var retries: Int? = null,
        var rateLimit: Int? = null,
        var connectionTimeout: Long? = null,
        var balance: String? = null,
        var backends: List<Backend> = emptyList()
    )

    data class Route @JvmOverloads constructor(
        val name: String,
        var path: String? = null,
        var pathPattern: Int? = null,
        var method: String? = null,
        var retries: Int? = null,
        var rateLimit: Int? = null,
        var connectionTimeout: Long? = null,
        var cacheEnabled: Boolean? = null,
        var headers: List<RouteHeader> = emptyList(),
    )

    data class Backend(
        val url: String,
        val weight: Int
    )

    data class RouteHeader(
        val name: String,
        val value: String
    )
}
