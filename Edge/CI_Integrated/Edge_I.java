package org.fog.application;

import org.fog.test.perfeval.Fuzzy1;

public class Edge_I {
    private String name;
    private int batteryLevel = 60; // Dummy battery percentage
    private Fuzzy1 fuzzy;          // Fuzzy logic engine

    public Edge_I(String name) {
        this.name = name;
        this.fuzzy = new Fuzzy1();
    }

    public String getName() {
        return name;
    }

    public void processSensorData(int value, Fog_I fogNode, Fog_I backupNode, Gateway_I gateway) {
        System.out.println("\n[" + name + "] Sensor reading: " + value);

        // === FUZZY LOGIC DECISION ===
        String fuzzyResult = fuzzy.classify(value);
        System.out.println("[EDGE][FUZZY] Classification result: " + fuzzyResult);

        // Route decision based on fuzzy category
        if (fuzzyResult.startsWith("Normal")) {
            System.out.println(" [EDGE] Normal value. Storing locally & will send later.");
        }
        else if (fuzzyResult.startsWith("Slightly High")) {
            System.out.println(" [EDGE] Slightly High → Sending to Fog for ML inference...");
            fogNode.runMLInference(value, this, gateway);   // new ML inference hook
        }
        else if (fuzzyResult.startsWith("Dangerous")) {
            System.out.println(" [EDGE] Dangerous → Sending CRITICAL ALERT to Fog!");
            fogNode.receiveCriticalAlert(value, backupNode, gateway);
        }

        // Battery check
        batteryLevel -= 10;
        if (batteryLevel < 30) {
            System.out.println(" [EDGE] Low Battery Alert from " + name);
        }
    }
}

	
