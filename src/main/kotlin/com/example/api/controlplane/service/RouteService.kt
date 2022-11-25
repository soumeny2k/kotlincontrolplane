package com.example.api.controlplane.service

import com.example.api.controlplane.kafka.KafkaData
import com.example.api.controlplane.kafka.Publisher
import com.example.api.controlplane.model.Route
import com.example.api.controlplane.model.RouteHeader
import com.example.api.controlplane.repository.ApiRepository
import com.example.api.controlplane.repository.RouteHeaderRepository
import com.example.api.controlplane.repository.RouteRepository
import com.example.api.controlplane.transferobject.ControlPlane
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class RouteService(
    private val apiRepository: ApiRepository,
    private val routeRepository: RouteRepository,
    private val routeHeaderRepository: RouteHeaderRepository,
    private val publisher: Publisher
) {
    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    fun findByApiId(id: Int): Flow<ControlPlane.Route> =
        routeRepository
            .getAll(id)
            .map {
                ControlPlane.Route(
                    name = it.name,
                    path = it.path,
                    headers = routeHeaderRepository.getAll(it.id!!).map { header ->
                        ControlPlane.RouteHeader(header.name, header.value)
                    }.toList()
                )
            }

    suspend fun findById(id: Int): ControlPlane.Route {
        val route = routeRepository.findById(id)
        return if (route != null) {
            ControlPlane.Route(
                name = route.name,
                path = route.path,
                headers = routeHeaderRepository.getAll(route.id!!).map { header ->
                    ControlPlane.RouteHeader(header.name, header.value)
                }.toList()
            )
        } else throw Exception("route not found")
    }

    suspend fun create(apiId: Int, route: ControlPlane.Route) {
        val dbApi = apiRepository.findById(apiId) ?: throw Exception("api not found")
        val dbRoute = routeRepository.save(
            Route(
                name = route.name,
                apiId = apiId,
                path = route.path
            )
        )
        route.headers.forEach {
            routeHeaderRepository.save(
                RouteHeader(
                    dbRoute.id!!,
                    it.name,
                    it.value
                )
            )
        }

        val kafkaData = KafkaData(
            "ROUTE",
            mapper.writeValueAsString(findById(dbRoute.id!!))
        )

        publisher.send(dbApi.name, kafkaData)
    }

    suspend fun update(apiId: Int, routeId: Int, route: ControlPlane.Route) {
        val dbApi = apiRepository.findById(apiId) ?: throw Exception("api not found")
        val dbRoute = routeRepository.save(
            Route(
                id = routeId,
                name = route.name,
                apiId = apiId,
                path = route.path
            )
        )
        route.headers.forEach {
            routeHeaderRepository.save(
                RouteHeader(
                    dbRoute.id!!,
                    it.name,
                    it.value
                )
            )
        }

        val kafkaData = KafkaData(
            "ROUTE",
            mapper.writeValueAsString(findById(dbRoute.id!!))
        )

        publisher.send(dbApi.name, kafkaData)
    }
}