package com.xavelo.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String key;
    private String value;
}

