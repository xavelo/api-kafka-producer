package com.xavelo.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
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
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message.getKey(), message.getValue());
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        future.whenComplete((result, e) -> {
            if (e == null) {
                logger.info("Message successfully sent to '{}' [parition {}]", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());                
            } else {
                logger.error("Error sending message: {}", e.getMessage(), e);
            }
        });
    }

    public void produceMessage(String topic, String message) {

    }

    public void produceMessageString(String topic, String message) {
        logger.info("-> topic '{}' --- message '{}'", topic, message);
        kafkaTemplate.send(topic, message);
        logger.info("Message sent to topic '{}'", topic);
    }

    public void produceMessageStringBatch(String topic, String message, int size) {
        logger.info("-> topic '{}' --- message '{}' in batch size '{}'", topic, message, size);
        for(int i=0; i<size; i++) {
            kafkaTemplate.send(topic, i + " - " + message);
            logger.info("Message '{}' sent to topic '{}'", i + " - " + message, topic);
        }

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

