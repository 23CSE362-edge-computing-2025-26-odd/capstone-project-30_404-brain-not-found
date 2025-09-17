package org.fog.test.perfeval;

import org.fog.application.Application;
import org.fog.entities.FogDevice;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementMapping;

import java.util.List;

public class ModulePlacements {

    public static void createModulePlacement(Application app, List<FogDevice> fogDevices) {

        ModuleMapping moduleMapping = ModuleMapping.createModuleMapping();

        // place edge-processor on edge devices
       // moduleMapping.addModuleToDevice("edge-processor", "edge-device-0");
       // moduleMapping.addModuleToDevice("edge-processor", "edge-device-1");
       // moduleMapping.addModuleToDevice("edge-processor", "edge-device-2");

        for(FogDevice edge : fogDevices){
            if(edge.getName().startsWith("edge-device")){
                moduleMapping.addModuleToDevice("edge-processor", edge.getName());
            }
        }
        // place fog-ml on fog-region-1
        moduleMapping.addModuleToDevice("fog-ml", "fog-region-1");

        // pass the fogDevices list to constructor
        ModulePlacementMapping placement = new ModulePlacementMapping(fogDevices, app, moduleMapping);
    }
}
