//package org.fog.application;
package org.fog.test.perfeval;


import java.util.Random;

public class SmartPKUFogNode {
    private String name;

    public SmartPKUFogNode(String name) {
        this.name = name;
    }

    public void processDietarySuggestion(int value) {
        System.out.println("[" + name + "] Processing dietary suggestion for value: " + value);
    }

    public void receiveCriticalAlert(int value, SmartPKUFogNode backupNode, SmartPKUGateway gateway) {
        System.out.println("[" + name + "] CRITICAL alert received: " + value);

        if (isOverloaded()) {
            System.out.println("⚠️ [" + name + "] Overloaded! Rerouting to backup node...");
            backupNode.processCriticalAlert(value);
        } else {
            processCriticalAlert(value);
        }

        // Notify hospital through gateway
        gateway.forwardAlertToHospital(value);
    }

    public void processCriticalAlert(int value) {
        System.out.println("🚨 [" + name + "] PRIORITY ALERT processed for value: " + value);
    }

    private boolean isOverloaded() {
        // Dummy overload simulation (50% chance)
        return new Random().nextBoolean();
    }
}
