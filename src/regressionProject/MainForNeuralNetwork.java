package regressionProject;

import java.io.IOException;
import java.util.List;
import tech.tablesaw.api.Table;
import java.util.Arrays;

/**
 * MainForNeuralNetwork - A class to run and train a neural network on time series data.
 * This class uses DataLoader to load data from a CSV file, trains a neural network with
 * specified layers and activation functions, and prints predictions.
 */
public class MainForNeuralNetwork {
    public static void main(String[] args) {
        try {
            // 1. Define start and end dates to handle time-based data
            String startDate = "2024-01-01 00:00:00+01:00";
            String endDate = "2024-10-31 23:30:00+01:00";
            DateConverter dateConverter = new DateConverter(startDate, endDate);

            // 2. Define the columns to use from the data file
            String filePath = "wulf.csv"; // CSV file with time series data
            String dateColumn = "timestamp"; // Column for timestamps
            String targetColumn = "close"; // Column for target value (e.g., stock close price)
            List<String> featureColumns = Arrays.asList("intervals_since_start", "volume"); // Define features to use

            // 3. Use DataLoader to load and prepare data from the CSV file
            DataLoader dataLoader = new DataLoader(filePath, dateColumn, targetColumn, featureColumns, dateConverter);
            Table stockData = dataLoader.loadData(); // Loads and processes data from the file

            // 4. Extract input and target variables from the loaded table
            double[][] inputs = dataLoader.getFeatureValues(stockData); // Fetch input features
            double[] targetValues = dataLoader.getTargetValues(stockData); // Fetch target variables (closing price)

            // 5. Convert target values to a 2D array format required by the network
            double[][] targets = new double[targetValues.length][1];
            for (int i = 0; i < targetValues.length; i++) {
                targets[i][0] = targetValues[i];
            }

            // 6. Create and configure the neural network architecture
            // Example architecture with two hidden layers (64 neurons each) and a linear output for regression
            int[] layers = {inputs[0].length, 64, 64, 1};
            List<ActivationFunctions> activations = Arrays.asList(
                    ActivationFunctions.RELU,   // Activation for the first hidden layer
                    ActivationFunctions.RELU,   // Activation for the second hidden layer
                    ActivationFunctions.LINEAR  // Linear activation for output layer (good for regression)
            );

            // 7. Create an instance of NeuralNetwork with the specified architecture and activations
            NeuralNetwork nn = new NeuralNetwork(layers, activations);

            // 8. Train the network with a specified number of epochs and learning rate
            int epochs = 10000; // Number of training cycles (epochs)
            double learningRate = 0.001; // Learning rate
            nn.train(inputs, targets, epochs, learningRate);

            // 9. Test the network by running forward pass on the same inputs to get predictions
            System.out.println("Predictions:");
            for (int i = 0; i < inputs.length; i++) {
                double[] output = nn.feedForward(inputs[i]);
                System.out.println("Actual value: " + targets[i][0] + " -> Predicted value: " + output[0]);
            }

        } catch (IOException e) {
            // Handle data loading errors
            e.printStackTrace();
        }
    }
}
