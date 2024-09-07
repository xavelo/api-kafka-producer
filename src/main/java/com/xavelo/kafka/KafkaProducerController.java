package com.xavelo.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {

    private static final Logger logger = LogManager.getLogger(KafkaProducerController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ping from pod " + podName);
    }

    @PostMapping("/produce")
    public ResponseEntity<Message> produce(@RequestBody Message message) {
        kafkaService.produceMessage("test-topic", message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }



}

