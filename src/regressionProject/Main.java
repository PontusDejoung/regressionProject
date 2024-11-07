package regressionProject;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Main - Class to initialize and run polynomial regression on time series stock data.
 * This class uses DataLoader to load data from a CSV file, trains a polynomial regression model,
 * and generates predictions for future time intervals.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // 1. Define the start and end dates for the dataset to handle time-based data
            String startDate = "2024-08-01 00:00:00+01:00";
            String endDate = "2024-10-31 23:30:00+01:00";
            DateConverter dateConverter = new DateConverter(startDate, endDate);

            // 2. Define additional columns to be included as features (e.g., "volume")
            List<String> featureColumns = Arrays.asList("volume", "trade_count");

            // 3. Create a DataLoader instance to read data from the CSV file
            String filePath = "wulf.csv";
            DataLoader dataLoader = new DataLoader(filePath, "timestamp", "close", featureColumns, dateConverter);
            Table stockData = dataLoader.loadData(); // Load and process data from file

            // 4. Create the regression model based on data and chosen polynomial degree
            int degree = 2; // Example: degree 2 (quadratic)
            MultivariatePolynomialRegression regression = createRegression(stockData, dataLoader, degree);

            // 5. Generate and print predictions for future intervals
            generatePredictions(stockData, dateConverter, regression, filePath, 500);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a multivariate polynomial regression model with additional features.
     *
     * @param stockData   Table containing the loaded stock data
     * @param dataLoader  DataLoader instance to handle data extraction and formatting
     * @param degree      Degree of the polynomial for regression
     * @return            A trained MultivariatePolynomialRegression model
     */
    private static MultivariatePolynomialRegression createRegression(Table stockData, DataLoader dataLoader, int degree) {
        // Extract input features and target variables from DataLoader
        double[][] X = dataLoader.getFeatureValues(stockData);
        double[] y = dataLoader.getTargetValues(stockData);

        // Create and return the regression model
        return new MultivariatePolynomialRegression(X, y, degree, X[0].length);
    }

    /**
     * Generates and prints predictions for future time intervals.
     *
     * @param stockData         Table containing the stock data for reference
     * @param dateConverter     DateConverter instance to handle date conversions
     * @param regression        Trained regression model to make predictions
     * @param filePath          File path to the original CSV file for reference
     * @param futureIntervals   Number of future intervals to predict
     * @throws IOException      If data file cannot be read
     */
    private static void generatePredictions(Table stockData, DateConverter dateConverter,
                                            MultivariatePolynomialRegression regression,
                                            String filePath, int futureIntervals) throws IOException {
        // Load original data for actual value comparison
        Table originalData = Table.read().csv(filePath);
        StringColumn dateColumn = originalData.stringColumn("timestamp");
        DoubleColumn originalPriceColumn = originalData.doubleColumn("close");

        OffsetDateTime currentDate = dateConverter.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

        System.out.println("\nPredictions:");
        for (int i = 1; i <= futureIntervals; i++) {
            // Add a 30-minute interval
            currentDate = currentDate.plusMinutes(30);
            String currentDateStr = currentDate.format(formatter);

            long intervalsFromStart = dateConverter.dateToIntervals(currentDateStr);

            // Create input for prediction (add dummy values if needed)
            double[] newInput = new double[] { intervalsFromStart, 0.0, 0.0 }; // Adjust number of 0.0 according to numFeatures

            // Predict price for this interval
            double predictedPrice = regression.predict(newInput);

            // Find actual price for comparison if available
            double actualPrice = Double.NaN;
            int rowIndex = dateColumn.indexOf(currentDateStr);
            if (rowIndex != -1) {
                actualPrice = originalPriceColumn.get(rowIndex);
            }

            printPredictionResult(currentDateStr, predictedPrice, actualPrice);
        }
    }

    /**
     * Prints the prediction result and compares it with the actual value, if available.
     *
     * @param date           Date of the prediction
     * @param predictedPrice Predicted price by the model
     * @param actualPrice    Actual price from the data, if available
     */
    private static void printPredictionResult(String date, double predictedPrice, double actualPrice) {
        if (!Double.isNaN(actualPrice)) {
            System.out.println("Date: " + date + ", Predicted Price: " + predictedPrice + ", Actual Price: " + actualPrice + "\n");
        } 
    }
}
