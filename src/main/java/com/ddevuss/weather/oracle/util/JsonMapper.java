package com.ddevuss.weather.oracle.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractValue(String json, String fieldName) {
        try {
            JsonNode fieldNode = objectMapper.readTree(json).path(fieldName);

            if (fieldNode.isNull() || fieldNode.isMissingNode() || fieldNode.asText().isEmpty() || fieldNode.asText().isBlank()) {
                throw new IllegalArgumentException("Missing '" + fieldName + "'.");
            }

            return fieldNode.asText();
        }
        catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON object.");
        }
    }
}
