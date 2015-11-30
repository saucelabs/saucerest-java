package com.saucelabs.saucerest.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * Created by gavinmogan on 2015-11-28.
 */
public class UnixtimeDeserializer extends StdDeserializer<Date> {
    public UnixtimeDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        double val = Double.parseDouble(jsonParser.getValueAsString());
        return new Date((long)(1000*val));
    }
}