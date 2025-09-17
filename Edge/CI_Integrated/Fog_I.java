package org.fog.application;

import java.util.*;
import org.fog.test.perfeval.DietPSO1;
import org.fog.test.perfeval.SimpleNN1;

public class Fog_I {
    private String name;
    private SimpleNN1 nn;   // small neural network

    public Fog_I(String name) {
        this.name = name;
        this.nn = new SimpleNN1(3, 5, 1);

        // (Optional) dummy training for demonstration
        for (int i = 0; i < 100; i++) {
            double[] input = {0.4, 0.2, 0.1};   // dummy features
            double[] target = {0.8};           // high risk target
            nn.train(input, target);
        }
    }

    // Fog node dietary suggestion (via PSO)
    public void processDietarySuggestion(int pheValue) {
        System.out.println("[" + name + "] Processing dietary suggestion for Phe value: " + pheValue);

        // Base calorie requirement + some adjustment based on Phe
        double calorieRequirement = 2000 + pheValue;  

        List<DietPSO1.FoodItem> foods = Arrays.asList(
            new DietPSO1.FoodItem("Rice", 50, 200),
            new DietPSO1.FoodItem("Milk", 120, 150),
            new DietPSO1.FoodItem("Fruit", 30, 100),
            new DietPSO1.FoodItem("Vegetables", 20, 50)
        );

        // Call PSO with this dynamic calorie requirement
        DietPSO1.runPSO(foods, calorieRequirement, 30, 100);  // same swarm & iterations as original
    }



    // Fog node ML inference path
    public void runMLInference(int value, Edge_I edge, Gateway_I gateway) {
        System.out.println("[" + name + "] Running ML inference (NN) for value: " + value);

        double phe = value / 1000.0;       // normalized phenylalanine
        double battery = 0.5;              // dummy battery (50%)
        double timeOfDay = 0.25;           // dummy time encoding
        double[] input = {phe, battery, timeOfDay};

        double[] output = nn.predict(input);
        double risk = output[0];

        System.out.println("[" + name + "] NN Predicted Risk: " + String.format("%.3f", risk));

        if (risk > 0.2) {
            System.out.println("⚠️ [" + name + "] High risk detected! Triggering PSO dietary recommendation...");
            processDietarySuggestion(value);
            gateway.forwardAdviceToHospital("High risk diet plan generated for " + edge.getName());
        } else {
            System.out.println("✅ [" + name + "] Risk low. Monitoring only.");
        }
    }

    // Fog node critical alert
    public void receiveCriticalAlert(int value, Fog_I backupNode, Gateway_I gateway) {
        System.out.println("[" + name + "] CRITICAL alert received: " + value);

        if (isOverloaded()) {
            System.out.println("[" + name + "] Overloaded! Rerouting to backup node...");
            backupNode.processCriticalAlert(value);
        } else {
            processCriticalAlert(value);
        }

        // Notify hospital through gateway
        gateway.forwardAlertToHospital(value);
    }

    public void processCriticalAlert(int value) {
        System.out.println("[" + name + "] PRIORITY ALERT processed for value: " + value);
    }

    private boolean isOverloaded() {
        return new Random().nextBoolean();
    }
}
