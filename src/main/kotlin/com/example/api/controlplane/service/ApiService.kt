package com.example.api.controlplane.service

import com.example.api.controlplane.kafka.KafkaData
import com.example.api.controlplane.kafka.Publisher
import com.example.api.controlplane.model.Api
import com.example.api.controlplane.model.Backend
import com.example.api.controlplane.model.Spec
import com.example.api.controlplane.repository.ApiRepository
import com.example.api.controlplane.repository.BackendRepository
import com.example.api.controlplane.repository.SpecRepository
import com.example.api.controlplane.transferobject.ControlPlane
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class ApiService(
    private val apiRepository: ApiRepository,
    private val specRepository: SpecRepository,
    private val backendRepository: BackendRepository,
    private val publisher: Publisher
) {
    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    fun findAll(): Flow<Api> = apiRepository.findAll()

    suspend fun findById(id: Int): ControlPlane.Api {
        val api = apiRepository.findById(id) ?: throw Exception("api not found")
        return ControlPlane.Api(
            name = api.name,
            protocol = api.protocols,
            type = api.type,
            retries = api.retries,
            rateLimit = api.rateLimit,
            connectionTimeout = api.connectionTimeout,
            backends = backendRepository.getAll(id).map { backend ->
                ControlPlane.Backend(backend.url, backend.weight)
            }.toList()
        )
    }

    suspend fun create(api: ControlPlane.ApiCreate) {
        val dbApi = apiRepository.save(
            Api(
                api.name,
                api.teamId,
                api.retries,
                api.rateLimit,
                api.connectionTimeout
            )
        )

        api.backends.forEach {
            backendRepository.save(
                Backend(
                    dbApi.id!!,
                    it.url
                )
            )
        }

        val kafkaData = KafkaData(
            "API",
            mapper.writeValueAsString(findById(dbApi.id!!))
        )
        publisher.send(dbApi.name, kafkaData)
    }

    suspend fun update(id: Int, api: ControlPlane.ApiCreate) {
        val existingApi = apiRepository.findById(id) ?: throw Exception("api not found")
        val dbApi = apiRepository.save(
            Api(
                name = api.name,
                teamId = existingApi.teamId,
                retries = api.retries ?: existingApi.retries,
                rateLimit = api.rateLimit ?: existingApi.rateLimit,
                connectionTimeout = api.connectionTimeout ?: existingApi.connectionTimeout,
                id = existingApi.id
            )
        )

        api.backends.map {
            backendRepository.save(
                Backend(
                    existingApi.id!!,
                    it.url
                )
            )
        }

        val kafkaData = KafkaData(
            "API",
            mapper.writeValueAsString(findById(dbApi.id!!))
        )
        publisher.send(dbApi.name, kafkaData)
    }

    suspend fun delete(id: Int) {
        apiRepository.deleteById(id)
    }

    suspend fun uploadSpec(id: Int, version: Int, spec: String) {
        val dbSpec = specRepository.getByVersion(id, version)
        specRepository.save(
            Spec(
                spec,
                id,
                version,
                dbSpec?.id,
            )
        )
    }
}