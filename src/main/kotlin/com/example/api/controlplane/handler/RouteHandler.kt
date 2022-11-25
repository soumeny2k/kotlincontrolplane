package com.example.api.controlplane.handler

import com.example.api.controlplane.service.RouteService
import com.example.api.controlplane.transferobject.ControlPlane
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class RouteHandler(
    private val routeService: RouteService
) {
    suspend fun findById(request: ServerRequest): ServerResponse =
        routeService.findById(request.pathVariable("route_id").toInt())
            .let {
                ServerResponse
                    .ok()
                    .json()
                    .bodyValueAndAwait(it)
            }

    suspend fun create(request: ServerRequest): ServerResponse {
        routeService.create(
            request.pathVariable("api_id").toInt(),
            request.bodyToMono(ControlPlane.Route::class.java).awaitSingle()
        )
        return ServerResponse.status(HttpStatus.CREATED).buildAndAwait()
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        routeService.update(
            request.pathVariable("api_id").toInt(),
            request.pathVariable("route_id").toInt(),
            request.bodyToMono(ControlPlane.Route::class.java).awaitSingle()
        )
        return ServerResponse.ok().buildAndAwait()
    }
}