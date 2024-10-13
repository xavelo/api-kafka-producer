package com.xavelo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceMessage(String topic, Message message) {
        logger.info("-> topic '{}' --- message '{}'", topic, message);
        kafkaTemplate.send(topic, message.getValue());
        logger.info("Message sent to topic '{}'", topic);
    }

    public void produceMessageString(String topic, String message) {
        logger.info("-> topic '{}' --- message '{}'", topic, message);
        kafkaTemplate.send(topic, message);
        logger.info("Message sent to topic '{}'", topic);
    }

    public void sendAsynchMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("pi-topic", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.error("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                logger.error("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

}

