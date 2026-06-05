package com.watchlist.stock_tracker.clients.yahoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooQuoteSummaryResponse {

    private QuoteSummary quoteSummary;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuoteSummary {

        private List<Result> result;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        private Price price;

        private SummaryDetail summaryDetail;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {

        private YahooNumericValue regularMarketPrice;

        private YahooNumericValue regularMarketChangePercent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SummaryDetail {

        private YahooNumericValue fiftyTwoWeekLowChangePercent;
    }
}
