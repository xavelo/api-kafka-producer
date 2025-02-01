package com.xavelo.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaProducerController {

    private static final Logger logger = LogManager.getLogger(KafkaProducerController.class);

    private static final String TOPIC = "test-topic";

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.info("Ping from pod {}", podName);
        return ResponseEntity.ok("ping from pod " + podName);
    }

    @PostMapping("/produce")
    public ResponseEntity<Message> produce(@RequestBody Message message) {
        logger.info("Producing message {} to {}", message, TOPIC);
        kafkaService.produceMessage(TOPIC, message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/produce/string")
    public ResponseEntity<String> produceString(@RequestBody String message) {
        logger.info("Producing message {} to {}", message, TOPIC);
        kafkaService.produceMessageString(TOPIC, message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/produce/string/batch/{size}")
    public ResponseEntity<String> produceStringBatch(@PathVariable int size, @RequestBody String message) {
        logger.info("Producing message {} to {} in batch size {}", message, TOPIC, size);
        kafkaService.produceMessageStringBatch(TOPIC, message, size);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

}

