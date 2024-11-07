package regressionProject;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import java.util.ArrayList;
import java.util.List;

/**
 * MultivariatePolynomialRegression - A regression model to fit polynomial terms to multiple input features.
 * This class extends ordinary least squares (OLS) regression to support polynomial terms, allowing
 * for non-linear relationships in multivariate data.
 */
public class MultivariatePolynomialRegression {
    private final OLSMultipleLinearRegression regression; // OLS regression model from Apache Commons Math
    private final int degree;                             // Degree of the polynomial
    private final int numFeatures;                        // Number of original features (excluding polynomial terms)

    /**
     * Constructor to initialize and fit a polynomial regression model using OLS.
     *
     * @param X          2D array of input features (each row is an observation, each column a feature)
     * @param y          Array of target values corresponding to each observation
     * @param degree     Degree of the polynomial for each feature
     * @param numFeatures Number of original features in the dataset
     */
    public MultivariatePolynomialRegression(double[][] X, double[] y, int degree, int numFeatures) {
        this.degree = degree;
        this.numFeatures = numFeatures;
        this.regression = new OLSMultipleLinearRegression();

        // Generate polynomial features for each column in X
        double[][] polyX = createPolynomialFeatures(X);
        regression.newSampleData(y, polyX); // Fit the regression model with transformed polynomial features
    }

    /**
     * Creates polynomial terms for each feature up to the specified degree.
     *
     * @param X 2D array of original input features
     * @return  2D array of input features with polynomial terms added
     */
    private double[][] createPolynomialFeatures(double[][] X) {
        List<double[]> polyFeatures = new ArrayList<>();
        for (double[] row : X) {
            List<Double> polyRow = new ArrayList<>();

            // Add polynomial terms for each feature
            for (int i = 0; i < numFeatures; i++) {
                for (int d = 1; d <= degree; d++) {
                    polyRow.add(Math.pow(row[i], d));
                }
            }

            // Convert to array and add to the list of polynomial features
            polyFeatures.add(polyRow.stream().mapToDouble(Double::doubleValue).toArray());
        }
        return polyFeatures.toArray(new double[0][]);
    }

    /**
     * Predicts the target value for a new observation using the fitted model.
     *
     * @param x Array of input features for a single observation (length should match numFeatures)
     * @return  Predicted target value
     */
    public double predict(double[] x) {
        // Verify that the input length matches the expected number of features
        if (x.length != numFeatures) {
            throw new IllegalArgumentException("Incorrect number of input features: expected " + numFeatures + ", but received " + x.length);
        }

        // Create polynomial terms for the input row
        double[] polyX = new double[degree * numFeatures];
        int index = 0;
        for (int i = 0; i < numFeatures; i++) {
            for (int d = 1; d <= degree; d++) {
                polyX[index++] = Math.pow(x[i], d);
            }
        }

        // Get model coefficients from the regression object
        double[] coefficients = regression.estimateRegressionParameters();
        
        // Calculate prediction using intercept and coefficients
        double yPred = coefficients[0]; // intercept term
        for (int i = 0; i < polyX.length; i++) {
            yPred += coefficients[i + 1] * polyX[i];
        }
        return yPred;
    }
}
