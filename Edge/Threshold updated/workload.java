package org.fog.test.perfeval;

import org.fog.entities.Tuple;

public class PKUWorkload {

    // Generate a tuple representing a moderate spike event
    public static Tuple moderateSpike(String srcModule, int sourceDeviceId) {
        Tuple t = new Tuple(
                "smartpku-app",     // appId
                "PKU_DATA",         // tupleType
                1,                  // cloudletId
                Tuple.UP,           // direction (upwards in hierarchy)
                sourceDeviceId,     // source device
                -1                  // destination unknown
        );
        t.setSrcModuleName(srcModule);
        return t;
    }

    // Generate a tuple representing a critical spike alert
    public static Tuple criticalSpike(String srcModule, int sourceDeviceId) {
        Tuple t = new Tuple(
                "smartpku-app",
                "ALERT",
                2,
                Tuple.UP,
                sourceDeviceId,
                -1
        );
        t.setSrcModuleName(srcModule);
        return t;
    }

    // Generate a tuple representing low battery alert
    public static Tuple lowBattery(String srcModule, int sourceDeviceId) {
        Tuple t = new Tuple(
                "smartpku-app",
                "LOW_BATTERY",
                3,
                Tuple.UP,
                sourceDeviceId,
                -1
        );
        t.setSrcModuleName(srcModule);
        return t;
    }
}
