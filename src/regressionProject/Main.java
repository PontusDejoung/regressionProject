package regressionProject;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // 1. Skapa en instans av DateConverter med ett startdatum
            DateConverter dateConverter = new DateConverter("2024-10-01");

            // 2. Läs in aktiedata från en CSV-fil
            String filePath = "stock_data.csv"; // Filen ska ha kolumnerna "date" och "close"
            Table stockData = Table.read().csv(filePath);

            // 3. Konvertera datum till numeriska värden och extrahera pris
            DoubleColumn daysColumn = DoubleColumn.create("days");
            DoubleColumn priceColumn = stockData.doubleColumn("close");

            for (String dateString : stockData.stringColumn("date")) {
                long days = dateConverter.dateToDays(dateString);
                daysColumn.append(days);
            }

            // Lägg till kolumnen "days" till tabellen
            stockData.addColumns(daysColumn);

            // 4. Konvertera kolumnerna till arrays för regressionen
            double[] xData = daysColumn.asDoubleArray();
            double[] yData = priceColumn.asDoubleArray();

            // 5. Skapa och träna en regressionsmodell
            LinearRegression regression = new LinearRegression(xData, yData);

            // Skriv ut lutningen och interceptet
            System.out.println("Lutning (m): " + regression.getSlope());
            System.out.println("Intercept (b): " + regression.getIntercept());

            // 6. Gör en förutsägelse för ett framtida datum
            String futureDate = "2023-12-31";
            long futureDays = dateConverter.dateToDays(futureDate);
            double predictedPrice = regression.predict(futureDays);

            System.out.println("Förutsagd pris för " + futureDate + ": " + predictedPrice);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
