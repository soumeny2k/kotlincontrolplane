package com.example.api.controlplane.kafka

import com.example.api.controlplane.config.KafkaConfig
import com.example.api.controlplane.transferobject.ControlPlane
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord

@Component
class Publisher(private val config: KafkaConfig) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    private final var sender: KafkaSender<String, String>

    init {
        val producerProperties = mapOf(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to config.host
        )
        sender = KafkaSender.create(
            SenderOptions.create(producerProperties)
        )
    }

    fun send(apiName: String, kafkaData: KafkaData) {
        sender.send(
            Mono.just(
                SenderRecord.create(
                    config.topic,
                    null,
                    null,
                    apiName,
                    mapper.writeValueAsString(kafkaData),
                    apiName
                )
            )
        ).doOnError {
            logger.error("error publishing to kafka")
        }.doOnNext {
            logger.info("data published to kafka")
        }.subscribe()
    }
}

data class KafkaData(
    val event: String,
    val data: String
)