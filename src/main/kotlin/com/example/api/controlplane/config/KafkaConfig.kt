package com.example.api.controlplane.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.config")
data class KafkaConfig(
    var host: String = "localhost:9092",
    var topic: String = "api"
)
