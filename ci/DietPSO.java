package ci;

import java.util.*;

public class DietPSO {
    // Food item representation
    static class FoodItem {
        String name;
        double phe;      // phenylalanine content (mg)
        double calories; // calorie content (kcal)

        FoodItem(String name, double phe, double calories) {
            this.name = name;
            this.phe = phe;
            this.calories = calories;
        }
    }

    // Particle representation
    static class Particle {
        double[] position;     // quantities of each food
        double[] velocity;
        double[] bestPosition;
        double bestFitness;

        Particle(int foodCount) {
            position = new double[foodCount];
            velocity = new double[foodCount];
            bestPosition = new double[foodCount];
        }
    }

    // PSO core
    public static void runPSO(List<FoodItem> foods, double calorieRequirement, int swarmSize, int maxIter) {
        int foodCount = foods.size();
        Random rand = new Random();

        // Initialize swarm
        List<Particle> swarm = new ArrayList<>();
        for (int i = 0; i < swarmSize; i++) {
            Particle p = new Particle(foodCount);
            for (int j = 0; j < foodCount; j++) {
                p.position[j] = rand.nextDouble() * 5; // max 5 servings
                p.velocity[j] = rand.nextDouble();
                p.bestPosition[j] = p.position[j];
            }
            p.bestFitness = fitness(p.position, foods, calorieRequirement);
            swarm.add(p);
        }

        // Global best
        double[] gBestPosition = swarm.get(0).bestPosition.clone();
        double gBestFitness = swarm.get(0).bestFitness;

        // Main loop
        for (int iter = 0; iter < maxIter; iter++) {
            for (Particle p : swarm) {
                double fit = fitness(p.position, foods, calorieRequirement);

                // Update personal best
                if (fit < p.bestFitness) {
                    p.bestFitness = fit;
                    p.bestPosition = p.position.clone();
                }

                // Update global best
                if (fit < gBestFitness) {
                    gBestFitness = fit;
                    gBestPosition = p.position.clone();
                }
            }

            // Update positions and velocities
            for (Particle p : swarm) {
                for (int j = 0; j < foodCount; j++) {
                    double w = 0.7, c1 = 1.5, c2 = 1.5;
                    double r1 = rand.nextDouble(), r2 = rand.nextDouble();

                    p.velocity[j] = w * p.velocity[j] +
                                    c1 * r1 * (p.bestPosition[j] - p.position[j]) +
                                    c2 * r2 * (gBestPosition[j] - p.position[j]);

                    p.position[j] += p.velocity[j];

                    // Clamp to [0,5] servings
                    if (p.position[j] < 0) p.position[j] = 0;
                    if (p.position[j] > 5) p.position[j] = 5;
                }
            }
        }

        // Print optimized result in human-readable form
        System.out.println("=== Optimized Diet Recommendations ===");
        for (int j = 0; j < foods.size(); j++) {
            double servings = gBestPosition[j];
            String advice;

            if (servings > 4) {
                advice = "Take more " + foods.get(j).name;
            } else if (servings > 2) {
                advice = "Include moderate " + foods.get(j).name;
            } else if (servings > 0) {
                advice = "Take less amount of " + foods.get(j).name;
            } else {
                advice = "Avoid " + foods.get(j).name;
            }

            System.out.println(advice + " (" + String.format("%.2f", servings) + " servings)");
        }
        System.out.println("Best Fitness (Total Phenylalanine intake): " + gBestFitness);
    }

    // Fitness function
    static double fitness(double[] servings, List<FoodItem> foods, double calorieRequirement) {
        double totalPhe = 0, totalCalories = 0;
        for (int i = 0; i < foods.size(); i++) {
            totalPhe += servings[i] * foods.get(i).phe;
            totalCalories += servings[i] * foods.get(i).calories;
        }

        // Penalize if calorie requirement not met
        if (totalCalories < calorieRequirement) {
            return Double.MAX_VALUE;
        }

        // Fitness = minimize Phe intake
        return totalPhe;
    }

    // Example usage
    public static void main(String[] args) {
        List<FoodItem> foods = Arrays.asList(
            new FoodItem("Rice", 50, 200),
            new FoodItem("Milk", 120, 150),
            new FoodItem("Fruit", 30, 100)
        );

        runPSO(foods, 2000, 30, 100); // 2000 kcal requirement, 30 particles, 100 iterations
    }
}
