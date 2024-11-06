package regressionProject;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class PolynomialRegression {
    private final OLSMultipleLinearRegression regression;
    private final int degree;

    public PolynomialRegression(double[] x, double[] y, int degree) {
        this.degree = degree;
        regression = new OLSMultipleLinearRegression();
        
        // Omvandla x-värdena till polynomiska termer
        double[][] xPoly = new double[x.length][degree];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < degree; j++) {
                xPoly[i][j] = Math.pow(x[i], j + 1);
            }
        }
        
        regression.newSampleData(y, xPoly);
    }

    // Förutsäg y för ett givet x-värde
    public double predict(double x) {
        double[] xPoly = new double[degree];
        for (int j = 0; j < degree; j++) {
            xPoly[j] = Math.pow(x, j + 1);
        }
        double[] coefficients = regression.estimateRegressionParameters();
        
        double yPred = coefficients[0]; // intercept
        for (int j = 0; j < degree; j++) {
            yPred += coefficients[j + 1] * xPoly[j];
        }
        return yPred;
    }
}
