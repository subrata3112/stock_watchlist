package com.watchlist.stock_tracker.clients;

import com.watchlist.stock_tracker.exceptions.CustomException;
import tools.jackson.databind.ObjectMapper;
import com.watchlist.stock_tracker.clients.yahoo.YahooNumericValue;
import com.watchlist.stock_tracker.clients.yahoo.YahooQuoteSummaryResponse;
import com.watchlist.stock_tracker.dtos.YahooStockQuoteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YahooFinanceClient {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String YF_COOKIE_KEY = "yahoofinance.cookie";
    private static final String YF_CRUMB_KEY = "yahoofinance.crumb";
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void initializeYahooSession() throws IOException {
        System.setProperty("http.agent", USER_AGENT);

        URL cookieUrl = new URL("https://fc.yahoo.com");
        HttpURLConnection cookieConn = (HttpURLConnection) cookieUrl.openConnection();
        cookieConn.setRequestMethod("GET");
        cookieConn.setRequestProperty("User-Agent", USER_AGENT);
        List<String> rawCookies = cookieConn.getHeaderFields().get("Set-Cookie");
        if (rawCookies == null || rawCookies.isEmpty()) {
            throw new RuntimeException("Could not extract security cookie headers from Yahoo.");
        }
        String sanitizedCookie = rawCookies.stream()
                .map(c -> c.split(";")[0])
                .collect(Collectors.joining("; "));

        String activeCrumb = getActiveCrumb(sanitizedCookie);
        System.setProperty(YF_COOKIE_KEY, sanitizedCookie);
        System.setProperty(YF_CRUMB_KEY, activeCrumb);

        log.info("Yahoo Handshake Succeeded. Properties registered.");
    }



    private static String getActiveCrumb(String sanitizedCookie) throws IOException {
        URL crumbUrl = new URL("https://query2.finance.yahoo.com/v1/test/getcrumb");
        HttpURLConnection crumbConn = (HttpURLConnection) crumbUrl.openConnection();
        crumbConn.setRequestMethod("GET");
        crumbConn.setRequestProperty("User-Agent", USER_AGENT);
        crumbConn.setRequestProperty("Cookie", sanitizedCookie);

        String activeCrumb;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(crumbConn.getInputStream()))) {
            activeCrumb = reader.readLine();
        }

        if (activeCrumb == null || activeCrumb.isBlank()) {
            throw new RuntimeException("Yahoo verification returned an empty crumb.");
        }
        return activeCrumb;
    }



    public YahooStockQuoteDto getStockQuote(String ticker) throws IOException {
        String cookie = System.getProperty(YF_COOKIE_KEY);
        String crumb = System.getProperty(YF_CRUMB_KEY);

        if (cookie == null || crumb == null) {
            initializeYahooSession();
            cookie = System.getProperty(YF_COOKIE_KEY);
            crumb = System.getProperty(YF_CRUMB_KEY);
        }

        String url = "https://query2.finance.yahoo.com/v10/finance/quoteSummary/" + ticker
                + "?modules=price,summaryDetail&crumb=" + crumb;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Cookie", cookie);

        if (conn.getResponseCode() != 200) {
            initializeYahooSession();
            var retry = conn.getResponseCode();
            if (retry != 200) {
                throw new CustomException("Failed to fetch Yahoo Data", 500);
            }
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        YahooQuoteSummaryResponse summary = objectMapper.readValue(response.toString(), YahooQuoteSummaryResponse.class);
        YahooQuoteSummaryResponse.Result result = summary.getQuoteSummary().getResult().get(0);

        return toStockQuoteDto(result.getPrice(), result.getSummaryDetail());
    }



    private YahooStockQuoteDto toStockQuoteDto(YahooQuoteSummaryResponse.Price price,
                                               YahooQuoteSummaryResponse.SummaryDetail summaryDetail) {
        if (price == null) {
            return null;
        }

        return YahooStockQuoteDto.builder()
                .regularMarketPrice(numericValue(price.getRegularMarketPrice()))
                .changePercentageDay(ratioToPercent(price.getRegularMarketChangePercent()))
                .changePercentage52WeekLow(ratioToPercent(summaryDetail != null
                        ? summaryDetail.getFiftyTwoWeekLowChangePercent()
                        : null))
                .build();
    }

    private BigDecimal numericValue(YahooNumericValue value) {
        return value != null ? value.getValue() : null;
    }

    private BigDecimal ratioToPercent(YahooNumericValue value) {
        BigDecimal ratio = numericValue(value);
        if (ratio == null) {
            return null;
        }
        return ratio.multiply(ONE_HUNDRED).setScale(4, RoundingMode.HALF_UP);
    }

}


