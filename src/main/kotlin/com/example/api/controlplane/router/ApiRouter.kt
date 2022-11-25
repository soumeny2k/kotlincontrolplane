package com.example.api.controlplane.router

import com.example.api.controlplane.handler.ApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ApiRouter(
    private val apiHandler: ApiHandler
) {
    @Bean
    fun apiRoutes() = coRouter {
        "/api".nest {
            GET("", apiHandler::findAll)
            POST("", apiHandler::create)
            "/{id}".nest {
                GET("", apiHandler::find)
                PUT("", apiHandler::update)
                DELETE("", apiHandler::delete)

                POST("/{version}/spec", apiHandler::uploadSpec)
            }
        }
    }
}