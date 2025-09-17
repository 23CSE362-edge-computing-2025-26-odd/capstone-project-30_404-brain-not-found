package org.fog.test.perfeval;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.application.Application;
import org.fog.entities.Sensor;
import org.fog.entities.Tuple;
import org.fog.utils.FogEvents;
import org.fog.utils.GeoLocation;
import org.fog.utils.distribution.DeterministicDistribution;

import java.util.Random;

public class PKUSensor extends Sensor {

    private Random rand = new Random();

    public PKUSensor(String name, int gatewayDeviceId, Application app) {
        super(name, 0, app.getAppId(), gatewayDeviceId, 1.0,
              new GeoLocation(0, 0), new DeterministicDistribution(5), "PKU_SENSOR");
        setApp(app); // attach the actual Application
    }

    @Override
    public void startEntity() {
        super.startEntity();
        // Schedule the first tuple
        send(getId(), getTransmitDistribution().getNextValue(), FogEvents.TUPLE_ARRIVAL);
    }

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {
            case FogEvents.TUPLE_ARRIVAL:
                generateAndSendTuple();
                break;
            default:
                super.processEvent(ev);
        }
    }

    private void generateAndSendTuple() {
        // Generate a random PKU reading between 0.5 and 1.5 (example)
        double pkuValue = 0.5 + rand.nextDouble();

        // Create a custom Tuple using your Tuple class
        Tuple tuple = new Tuple(
                getAppId(),
                "PKU_DATA",
                CloudSim.getEntityId(getName()),
                Tuple.UP,
                getId(),
                getGatewayDeviceId()
        );
        tuple.setCloudletLength((long) pkuValue);  
        tuple.setSrcModuleName("PKU_SENSOR");
        tuple.setDestModuleName("edge-processor");

        // Store sensor value in the moduleCopyMap (or another field)
        tuple.getModuleCopyMap().put("PKU_VALUE", (int)(pkuValue * 1000)); // example

        System.out.println("PKU Sensor " + getName() + " reading: " + pkuValue);

        // Send the tuple to the gateway (edge processor)
        send(getGatewayDeviceId(), getLatency(), FogEvents.TUPLE_ARRIVAL, tuple);

        // Schedule next reading
        send(getId(), getTransmitDistribution().getNextValue(), FogEvents.TUPLE_ARRIVAL);
    }
}
