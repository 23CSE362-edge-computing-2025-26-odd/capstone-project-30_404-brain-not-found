import java.util.Random;
public class SimpleNN {
    private int inputNodes, hiddenNodes, outputNodes;
    private double[][] weightsInputHidden, weightsHiddenOutput;
    private double[] hiddenBias, outputBias;
    private double learningRate = 0.01;
    private Random rand = new Random();

    // Constructor
    public SimpleNN(int inputNodes, int hiddenNodes, int outputNodes) {
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;

        // Randomly initialize weights and biases
        weightsInputHidden = new double[inputNodes][hiddenNodes];
        weightsHiddenOutput = new double[hiddenNodes][outputNodes];
        hiddenBias = new double[hiddenNodes];
        outputBias = new double[outputNodes];

        for (int i = 0; i < inputNodes; i++)
            for (int j = 0; j < hiddenNodes; j++)
                weightsInputHidden[i][j] = rand.nextGaussian() * 0.01;

        for (int i = 0; i < hiddenNodes; i++)
            for (int j = 0; j < outputNodes; j++)
                weightsHiddenOutput[i][j] = rand.nextGaussian() * 0.01;
    }

    // Sigmoid activation
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }

    // Forward pass
    public double[] predict(double[] input) {
        double[] hidden = new double[hiddenNodes];
        double[] output = new double[outputNodes];

        // Input → Hidden
        for (int j = 0; j < hiddenNodes; j++) {
            double sum = hiddenBias[j];
            for (int i = 0; i < inputNodes; i++) {
                sum += input[i] * weightsInputHidden[i][j];
            }
            hidden[j] = sigmoid(sum);
        }

        // Hidden → Output
        for (int k = 0; k < outputNodes; k++) {
            double sum = outputBias[k];
            for (int j = 0; j < hiddenNodes; j++) {
                sum += hidden[j] * weightsHiddenOutput[j][k];
            }
            output[k] = sigmoid(sum);
        }

        return output;
    }

    // Train with backpropagation
    public void train(double[] input, double[] target) {
        // Forward pass
        double[] hidden = new double[hiddenNodes];
        double[] output = new double[outputNodes];

        for (int j = 0; j < hiddenNodes; j++) {
            double sum = hiddenBias[j];
            for (int i = 0; i < inputNodes; i++) {
                sum += input[i] * weightsInputHidden[i][j];
            }
            hidden[j] = sigmoid(sum);
        }

        for (int k = 0; k < outputNodes; k++) {
            double sum = outputBias[k];
            for (int j = 0; j < hiddenNodes; j++) {
                sum += hidden[j] * weightsHiddenOutput[j][k];
            }
            output[k] = sigmoid(sum);
        }

        // Calculate output error
        double[] outputError = new double[outputNodes];
        for (int k = 0; k < outputNodes; k++) {
            outputError[k] = target[k] - output[k];
        }

        // Backpropagate error to hidden layer
        double[] hiddenError = new double[hiddenNodes];
        for (int j = 0; j < hiddenNodes; j++) {
            double sum = 0;
            for (int k = 0; k < outputNodes; k++) {
                sum += outputError[k] * weightsHiddenOutput[j][k];
            }
            hiddenError[j] = sum;
        }

        // Update weights Hidden → Output
        for (int j = 0; j < hiddenNodes; j++) {
            for (int k = 0; k < outputNodes; k++) {
                double delta = outputError[k] * sigmoidDerivative(output[k]) * hidden[j];
                weightsHiddenOutput[j][k] += learningRate * delta;
            }
        }

        // Update weights Input → Hidden
        for (int i = 0; i < inputNodes; i++) {
            for (int j = 0; j < hiddenNodes; j++) {
                double delta = hiddenError[j] * sigmoidDerivative(hidden[j]) * input[i];
                weightsInputHidden[i][j] += learningRate * delta;
            }
        }
    }

    // Example usage
    public static void main(String[] args) {
        SimpleNN nn = new SimpleNN(3, 5, 1); // 3 inputs, 5 hidden, 1 output

        // Example: input = [phe, tyr, hoursSinceMeal], output = risk (0-1)
        double[] input = {450, 60, 3};
        double[] prediction = nn.predict(input);

        System.out.println("Predicted risk: " + prediction[0]);
    }
}

