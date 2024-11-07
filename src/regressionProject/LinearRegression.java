package regressionProject;

/**
 * LinearRegression - A simple linear regression model for finding the best fit line (y = mx + b)
 * for a set of data points. This model is trained using the least squares method.
 */
public class LinearRegression {
    private double m; // Slope (gradient) of the regression line
    private double b; // Intercept of the regression line

    /**
     * Constructor to initialize and train the linear regression model using input data.
     *
     * @param x Array of x-values (independent variable)
     * @param y Array of y-values (dependent variable)
     */
    public LinearRegression(double[] x, double[] y) {
        int n = x.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        
        // Calculate sums needed for the least squares method
        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
        }

        // Calculate slope (m) and intercept (b) using the least squares formulas
        m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX); 
        b = (sumY - m * sumX) / n;
    }

    /**
     * Predicts the y-value for a given x-value using the regression line.
     *
     * @param x Input x-value
     * @return  Predicted y-value
     */
    public double predict(double x) {
        return m * x + b;
    }

    /**
     * Gets the intercept (b) of the regression line.
     *
     * @return Intercept of the line
     */
    public double getIntercept() {
        return b;
    }

    /**
     * Gets the slope (m) of the regression line.
     *
     * @return Slope of the line
     */
    public double getSlope() {
        return m;
    }

    /**
     * Calculates the Mean Squared Error (MSE) of the model on the provided dataset.
     *
     * @param x Array of x-values (independent variable)
     * @param y Array of y-values (dependent variable)
     * @return  Mean Squared Error between actual and predicted y-values
     */
    public double meanSquaredError(double[] x, double[] y) {
        int n = x.length;
        double sumSquaredErrors = 0;
        for (int i = 0; i < n; i++) {
            double predictedY = predict(x[i]);
            sumSquaredErrors += Math.pow(y[i] - predictedY, 2);
        }
        return sumSquaredErrors / n;
    }
}
