package org.fog.test.perfeval;

import org.cloudbus.cloudsim.core.CloudSim;
import org.fog.application.Application;
import org.fog.entities.Actuator;
import org.fog.entities.FogDevice;
import org.fog.entities.Sensor;
import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        try {
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            // Initialize CloudSim
            CloudSim.init(numUsers, calendar, traceFlag);

            // ---------------------------
            // 1. Build Fog Topology
            // ---------------------------
            List<FogDevice> fogDevices = new LinkedList<>();
            List<Sensor> sensors = new LinkedList<>();
            List<Actuator> actuators = new LinkedList<>();

            FogDevicesBuilder builder = new FogDevicesBuilder();
            FogDevice cloud = builder.createCloud("cloud");
            FogDevice fog = builder.createFogNode("fog-region-1", cloud.getId());
            fogDevices.add(cloud);
            fogDevices.add(fog);

            // ---------------------------
            // 2. Create Application
            // ---------------------------
            Application application = SmartPKUApplication.createApplication("smartpku-app", 1);

            // ---------------------------
            // 3. Create Edge Devices, Sensors, Actuators
            // ---------------------------
            for (int i = 0; i < 3; i++) {
                String edgeName = "edge-device-" + i;
                FogDevice edge = builder.createEdgeDevice(edgeName, fog.getId());
                fogDevices.add(edge);

                // Sensor attached to this edge
                PKUSensor sensor = new PKUSensor(edge.getName() + "-Sensor", edge.getId(), application);
                sensors.add(sensor);

                // Actuator attached to this edge
                PKUActuator actuator = new PKUActuator(
                        edge.getName() + "-Actuator",
                        1,
                        application.getAppId(),
                        edge.getId(),
                        5.0,
                        "PKU_ACTUATOR"
                );
                actuators.add(actuator);
            }

            // ---------------------------
            // 4. Module Placement
            // ---------------------------
            ModuleMapping moduleMapping = ModuleMapping.createModuleMapping();

            // Place edge-processor on each edge device
            for (FogDevice edge : fogDevices) {
                if (edge.getName().startsWith("edge-device")) {
                    moduleMapping.addModuleToDevice("edge-processor", edge.getName());
                }
            }

            // Place fog-ml module on fog-region-1
            moduleMapping.addModuleToDevice("fog-ml", "fog-region-1");

            // Apply module placement
            ModulePlacementHelper.createModulePlacement(application, fogDevices);

            // ---------------------------
            // 5. Create Controller
            // ---------------------------
            Controller controller = new Controller("master-controller", fogDevices, sensors, actuators);


            // ---------------------------
            // 6. Run Simulation
            // ---------------------------
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            System.out.println("Simulation finished");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
