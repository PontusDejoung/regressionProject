package regressionProject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateConverter {

    private LocalDate startDate;

    // Konstruktor som tar ett startdatum i formatet "yyyy-MM-dd"
    public DateConverter(String startDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.startDate = LocalDate.parse(startDateString, formatter);
    }

    // Konvertera ett datum till antalet dagar sedan startdatumet
    public long dateToDays(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return ChronoUnit.DAYS.between(startDate, date);
    }

    // Konvertera antalet dagar sedan startdatumet tillbaka till ett datum
    public String daysToDate(long days) {
        LocalDate date = startDate.plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
