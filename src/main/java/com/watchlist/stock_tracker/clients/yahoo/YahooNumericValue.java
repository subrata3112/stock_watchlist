package com.watchlist.stock_tracker.clients.yahoo;

import tools.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonDeserialize(using = YahooNumericValueDeserializer.class)
public class YahooNumericValue {

    private BigDecimal raw;

    private String fmt;

    public BigDecimal getValue() {
        return raw;
    }
}
