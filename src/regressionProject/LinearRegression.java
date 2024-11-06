package regressionProject;

public class LinearRegression {
	private double m; // Gradient
	private double b; // Intercept
	
	public LinearRegression(double[] x, double[] y) {
		int n = x.length;
		double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
		
		for (int i = 0; i < n; i++) {
			sumX += x[i];
			sumY += y[i];
			sumXY += x[i] * y[i];
			sumX2 += x[i] * x[i];
		}
		// Calculate gradient (m) and intercept (b)
		m =(n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX); 
		b = (sumY - m * sumX) / n;
	}
	
	public double predict(double x) {
		return m * x + b;
	}
	
	public double getIntercept() {
		return b;
	}
	
	public double getSlope() {
		return m;
	}
	
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