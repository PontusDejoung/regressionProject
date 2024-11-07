package regressionProject;

import java.util.Random;
import java.util.List;

/**
 * NeuralNetwork - A simple feed-forward neural network implementation for regression.
 * Supports multiple hidden layers, configurable activation functions, and mean squared error (MSE) tracking.
 * Designed for training on time series or tabular data with backpropagation and gradient descent.
 */
public class NeuralNetwork {
    private int[] layers;                  // Array defining the number of neurons per layer
    private double[][] neurons;             // Activations of each neuron per layer
    private double[][][] weights;           // Weights between layers
    private double[][] biases;              // Bias terms for each neuron
    private List<ActivationFunctions> activations; // Activation function for each layer

    /**
     * Constructor to initialize the neural network with specified layers and activation functions.
     *
     * @param layers      Array defining the number of neurons per layer
     * @param activations List of activation functions for each layer (excluding input layer)
     */
    public NeuralNetwork(int[] layers, List<ActivationFunctions> activations) {
        if (activations.size() != layers.length - 1) {
            throw new IllegalArgumentException("Number of activation functions must match number of layers (excluding input layer).");
        }

        this.layers = layers;
        this.activations = activations;
        this.neurons = new double[layers.length][];
        this.weights = new double[layers.length - 1][][];
        this.biases = new double[layers.length - 1][];

        Random rand = new Random();
        for (int i = 0; i < layers.length; i++) {
            neurons[i] = new double[layers[i]];
            if (i < layers.length - 1) {
                weights[i] = new double[layers[i]][layers[i + 1]];
                biases[i] = new double[layers[i + 1]];

                // Initialize weights and biases with small random values
                for (int j = 0; j < layers[i]; j++) {
                    for (int k = 0; k < layers[i + 1]; k++) {
                        weights[i][j][k] = rand.nextDouble() * 0.002 - 0.001; // Small initial values
                    }
                }
                for (int j = 0; j < layers[i + 1]; j++) {
                    biases[i][j] = rand.nextDouble() * 0.002 - 0.001;
                }
            }
        }
    }

    /**
     * Feed-forward pass to compute network output for given input data.
     *
     * @param input Array of input values for the network
     * @return      Array of output values from the network
     */
    public double[] feedForward(double[] input) {
        neurons[0] = input;

        for (int i = 1; i < layers.length; i++) {
            ActivationFunctions activation = activations.get(i - 1);
            for (int j = 0; j < layers[i]; j++) {
                double sum = biases[i - 1][j];
                for (int k = 0; k < layers[i - 1]; k++) {
                    sum += neurons[i - 1][k] * weights[i - 1][k][j];
                }

                // Check for NaN values to avoid instability
                if (Double.isNaN(sum)) {
                    System.out.println("NaN detected in feed-forward pass at layer " + i);
                }

                neurons[i][j] = activation.activate(sum);
            }
        }
        return neurons[layers.length - 1];
    }

    /**
     * Backpropagation algorithm to update weights and biases based on error.
     *
     * @param target       Array of target values
     * @param learningRate Learning rate for weight updates
     */
    public void backpropagate(double[] target, double learningRate) {
        double[][] deltas = new double[layers.length][];
        for (int i = 0; i < layers.length; i++) {
            deltas[i] = new double[layers[i]];
        }

        // Calculate output layer error
        for (int i = 0; i < layers[layers.length - 1]; i++) {
            double error = neurons[layers.length - 1][i] - target[i];
            deltas[layers.length - 1][i] = error;
        }

        // Backpropagate errors through hidden layers
        for (int i = layers.length - 2; i > 0; i--) {
            ActivationFunctions activation = activations.get(i - 1);
            for (int j = 0; j < layers[i]; j++) {
                double error = 0;
                for (int k = 0; k < layers[i + 1]; k++) {
                    error += deltas[i + 1][k] * weights[i][j][k];
                }
                deltas[i][j] = error * activation.derivative(neurons[i][j]);
            }
        }

        // Update weights and biases
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] -= learningRate * deltas[i + 1][k] * neurons[i][j];
                }
            }
            for (int j = 0; j < biases[i].length; j++) {
                biases[i][j] -= learningRate * deltas[i + 1][j];
            }
        }
    }

    /**
     * Training loop to train the neural network over multiple epochs.
     * Calculates Mean Squared Error (MSE) after each epoch and prints training progress.
     *
     * @param inputs       2D array of input data
     * @param targets      2D array of target values
     * @param epochs       Number of training epochs
     * @param learningRate Learning rate for weight updates
     */
    public void train(double[][] inputs, double[][] targets, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            double totalError = 0;

            for (int i = 0; i < inputs.length; i++) {
                double[] output = feedForward(inputs[i]);
                backpropagate(targets[i], learningRate);

                // Calculate Mean Squared Error (MSE) for each prediction
                double mse = 0;
                for (int j = 0; j < output.length; j++) {
                    mse += Math.pow(output[j] - targets[i][j], 2);
                }
                totalError += mse / output.length;
            }

            // Calculate average MSE for the epoch
            double meanSquaredError = totalError / inputs.length;

            // Print progress and MSE every 10 epochs
            if (epoch % 10 == 0 || epoch == epochs - 1) {
                double progress = (double) (epoch + 1) / epochs * 100;
                int barLength = 30;
                int filledLength = (int) (barLength * progress / 100);
                StringBuilder bar = new StringBuilder();
                for (int j = 0; j < barLength; j++) {
                    bar.append(j < filledLength ? "#" : "-");
                }
                System.out.printf("\rEpoch %d/%d [%s] %.2f%% complete - MSE: %.6f", epoch + 1, epochs, bar, progress, meanSquaredError);
                System.out.flush();
            }
        }
        System.out.println("\nTraining complete!");
    }
}
