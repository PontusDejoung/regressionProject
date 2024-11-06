package regressionProject;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateConverter {
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

    // Konstruktor som tar både start- och slutdatum i formatet "yyyy-MM-dd HH:mm:ss+01:00"
    public DateConverter(String startDateString, String endDateString) {
        this.startDate = OffsetDateTime.parse(startDateString, formatter).withOffsetSameInstant(ZoneOffset.of("+01:00"));
        this.endDate = OffsetDateTime.parse(endDateString, formatter).withOffsetSameInstant(ZoneOffset.of("+01:00"));
    }

    // Konvertera ett datum till antalet 30-minutersintervall sedan startdatumet
    public long dateToIntervals(String dateString) {
        OffsetDateTime date = OffsetDateTime.parse(dateString, formatter).withOffsetSameInstant(ZoneOffset.of("+01:00"));
        return ChronoUnit.MINUTES.between(startDate, date) / 30;
    }

    // Konvertera antalet 30-minutersintervall sedan startdatumet tillbaka till ett datum
    public String intervalsToDate(long intervals) {
        OffsetDateTime date = startDate.plusMinutes(intervals * 30);
        return date.format(formatter);
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }
}
