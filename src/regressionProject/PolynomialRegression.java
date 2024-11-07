package regressionProject;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

/**
 * PolynomialRegression - A class to fit and make predictions using polynomial regression.
 * This class allows fitting of a polynomial model to a dataset with one independent variable (x)
 * and one dependent variable (y), using ordinary least squares (OLS) regression.
 */
public class PolynomialRegression {
    private final OLSMultipleLinearRegression regression; // OLS regression model from Apache Commons Math
    private final int degree;                             // Degree of the polynomial

    /**
     * Constructor to initialize and fit a polynomial regression model.
     *
     * @param x      Array of x-values (independent variable)
     * @param y      Array of y-values (dependent variable)
     * @param degree Degree of the polynomial for the regression model
     */
    public PolynomialRegression(double[] x, double[] y, int degree) {
        this.degree = degree;
        regression = new OLSMultipleLinearRegression();
        
        // Transform x-values to polynomial terms up to the specified degree
        double[][] xPoly = new double[x.length][degree];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < degree; j++) {
                xPoly[i][j] = Math.pow(x[i], j + 1); // Generate terms like x, x^2, x^3, etc.
            }
        }
        
        regression.newSampleData(y, xPoly); // Fit the model with the transformed polynomial features
    }

    /**
     * Predicts the y-value for a given x-value using the polynomial model.
     *
     * @param x Input x-value for which the prediction is made
     * @return  Predicted y-value
     */
    public double predict(double x) {
        // Create polynomial terms for the input x-value up to the specified degree
        double[] xPoly = new double[degree];
        for (int j = 0; j < degree; j++) {
            xPoly[j] = Math.pow(x, j + 1);
        }
        
        // Get model coefficients from the regression object
        double[] coefficients = regression.estimateRegressionParameters();
        
        // Calculate predicted y-value using intercept and polynomial coefficients
        double yPred = coefficients[0]; // intercept term
        for (int j = 0; j < degree; j++) {
            yPred += coefficients[j + 1] * xPoly[j];
        }
        return yPred;
    }
}
