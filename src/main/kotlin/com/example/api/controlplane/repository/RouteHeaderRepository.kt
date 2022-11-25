package com.example.api.controlplane.repository

import com.example.api.controlplane.model.RouteHeader
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RouteHeaderRepository : CoroutineCrudRepository<RouteHeader, Int> {
    @Query("select * from route_header where route_id = :routeId")
    fun getAll(routeId: Int): Flow<RouteHeader>
}