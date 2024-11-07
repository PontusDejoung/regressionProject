package regressionProject;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateConverter - A utility class to convert dates into intervals (in 30-minute increments)
 * since a specified start date. This class also supports conversion from intervals back to dates.
 * Useful for time series analysis where dates need to be standardized into consistent intervals.
 */
public class DateConverter {
    private OffsetDateTime startDate;                     // The start date for interval calculations
    private OffsetDateTime endDate;                       // The end date for reference purposes
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

    /**
     * Constructor to initialize DateConverter with start and end dates.
     * Both dates are expected in the format "yyyy-MM-dd HH:mm:ss+01:00".
     *
     * @param startDateString Start date as a string
     * @param endDateString   End date as a string
     */
    public DateConverter(String startDateString, String endDateString) {
        this.startDate = OffsetDateTime.parse(startDateString, formatter).withOffsetSameInstant(ZoneOffset.of("+01:00"));
        this.endDate = OffsetDateTime.parse(endDateString, formatter).withOffsetSameInstant(ZoneOffset.of("+01:00"));
    }

    /**
     * Converts a date string to the number of 30-minute intervals since the start date.
     *
     * @param dateString Date to be converted, in format "yyyy-MM-dd HH:mm:ss+01:00"
     * @return           Number of 30-minute intervals from the start date
     */
    public long dateToIntervals(String dateString) {
        OffsetDateTime date = OffsetDateTime.parse(dateString, formatter).withOffsetSameInstant(ZoneOffset.of("+01:00"));
        return ChronoUnit.MINUTES.between(startDate, date) / 30;
    }

    /**
     * Converts the number of 30-minute intervals since the start date back to a formatted date string.
     *
     * @param intervals Number of 30-minute intervals from the start date
     * @return          Date string in the format "yyyy-MM-dd HH:mm:ss+01:00"
     */
    public String intervalsToDate(long intervals) {
        OffsetDateTime date = startDate.plusMinutes(intervals * 30);
        return date.format(formatter);
    }

    /**
     * Gets the end date as an OffsetDateTime object.
     *
     * @return The end date
     */
    public OffsetDateTime getEndDate() {
        return endDate;
    }
}
