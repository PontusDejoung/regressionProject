package regressionProject;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            // Ange start- och slutdatum
            String startDate = "2024-08-01 00:00:00+01:00";
            String endDate = "2024-10-31 23:30:00+01:00";
            DateConverter dateConverter = new DateConverter(startDate, endDate);

            // Definiera kolumner att inkludera som extra funktioner (t.ex. "volym")
            List<String> featureColumns = Arrays.asList("volume","trade_count");

            // Skapa en DataLoader-instans och läs in data från CSV
            String filePath = "wulf.csv";
            DataLoader dataLoader = new DataLoader(filePath, "timestamp", "close", featureColumns, dateConverter);
            Table stockData = dataLoader.loadData();

            // Skapa regressionsmodellen baserat på data och vald polynomgrad
            int degree = 2; // Exempel: grad 2 (kvadratisk)
            MultivariatePolynomialRegression regression = createRegression(stockData, dataLoader, degree);

            // Generera och skriv ut förutsägelser
            generatePredictions(stockData, dateConverter, regression, filePath, 500);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Skapa polynomisk regressionsmodell med extra funktioner
    private static MultivariatePolynomialRegression createRegression(Table stockData, DataLoader dataLoader, int degree) {
        // Hämta indatafunktioner och målvariabler från DataLoader
        double[][] X = dataLoader.getFeatureValues(stockData);
        double[] y = dataLoader.getTargetValues(stockData);

        // Skapa och returnera regressionsmodellen
        return new MultivariatePolynomialRegression(X, y, degree, X[0].length);
    }

 // Generera och skriv ut förutsägelser
    private static void generatePredictions(Table stockData, DateConverter dateConverter, 
                                            MultivariatePolynomialRegression regression, 
                                            String filePath, int futureIntervals) throws IOException {
        // Ladda originaldata för jämförelse av faktiska värden
        Table originalData = Table.read().csv(filePath);
        StringColumn dateColumn = originalData.stringColumn("timestamp");
        DoubleColumn originalPriceColumn = originalData.doubleColumn("close");

        OffsetDateTime currentDate = dateConverter.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

        System.out.println("\nFörutsägelser:");
        for (int i = 1; i <= futureIntervals; i++) {
            // Lägg till 30-minutersintervall
            currentDate = currentDate.plusMinutes(30);
            String currentDateStr = currentDate.format(formatter);
            
            long intervalsFromStart = dateConverter.dateToIntervals(currentDateStr);

            // Skapa indata för förutsägelse (lägger till dummy-variabler om nödvändigt)
            double[] newInput = new double[] { intervalsFromStart, 0.0, 0.0 }; // Justera antal 0.0 enligt numFeatures

            // Förutsäg priset för detta intervall
            double predictedPrice = regression.predict(newInput);

            double actualPrice = Double.NaN;
            int rowIndex = dateColumn.indexOf(currentDateStr);
            if (rowIndex != -1) {
                actualPrice = originalPriceColumn.get(rowIndex);
            }

            printPredictionResult(currentDateStr, predictedPrice, actualPrice);
        }
    }

    // Metod för att skriva ut förutsägelse och jämförelse med faktiska värden
    private static void printPredictionResult(String date, double predictedPrice, double actualPrice) {
        if (!Double.isNaN(actualPrice)) {
            System.out.println("Datum: " + date + ", Förutsagd pris: " + predictedPrice + ", Faktiskt pris: " + actualPrice + "\n");
        } //else {
            //System.out.println("Datum: " + date + ", Förutsagd pris: " + predictedPrice + ", Faktiskt pris: Saknas");
        //}
    }
}
