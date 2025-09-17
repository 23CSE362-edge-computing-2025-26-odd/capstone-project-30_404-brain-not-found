package org.fog.test.perfeval;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.application.Application;
import org.fog.entities.Tuple;
import org.fog.utils.FogEvents;
import org.fog.utils.GeoLocation;
import org.fog.utils.distribution.DeterministicDistribution;

import java.util.Random;

public class PKUSensor extends org.fog.entities.Sensor {

    private Random rand = new Random();
    private static final double LOW_THRESHOLD = 0.8;   // moderate level
    private static final double HIGH_THRESHOLD = 1.2;  // critical level

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
        // Generate a random PKU reading between 0.5 and 1.5
        double pkuValue = 0.5 + rand.nextDouble();

        System.out.println("PKU Sensor " + getName() + " reading: " + pkuValue);

        Tuple tupleToSend;

        // ✅ Apply two-threshold logic
        if (pkuValue > HIGH_THRESHOLD) {
            System.out.println(" CRITICAL ALERT triggered at " + getName());
            tupleToSend = PKUWorkload.criticalSpike("PKU_SENSOR", getId());
        } else if (pkuValue > LOW_THRESHOLD) {
            System.out.println(" MODERATE ALERT triggered at " + getName());
            tupleToSend = PKUWorkload.moderateSpike("PKU_SENSOR", getId());
        } else {
            // Normal data tuple
            tupleToSend = new Tuple(
                    getAppId(),
                    "PKU_DATA",
                    CloudSim.getEntityId(getName()),
                    Tuple.UP,
                    getId(),
                    getGatewayDeviceId()
            );
            tupleToSend.setSrcModuleName("PKU_SENSOR");
            tupleToSend.setDestModuleName("edge-processor");
        }

        // Send tuple to the gateway (edge processor)
        send(getGatewayDeviceId(), getLatency(), FogEvents.TUPLE_ARRIVAL, tupleToSend);

        // Schedule next reading
        send(getId(), getTransmitDistribution().getNextValue(), FogEvents.TUPLE_ARRIVAL);
    }
}
