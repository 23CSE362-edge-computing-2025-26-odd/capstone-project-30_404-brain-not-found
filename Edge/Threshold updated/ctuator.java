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
            switch (tuple.getTupleType()) {
                case "ALERT":
                    System.out.println("🚨 " + getName() + " >>> CRITICAL ALERT received!");
                    break;
                case "PKU_DATA":
                    System.out.println("📊 " + getName() + " received PKU data.");
                    break;
                case "LOW_BATTERY":
                    System.out.println("🔋 " + getName() + " received LOW BATTERY warning!");
                    break;
                default:
                    System.out.println(getName() + " received unknown tuple: " + tuple.getTupleType());
            }
        } else {
            super.processEvent(ev);
        }
    }
}
