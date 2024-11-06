package regressionProject;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import java.util.ArrayList;
import java.util.List;

public class MultivariatePolynomialRegression {
    private final OLSMultipleLinearRegression regression;
    private final int degree;
    private final int numFeatures;

    public MultivariatePolynomialRegression(double[][] X, double[] y, int degree, int numFeatures) {
        this.degree = degree;
        this.numFeatures = numFeatures;
        this.regression = new OLSMultipleLinearRegression();

        // Skapa polynomiska termer för varje kolumn i X
        double[][] polyX = createPolynomialFeatures(X);
        regression.newSampleData(y, polyX);
    }

    // Skapa polynomiska termer för varje funktion upp till specificerad grad
    private double[][] createPolynomialFeatures(double[][] X) {
        List<double[]> polyFeatures = new ArrayList<>();
        for (double[] row : X) {
            List<Double> polyRow = new ArrayList<>();

            // Lägg till polynomiska termer för varje funktion
            for (int i = 0; i < numFeatures; i++) {
                for (int d = 1; d <= degree; d++) {
                    polyRow.add(Math.pow(row[i], d));
                }
            }

            // Konvertera till en array och lägg till
            polyFeatures.add(polyRow.stream().mapToDouble(Double::doubleValue).toArray());
        }
        return polyFeatures.toArray(new double[0][]);
    }

    public double predict(double[] x) {
        // Kontrollera att längden på x stämmer överens med numFeatures
        if (x.length != numFeatures) {
            throw new IllegalArgumentException("Felaktigt antal indatafunktioner: förväntat " + numFeatures + ", men fick " + x.length);
        }

        // Skapa polynomiska termer för den nya dataraden
        double[] polyX = new double[degree * numFeatures];
        int index = 0;
        for (int i = 0; i < numFeatures; i++) {
            for (int d = 1; d <= degree; d++) {
                polyX[index++] = Math.pow(x[i], d);
            }
        }

        // Hämta koefficienterna från modellen
        double[] coefficients = regression.estimateRegressionParameters();
        
        // Beräkna förutsägelser baserat på intercept och koefficienter
        double yPred = coefficients[0]; // intercept
        for (int i = 0; i < polyX.length; i++) {
            yPred += coefficients[i + 1] * polyX[i];
        }
        return yPred;
    }

}
