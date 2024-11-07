package regressionProject;

import java.util.Random;
import java.util.List;

public class NeuralNetwork {
    private int[] layers;
    private double[][] neurons;
    private double[][][] weights;
    private double[][] biases;
    private List<ActivationFunctions> activations;

    public NeuralNetwork(int[] layers, List<ActivationFunctions> activations) {
        if (activations.size() != layers.length - 1) {
            throw new IllegalArgumentException("Antalet aktiveringsfunktioner måste matcha antalet lager (exkl. ingångslagret).");
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

                for (int j = 0; j < layers[i]; j++) {
                    for (int k = 0; k < layers[i + 1]; k++) {
                        weights[i][j][k] = rand.nextDouble() * 0.002 - 0.001; // Mycket små initiala värden
                    }
                }
                for (int j = 0; j < layers[i + 1]; j++) {
                    biases[i][j] = rand.nextDouble() * 0.002 - 0.001;
                }
            }
        }
    }

    public double[] feedForward(double[] input) {
        neurons[0] = input;
        for (int i = 1; i < layers.length; i++) {
        	ActivationFunctions activation = activations.get(i - 1);
            for (int j = 0; j < layers[i]; j++) {
                double sum = biases[i - 1][j];
                for (int k = 0; k < layers[i - 1]; k++) {
                    sum += neurons[i - 1][k] * weights[i - 1][k][j];
                }
                if (Double.isNaN(sum)) {
                    System.out.println("NaN hittades i framåtpasseringen på lager " + i);      
                }
                neurons[i][j] = activation.activate(sum); 
            }
        }
        return neurons[layers.length - 1];
    }

    public void backpropagate(double[] target, double learningRate) {
        double[][] deltas = new double[layers.length][];
        for (int i = 0; i < layers.length; i++) {
            deltas[i] = new double[layers[i]];
        }

        for (int i = 0; i < layers[layers.length - 1]; i++) {
            double error = neurons[layers.length - 1][i] - target[i];
            deltas[layers.length - 1][i] = error;
        }

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

    public void train(double[][] inputs, double[][] targets, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            double totalError = 0;

            for (int i = 0; i < inputs.length; i++) {
                double[] output = feedForward(inputs[i]);
                backpropagate(targets[i], learningRate);

                // Beräkna kvadratiskt fel för denna förutsägelse
                double mse = 0;
                for (int j = 0; j < output.length; j++) {
                    mse += Math.pow(output[j] - targets[i][j], 2);
                }
                totalError += mse / output.length; // Lägger till fel för varje dataexempel
            }

            // Genomsnittligt fel (MSE) för hela datasetet under denna epok
            double meanSquaredError = totalError / inputs.length;

            // Utskrift av progress och MSE
            if (epoch % 10 == 0 || epoch == epochs - 1) {
                double progress = (double) (epoch + 1) / epochs * 100;
                int barLength = 30;
                int filledLength = (int) (barLength * progress / 100);
                StringBuilder bar = new StringBuilder();
                for (int j = 0; j < barLength; j++) {
                    bar.append(j < filledLength ? "#" : "-");
                }
                System.out.printf("\rEpoch %d/%d [%s] %.2f%% färdigt - MSE: %.6f", epoch + 1, epochs, bar, progress, meanSquaredError);
                System.out.flush();
            }
        }
        System.out.println("\nTräning färdig!");
    }
}

