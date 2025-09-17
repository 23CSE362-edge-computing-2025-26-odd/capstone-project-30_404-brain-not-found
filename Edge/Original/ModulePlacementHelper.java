package org.fog.test.perfeval;

import org.fog.application.Application;
import org.fog.entities.FogDevice;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementMapping;

import java.util.List;

public class ModulePlacementHelper {

    public static void createModulePlacement(Application app, List<FogDevice> fogDevices) {
        // 1. Create module mapping
        ModuleMapping mapping = ModuleMapping.createModuleMapping();

        // 2. Place modules explicitly
        for (FogDevice dev : fogDevices) {
            String name = dev.getName();

            if (name.startsWith("edge-device")) {
                mapping.addModuleToDevice("edge-processor", name);
            } else if (name.startsWith("fog-region")) {
                mapping.addModuleToDevice("fog-ml", name);
            }
        }

        // 3. Create ModulePlacementMapping
        ModulePlacementMapping placement = new ModulePlacementMapping(fogDevices, app, mapping);
        mapping.addModuleToDevice("fog-ml", "fog-region-1");


        // ✅ Optional: keep a reference somewhere if needed
        // But usually the Controller will use it internally
    }
}
