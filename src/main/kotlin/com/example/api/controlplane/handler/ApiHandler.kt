package com.example.api.controlplane.handler

import com.example.api.controlplane.service.ApiService
import com.example.api.controlplane.transferobject.ControlPlane
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class ApiHandler(
    private val apiService: ApiService
) {
    suspend fun findAll(request: ServerRequest): ServerResponse =
        ServerResponse
            .ok()
            .json()
            .bodyAndAwait(apiService.findAll())

    suspend fun find(request: ServerRequest): ServerResponse =
        apiService.findById(request.pathVariable("id").toInt())
            .let {
                ServerResponse
                    .ok()
                    .json()
                    .bodyValueAndAwait(it)
            }

    suspend fun create(request: ServerRequest): ServerResponse {
        apiService.create(
            request.bodyToMono(ControlPlane.ApiCreate::class.java).awaitSingle()
        )
        return ServerResponse.status(HttpStatus.CREATED).buildAndAwait()
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        apiService.update(
            request.pathVariable("id").toInt(),
            request.bodyToMono(ControlPlane.ApiCreate::class.java).awaitSingle()
        )
        return ServerResponse.ok().buildAndAwait()
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        apiService.delete(
            request.pathVariable("id").toInt()
        )
        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun uploadSpec(request: ServerRequest): ServerResponse {
        apiService.uploadSpec(
            request.pathVariable("id").toInt(),
            request.pathVariable("version").toInt(),
            request.bodyToMono(String::class.java).awaitSingle()
        )
        return ServerResponse.ok().buildAndAwait()
    }
}