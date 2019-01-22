package com.connectgroup;

/**
 * Model for entry in application request log
 */
public class LogEntry {

    private Long requestTimestamp;
    private String countryCode;
    private Long responseTime;

    public LogEntry(Long requestTimestamp, String countryCode, Long responseTime) {
        this.requestTimestamp = requestTimestamp;
        this.countryCode = countryCode;
        this.responseTime = responseTime;
    }

    /**
     * @return Unix timestamp of the time the request was made
     */
    public Long getRequestTimestamp() {
        return requestTimestamp;
    }

    /**
     * @return country from which the request originated
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @return time in milliseconds which the request took to complete
     */
    public Long getResponseTime() {
        return responseTime;
    }
}
