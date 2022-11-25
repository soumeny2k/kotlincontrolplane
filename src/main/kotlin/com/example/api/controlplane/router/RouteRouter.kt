package com.example.api.controlplane.router

import com.example.api.controlplane.handler.RouteHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouteRouter(
    private val routeHandler: RouteHandler
) {
    @Bean
    fun routeRoutes() = coRouter {
        "/api/{api_id}/route".nest {
            POST("", routeHandler::create)
            "/{route_id}".nest {
                GET("", routeHandler::findById)
                PUT("", routeHandler::update)
            }
        }
    }
}