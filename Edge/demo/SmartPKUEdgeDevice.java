//package org.fog.application;
/*package org.fog.test.perfeval;


public class SmartPKUEdgeDevice {
    private String name;
    private int batteryLevel = 100; // Dummy battery percentage

    public SmartPKUEdgeDevice(String name) {
        this.name = name;
    }

    public void processSensorData(int value, SmartPKUFogNode fogNode, SmartPKUFogNode backupNode, SmartPKUGateway gateway) {
        System.out.println("\n[" + name + "] Sensor reading: " + value);

        // Battery check
        batteryLevel -= 10;
        if (batteryLevel < 30) {
            System.out.println("⚠️ Low Battery Alert from " + name);
        }

        if (value > SmartPKUHelper.THRESHOLD_2) {
            System.out.println("🚨 Level 2 Threshold crossed! Sending ALERT to Hospital + Fog");
            fogNode.receiveCriticalAlert(value, backupNode, gateway);
        } else if (value > SmartPKUHelper.THRESHOLD_1) {
            System.out.println("⚠️ Level 1 Threshold crossed! Sending to Fog Node for dietary suggestion");
            fogNode.processDietarySuggestion(value);
        } else {
            System.out.println("✅ Normal Value. Storing locally & will send at night.");
        }
    }
}*/

package org.fog.test.perfeval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmartPKUEdgeDevice {
    private String name;
    private int batteryLevel = 60; // Dummy battery percentage

    public SmartPKUEdgeDevice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void processSensorData(int value, SmartPKUFogNode fogNode, SmartPKUFogNode backupNode, SmartPKUGateway gateway) {
        System.out.println("\n[" + name + "] Sensor reading: " + value);

        List<task> taskQueue = new ArrayList<>();

        // Add tasks to queue based on priority
        if (value > SmartPKUHelper.THRESHOLD_2) {
            taskQueue.add(new task(task.Type.CRITICAL, value, 1));
        }
        if (value > SmartPKUHelper.THRESHOLD_1) {
            taskQueue.add(new task(task.Type.DIETARY, value, 2));
        }
        if (batteryLevel < 30) {
            taskQueue.add(new task(task.Type.BATTERY, value, 3));
        }

        // Sort tasks by priority (lower number = higher priority)
        Collections.sort(taskQueue);

        // Process tasks in priority order
        for (task t : taskQueue) {
            switch (t.type) {
                case CRITICAL:
                    System.out.println("🚨 [HIGH PRIORITY TASK] Critical Alert Processing...");
                    fogNode.receiveCriticalAlert(t.value, backupNode, gateway);
                    break;

                case DIETARY:
                    System.out.println("⚠️ [MODERATE PRIORITY TASK] Dietary Suggestion Processing...");
                    fogNode.processDietarySuggestion(t.value);
                    break;

                case BATTERY:
                    System.out.println("🔋 [LOW PRIORITY TASK] Low Battery Alert Processing...");
                    System.out.println("⚠️ Low Battery Alert from " + name);
                    break;
            }
        }

        batteryLevel -= 10;
    }
}

