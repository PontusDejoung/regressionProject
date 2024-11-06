package regressionProject;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.NumericColumn;
import java.io.IOException;
import java.util.List;

public class DataLoader {
    private String filePath;
    private String dateColumn;
    private String targetColumn;
    private List<String> featureColumns;
    private DateConverter dateConverter;

    // Konstruktor för att initiera DataLoader med nödvändig information
    public DataLoader(String filePath, String dateColumn, String targetColumn, List<String> featureColumns, DateConverter dateConverter) {
        this.filePath = filePath;
        this.dateColumn = dateColumn;
        this.targetColumn = targetColumn;
        this.featureColumns = featureColumns;
        this.dateConverter = dateConverter;
    }

    // Metod för att läsa och bearbeta data från CSV-filen
    public Table loadData() throws IOException {
        Table stockData = Table.read().csv(filePath);

        // Konvertera tidsstämplar till antalet 30-minutersintervall
        DoubleColumn intervalsColumn = DoubleColumn.create("intervals_since_start");
        for (String dateString : stockData.stringColumn(dateColumn)) {
            long intervals = dateConverter.dateToIntervals(dateString);
            intervalsColumn.append(intervals);
        }
        stockData.addColumns(intervalsColumn);

        // Kontrollera att alla extra feature-kolumner finns i data
        for (String featureColumn : featureColumns) {
            if (!stockData.columnNames().contains(featureColumn)) {
                throw new IllegalArgumentException("Kolumnen " + featureColumn + " finns inte i CSV-filen.");
            }
        }

        // Returnera den bearbetade tabellen
        return stockData;
    }

    // Metod för att hämta målvärdet (t.ex. pris) som en array
    public double[] getTargetValues(Table stockData) {
        return stockData.doubleColumn(targetColumn).asDoubleArray();
    }

    // Metod för att hämta indata för regression som en 2D-array
    public double[][] getFeatureValues(Table stockData) {
        int numRows = stockData.rowCount();
        int numFeatures = featureColumns.size() + 1; // intervals_since_start + extra kolumner

        double[][] features = new double[numRows][numFeatures];
        
        // Fyll på med värden för intervals_since_start
        DoubleColumn intervalsColumn = stockData.doubleColumn("intervals_since_start");
        for (int i = 0; i < numRows; i++) {
            features[i][0] = intervalsColumn.get(i);
        }

        // Fyll på med värden för extra kolumner
        for (int j = 0; j < featureColumns.size(); j++) {
            NumericColumn<?> numericColumn = stockData.numberColumn(featureColumns.get(j)); // Hämta som NumericColumn

            // Om kolumnen är av typen IntColumn, konvertera den till DoubleColumn
            if (numericColumn instanceof IntColumn) {
                numericColumn = ((IntColumn) numericColumn).asDoubleColumn();
            }

            // Fyll features-arrayen med värden från kolumnen
            for (int i = 0; i < numRows; i++) {
                features[i][j + 1] = numericColumn.getDouble(i); // Börjar på index 1 för att lämna plats för intervals_since_start
            }
        }

        return features;
    }
}
