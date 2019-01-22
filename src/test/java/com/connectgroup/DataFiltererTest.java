package com.connectgroup;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataFiltererTest {

    private final String empty = "src/test/resources/empty";
    private final String multiLine = "src/test/resources/multi-lines";

    @Test
    public void shouldReturnEmptyCollection_WhenLogFileIsEmpty() throws FileNotFoundException {
        assertTrue(DataFilterer.filterByCountry(openFile(empty), "GB").isEmpty());
        assertTrue(DataFilterer.filterByCountryWithResponseTimeAboveLimit(openFile(empty), "GB", 1).isEmpty());
        assertTrue(DataFilterer.filterByResponseTimeAboveAverage(openFile(empty)).isEmpty());
    }

    @Test
    public void shouldFilterLogByCountryCode_WhenLogFileHasMultipleEntries() throws FileNotFoundException {
        final String countryCode = "GB";

        Collection<LogEntry> logEntries = DataFilterer.filterByCountry(openFile(multiLine), countryCode);

        assertEquals(logEntries.size(), 1);
        assertTrue(logEntries.stream().allMatch(logEntry -> logEntry.getCountryCode().equals(countryCode)));
    }

    @Test
    public void shouldFilterLogByCountryWithResponseTimeAboveLimit_WhenLogFileHasMultipleEntries() throws FileNotFoundException {

        final long limit = 600;
        final String countryCode = "US";

        Collection<LogEntry> logEntries = DataFilterer.filterByCountryWithResponseTimeAboveLimit(openFile(multiLine), countryCode, limit);

        assertEquals(logEntries.size(), 2);
        assertTrue(logEntries.stream()
                .allMatch(logEntry -> logEntry.getResponseTime() > limit && logEntry.getCountryCode().equals(countryCode)));
    }


    @Test
    public void shouldFilterLogByAboveAverageResponseTime_WhenLogFileHasMultipleEntries() throws FileNotFoundException {

        final double average = 526.0;

        Collection<LogEntry> logEntries = DataFilterer.filterByResponseTimeAboveAverage(openFile(multiLine));

        assertEquals(logEntries.size(), 3);
        assertTrue(logEntries.stream().allMatch(logEntry -> logEntry.getResponseTime() > average));
    }

    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(new File(filename));
    }
}
