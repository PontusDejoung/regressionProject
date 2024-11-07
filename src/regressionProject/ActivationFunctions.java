package regressionProject;

/**
 * ActivationFunctions - Enum defining activation functions commonly used in neural networks.
 * Each enum constant represents a specific activation function and its derivative,
 * which is essential for backpropagation in training.
 */
public enum ActivationFunctions {

    /**
     * ReLU (Rectified Linear Unit) activation function.
     * Returns the input if it is greater than 0, otherwise returns 0.
     * Commonly used in hidden layers of neural networks for its simplicity
     * and effectiveness in handling non-linearities.
     */
    RELU {
        @Override
        public double activate(double x) {
            return Math.max(0, x);
        }

        @Override
        public double derivative(double x) {
            return x > 0 ? 1 : 0;
        }
    },

    /**
     * Sigmoid activation function.
     * Squashes the input to a range between 0 and 1, which makes it useful for output layers
     * in binary classification tasks. However, it can suffer from vanishing gradients.
     */
    SIGMOID {
        @Override
        public double activate(double x) {
            return 1 / (1 + Math.exp(-x));
        }

        @Override
        public double derivative(double x) {
            double sigmoid = activate(x);
            return sigmoid * (1 - sigmoid);
        }
    },

    /**
     * Linear activation function.
     * Outputs the input directly. Often used in the output layer of a network
     * for regression tasks where continuous output is required.
     */
    LINEAR {
        @Override
        public double activate(double x) {
            return x;
        }

        @Override
        public double derivative(double x) {
            return 1; // Derivative of a linear function is constant 1
        }
    };

    /**
     * Abstract method for activation function.
     * Each activation function must implement this method.
     *
     * @param x Input value for the activation function.
     * @return  Activated output.
     */
    public abstract double activate(double x);

    /**
     * Abstract method for activation function derivative.
     * Each activation function must implement this method for backpropagation.
     *
     * @param x Input value for which derivative is computed.
     * @return  Derivative value of the activation function at x.
     */
    public abstract double derivative(double x);
}
