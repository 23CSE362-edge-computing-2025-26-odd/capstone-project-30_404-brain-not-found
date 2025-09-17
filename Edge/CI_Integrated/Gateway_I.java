package org.fog.application;

public class Gateway_I {
    private String name;

    public Gateway_I(String name) {
        this.name = name;
    }

    // For critical alerts
    public void forwardAlertToHospital(int value) {
        System.out.println("🏥 [" + name + "] Forwarded CRITICAL alert to Hospital for value: " + value);
    }

    // For advisory messages (diet, ML results, etc.)
    public void forwardAdviceToHospital(String msg) {
        System.out.println("🏥 [" + name + "] Forwarded ADVICE to Hospital: " + msg);
    }
}
