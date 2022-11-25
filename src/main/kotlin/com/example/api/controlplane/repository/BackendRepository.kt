package com.example.api.controlplane.repository

import com.example.api.controlplane.model.Backend
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BackendRepository : CoroutineCrudRepository<Backend, Int> {
    @Query("select * from backend where api_id = :apiId")
    fun getAll(apiId: Int): Flow<Backend>
}