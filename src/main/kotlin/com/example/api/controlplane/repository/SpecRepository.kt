package com.example.api.controlplane.repository

import com.example.api.controlplane.model.Spec
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SpecRepository : CoroutineCrudRepository<Spec, Int> {
    @Query("select * from spec where version = :version and api_id = :id")
    suspend fun getByVersion(apiId: Int, version: Int): Spec?
}