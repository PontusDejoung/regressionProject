package regressionProject;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.NumericColumn;
import java.io.IOException;
import java.util.List;

/**
 * DataLoader - A utility class to load, process, and retrieve data from a CSV file.
 * This class uses the Tablesaw library to read data and convert specific columns into
 * formats suitable for regression models.
 */
public class DataLoader {
    private String filePath;                   // Path to the CSV file
    private String dateColumn;                 // Column name for timestamps
    private String targetColumn;               // Column name for target variable (e.g., price)
    private List<String> featureColumns;       // List of additional feature columns
    private DateConverter dateConverter;       // DateConverter instance for handling date transformations

    /**
     * Constructor to initialize DataLoader with necessary information.
     *
     * @param filePath       Path to the CSV file
     * @param dateColumn     Column name for timestamp data
     * @param targetColumn   Column name for the target variable
     * @param featureColumns List of additional feature column names
     * @param dateConverter  DateConverter instance for handling date transformations
     */
    public DataLoader(String filePath, String dateColumn, String targetColumn, List<String> featureColumns, DateConverter dateConverter) {
        this.filePath = filePath;
        this.dateColumn = dateColumn;
        this.targetColumn = targetColumn;
        this.featureColumns = featureColumns;
        this.dateConverter = dateConverter;
    }

    /**
     * Method to load and process data from the CSV file.
     * Converts timestamps to intervals and verifies that all feature columns are present.
     *
     * @return Processed Table object containing stock data
     * @throws IOException If the CSV file cannot be read
     */
    public Table loadData() throws IOException {
        Table stockData = Table.read().csv(filePath);

        // Convert timestamps to intervals of 30 minutes since the start date
        DoubleColumn intervalsColumn = DoubleColumn.create("intervals_since_start");
        for (String dateString : stockData.stringColumn(dateColumn)) {
            long intervals = dateConverter.dateToIntervals(dateString);
            intervalsColumn.append(intervals);
        }
        stockData.addColumns(intervalsColumn);

        // Verify that all additional feature columns are present in the data
        for (String featureColumn : featureColumns) {
            if (!stockData.columnNames().contains(featureColumn)) {
                throw new IllegalArgumentException("Column " + featureColumn + " is not found in the CSV file.");
            }
        }

        // Return the processed data table
        return stockData;
    }

    /**
     * Method to retrieve the target variable (e.g., price) as an array.
     *
     * @param stockData Table containing the stock data
     * @return          Array of target values from the specified target column
     */
    public double[] getTargetValues(Table stockData) {
        return stockData.doubleColumn(targetColumn).asDoubleArray();
    }

    /**
     * Method to retrieve input features for regression as a 2D array.
     *
     * @param stockData Table containing the stock data
     * @return          2D array of feature values, where each row corresponds to an observation
     */
    public double[][] getFeatureValues(Table stockData) {
        int numRows = stockData.rowCount();
        int numFeatures = featureColumns.size() + 1; // includes intervals_since_start and additional columns

        double[][] features = new double[numRows][numFeatures];
        
        // Populate the first feature column with values from intervals_since_start
        DoubleColumn intervalsColumn = stockData.doubleColumn("intervals_since_start");
        for (int i = 0; i < numRows; i++) {
            features[i][0] = intervalsColumn.get(i);
        }

        // Populate remaining feature columns with values from additional feature columns
        for (int j = 0; j < featureColumns.size(); j++) {
            NumericColumn<?> numericColumn = stockData.numberColumn(featureColumns.get(j)); // Fetch as NumericColumn

            // Convert IntColumn to DoubleColumn if necessary
            if (numericColumn instanceof IntColumn) {
                numericColumn = ((IntColumn) numericColumn).asDoubleColumn();
            }

            // Fill feature array with values from the column, starting at index 1
            for (int i = 0; i < numRows; i++) {
                features[i][j + 1] = numericColumn.getDouble(i); // Start at index 1 to leave room for intervals_since_start
            }
        }

        return features;
    }
}
