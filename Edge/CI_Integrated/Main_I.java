package org.fog.test.perfeval;

import java.util.Scanner;

import org.fog.application.Edge_I;
import org.fog.application.Fog_I;
import org.fog.application.Gateway_I;

public class Main_I {
    public static void main(String[] args) {
        System.out.println("=== SMART PKU SIMULATION START ===");

        Scanner scanner = new Scanner(System.in);

        // Create Fog Nodes and Gateway
        Fog_I fogNode = new Fog_I("Hospital Fog Node");
        Fog_I backupNode = new Fog_I("Backup Fog Node");
        Gateway_I gateway = new Gateway_I("Gateway");

        // Ask for number of patients
        System.out.print("Enter number of patients: ");
        int patientCount = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Edge_I[] patients = new Edge_I[patientCount];

        // Get patient names
        for (int i = 0; i < patientCount; i++) {
            System.out.print("Enter name for Patient " + (i + 1) + ": ");
            String patientName = scanner.nextLine();
            patients[i] = new Edge_I(patientName);
        }

        // Sample sensor data
        int[][] sampleData = {
            {130, 230, 410, 90, 500},
            {50, 290, 310, 250, 450},
            {120, 80, 260, 30, 600}
        };

        // Run simulation
        for (int i = 0; i < patients.length; i++) {
            System.out.println("\n--- Processing Data for " + patients[i].getName() + " ---");
            for (int value : sampleData[i % sampleData.length]) {
                patients[i].processSensorData(value, fogNode, backupNode, gateway);
            }
        }

        System.out.println("\n=== SMART PKU SIMULATION END ===");
        scanner.close();
    }
}
