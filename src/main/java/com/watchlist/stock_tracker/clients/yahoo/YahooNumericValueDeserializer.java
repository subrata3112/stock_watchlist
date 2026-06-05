package com.watchlist.stock_tracker.clients.yahoo;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

public class YahooNumericValueDeserializer extends ValueDeserializer<YahooNumericValue> {

    @Override
    public YahooNumericValue deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        JsonNode node = parser.objectReadContext().readTree(parser);
        YahooNumericValue value = new YahooNumericValue();

        if (node.isNumber()) {
            value.setRaw(node.decimalValue());
            return value;
        }

        if (node.isObject()) {
            JsonNode raw = node.get("raw");
            if (raw != null && !raw.isNull()) {
                value.setRaw(raw.decimalValue());
            }
            JsonNode fmt = node.get("fmt");
            if (fmt != null && !fmt.isNull()) {
                value.setFmt(fmt.asString());
            }
        }

        return value;
    }
}
