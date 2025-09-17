package org.fog.test.perfeval;

import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.entities.Actuator;
import org.fog.entities.Tuple;
import org.fog.utils.FogEvents;
import org.fog.utils.GeoLocation;

public class PKUActuator extends Actuator {

    public PKUActuator(String name, int userId, String appId, int gatewayDeviceId, double latency, String actuatorType) {
        super(name, userId, appId, gatewayDeviceId, latency, new GeoLocation(0,0), actuatorType, "");
    }

    @Override
    public void processEvent(SimEvent ev) {
        if(ev.getTag() == FogEvents.TUPLE_ARRIVAL) {
            Tuple tuple = (Tuple) ev.getData();
            if(tuple.getTupleType().equals("ALERT")) {
                System.out.println(getName() + " >>> CRITICAL ALERT received: " + tuple.getTupleType());
            } else {
                System.out.println(getName() + " received: " + tuple.getTupleType());
            }
        } else {
            super.processEvent(ev);
        }
    }
}

