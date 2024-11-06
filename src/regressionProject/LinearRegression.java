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
	
	public double meanSquaredError(double[] x, double[] y) {
        int n = x.length;
        double sumSquaredErrors = 0;
        for (int i = 0; i < n; i++) {
            double predictedY = predict(x[i]);
            sumSquaredErrors += Math.pow(y[i] - predictedY, 2);
        }
        return sumSquaredErrors / n;
    }
	
	public static void main(String[] args) {
	        // Exempeldata
	        double[] x = {1, 2, 3, 4, 5};  // Oberoende variabel
	        double[] y = {2, 4, 5, 4, 5};  // Beroende variabel

	        // Skapa ett LinearRegression-objekt
	        LinearRegression lr = new LinearRegression(x, y);

	        // Skriv ut lutning och intercept
	        System.out.println("Lutning (m): " + lr.m);
	        System.out.println("Intercept (b): " + lr.b);

	        // Testa att förutsäga ett nytt värde
	        double xNew = 6;
	        double prediction = lr.predict(xNew);
	        System.out.println("Förutsägelse för x = " + xNew + " är y = " + prediction);

	        // Beräkna och skriv ut Mean Squared Error (MSE)
	        double mse = lr.meanSquaredError(x, y);
	        System.out.println("Mean Squared Error: " + mse);
	}

	
}