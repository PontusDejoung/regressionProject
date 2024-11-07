# Polynomial and Neural Network Regression Models in Java

This project provides implementations of regression models (linear, polynomial) and a simple feed-forward neural network in Java. These models are designed to handle both univariate and multivariate data, support feature transformations, date interval conversions, and generate predictions for time series data. 

## Project Structure

The main Java files are located in the `regressionProject/src/regressionProject` directory:

- `DateConverter.java` - Converts dates to intervals for time series analysis.
- `DataLoader.java` - Loads and preprocesses data from a CSV file.
- `LinearRegression.java` - Implements simple linear regression.
- `PolynomialRegression.java` - Implements polynomial regression for univariate data.
- `MultivariatePolynomialRegression.java` - Extends polynomial regression to multiple input features.
- `ActivationFunctions.java` - Defines activation functions used in neural network models.
- `NeuralNetwork.java` - Implements a simple feed-forward neural network for regression.
- `Main.java` - Main class to run a regression model and generate predictions.
- `MainForNeuralNetwork.java` - Main class to demonstrate training and predicting with the neural network.

## Requirements

- **Java 8 or above**
- **Apache Commons Math** library for OLS regression:
  - Add the dependency to your project or include the library in the classpath.

  Maven dependency:
  ```xml
  <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-math3</artifactId>
      <version>3.6.1</version>
  </dependency>

  <dependency>
      <groupId>tech.tablesaw</groupId>
      <artifactId>tablesaw-core</artifactId>
      <version>0.38.1</version>
  </dependency>

