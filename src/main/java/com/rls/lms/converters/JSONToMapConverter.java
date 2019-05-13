package com.rls.lms.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class JSONToMapConverter  implements AttributeConverter<Map<String, Object>, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONToMapConverter.class);

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(attribute);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("Could not convert map to json string.");
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new HashMap<>();
        }
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(dbData, HashMap.class);
        }
        catch (IOException e) {
            LOGGER.error("Convert error while trying to convert string(JSON) to map data structure.");
        }
        return new HashMap<>();
    }
}
