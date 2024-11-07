package regressionProject;

public enum ActivationFunctions {
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
    LINEAR {
        @Override
        public double activate(double x) {
            return x;
        }

        @Override
        public double derivative(double x) {
            return 1;
        }
    };
	
    public abstract double activate(double x);
    public abstract double derivative(double x);
}
