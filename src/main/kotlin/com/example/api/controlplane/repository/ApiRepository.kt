package com.example.api.controlplane.repository

import com.example.api.controlplane.model.Api
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiRepository : CoroutineCrudRepository<Api, Int>