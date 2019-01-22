package com.connectgroup;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataFilterer {


    private static final String SEPARATOR = ",";

    public static Collection<LogEntry> filterByCountry(Reader source, String country) {

        return getLogFileEntryStream(source)
                .filter(logEntry -> logEntry.getCountryCode().equals(country))
                .collect(Collectors.toList());
    }

    public static Collection<LogEntry> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {

        return DataFilterer.filterByCountry(source, country).stream()
                .filter(logEntry -> logEntry.getResponseTime() > limit)
                .collect(Collectors.toList());
    }

    public static Collection<LogEntry> filterByResponseTimeAboveAverage(Reader source) {

        final List<LogEntry> logEntries = getLogFileEntryStream(source)
                .collect(Collectors.toList());

        final Double averageResponseTime = logEntries.stream()
                .collect(Collectors.averagingLong(LogEntry::getResponseTime));

        return logEntries.stream()
                .filter(logEntry -> logEntry.getResponseTime() > averageResponseTime)
                .collect(Collectors.toList());
    }

    private static Stream<LogEntry> getLogFileEntryStream(Reader source) {
        return new BufferedReader(source).lines().skip(1).map(DataFilterer::parseLogFileEntry);
    }

    private static LogEntry parseLogFileEntry(String logFileEntry) {

        // Making the assumption the csv file always matches the specification detailed in the README
        // and we do not need a parser to validate the content
        final String[] entry = logFileEntry.split(SEPARATOR);
        return new LogEntry(Long.parseLong(entry[0]), entry[1], Long.parseLong(entry[2]));
    }

}