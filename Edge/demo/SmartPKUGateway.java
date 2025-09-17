//package org.fog.application;
package org.fog.test.perfeval;


public class SmartPKUGateway {
    private String name;

    public SmartPKUGateway(String name) {
        this.name = name;
    }

    public void forwardAlertToHospital(int value) {
        System.out.println("🏥 [" + name + "] Forwarded CRITICAL alert to Hospital for value: " + value);
    }
}

