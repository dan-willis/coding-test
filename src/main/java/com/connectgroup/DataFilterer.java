package com.connectgroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class DataFilterer {


    private static final String SEPARATOR = ",";

    public static Collection<LogEntry> filterByCountry(Reader source, String country) {

        return getLogFileEntryStream(source).stream()
                .filter(logEntry -> logEntry.getCountryCode().equals(country))
                .collect(Collectors.toList());
    }

    public static Collection<LogEntry> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {

        return DataFilterer.filterByCountry(source, country).stream()
                .filter(logEntry -> logEntry.getResponseTime() > limit)
                .collect(Collectors.toList());
    }

    public static Collection<LogEntry> filterByResponseTimeAboveAverage(Reader source) {

        final Collection<LogEntry> logEntries = getLogFileEntryStream(source);

        final Double averageResponseTime = logEntries.stream()
                .collect(Collectors.averagingLong(LogEntry::getResponseTime));

        return logEntries.stream()
                .filter(logEntry -> logEntry.getResponseTime() > averageResponseTime)
                .collect(Collectors.toList());
    }

    private static Collection<LogEntry> getLogFileEntryStream(Reader source) {
        try (BufferedReader br = new BufferedReader(source)) {
            return br.lines().skip(1).map(DataFilterer::parseLogFileEntry).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static LogEntry parseLogFileEntry(String logFileEntry) {

        // Making the assumption the csv file always matches the specification detailed in the README
        // and we do not need a parser to validate the content
        final String[] entry = logFileEntry.split(SEPARATOR);
        return new LogEntry(Long.parseLong(entry[0]), entry[1], Long.parseLong(entry[2]));
    }

}