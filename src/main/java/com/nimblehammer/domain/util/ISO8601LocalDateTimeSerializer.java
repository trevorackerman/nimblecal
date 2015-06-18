package com.nimblehammer.domain.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ISO8601LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime instant, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(instant));
    }
}
