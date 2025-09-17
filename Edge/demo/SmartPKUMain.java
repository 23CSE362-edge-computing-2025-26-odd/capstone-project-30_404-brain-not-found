//package org.fog.application;
/*package org.fog.test.perfeval;


public class SmartPKUMain {
    public static void main(String[] args) {
        System.out.println("=== SMART PKU SIMULATION START ===");

        // Create Edge Device
        SmartPKUEdgeDevice edgeDevice = new SmartPKUEdgeDevice("Patient-1 Edge");
        SmartPKUFogNode fogNode = new SmartPKUFogNode("Hospital Fog Node");
        SmartPKUFogNode backupNode = new SmartPKUFogNode("Backup Fog Node");
        SmartPKUGateway gateway = new SmartPKUGateway("Gateway");

        // Simulate incoming sensor data
        int[] sampleData = {150, 230, 410, 90, 500}; // Example PKU sensor values
        for (int value : sampleData) {
            edgeDevice.processSensorData(value, fogNode, backupNode, gateway);
        }

        System.out.println("=== SMART PKU SIMULATION END ===");
    }
}
*/



package org.fog.test.perfeval;

import java.util.Scanner;

public class SmartPKUMain {
    public static void main(String[] args) {
        System.out.println("=== SMART PKU SIMULATION START ===");

        Scanner scanner = new Scanner(System.in);

        // Create Fog Nodes and Gateway
        SmartPKUFogNode fogNode = new SmartPKUFogNode("Hospital Fog Node");
        SmartPKUFogNode backupNode = new SmartPKUFogNode("Backup Fog Node");
        SmartPKUGateway gateway = new SmartPKUGateway("Gateway");

        // Ask for number of patients
        System.out.print("Enter number of patients: ");
        int patientCount = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        SmartPKUEdgeDevice[] patients = new SmartPKUEdgeDevice[patientCount];

        // Get patient names from user
        for (int i = 0; i < patientCount; i++) {
            System.out.print("Enter name for Patient " + (i + 1) + ": ");
            String patientName = scanner.nextLine();
            patients[i] = new SmartPKUEdgeDevice(patientName);
        }

        // Sample sensor data for each patient
        int[][] sampleData = {
            {150, 230, 410, 90, 500},
            {100, 290, 310, 250, 450},
            {120, 80, 260, 350, 600}
        };

        // Run simulation for each patient
        for (int i = 0; i < patients.length; i++) {
            System.out.println("\n--- Processing Data for " + patients[i].getName() + " ---");
            for (int value : sampleData[i % sampleData.length]) {  // Cycle sampleData if fewer than patientCount
                patients[i].processSensorData(value, fogNode, backupNode, gateway);
            }
        }

        System.out.println("\n=== SMART PKU SIMULATION END ===");
        scanner.close();
    }
}


