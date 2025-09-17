package org.fog.test.perfeval;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.policy.AppModuleAllocationPolicy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FogDevicesBuilder {

    private FogDevice createFogDeviceBase(String name, long mips, int ram, long bw, PowerModelLinear pm) throws Exception {

        // 1. Create PEs
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));

        // 2. Create host
        PowerHost host = new PowerHost(
                0,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                1000000, // storage
                peList,
                new VmSchedulerTimeShared(peList),
                pm
        );

        // 3. Create FogDeviceCharacteristics (single host)
        FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
                "x86",           // architecture
                "Linux",         // OS
                "Xen",           // VMM
                host,            // single host
                0.0,             // time zone
                3.0,             // cost/sec
                0.05,            // cost/mem
                0.001,           // cost/storage
                0.0              // cost/BW
        );

        // 4. Create VmAllocationPolicy using the host
        List<Host> hostList = new ArrayList<>();
        hostList.add(host);
        AppModuleAllocationPolicy allocationPolicy = new AppModuleAllocationPolicy(hostList);

        // 5. Create FogDevice
        FogDevice device = new FogDevice(
                name,
                characteristics,
                allocationPolicy,
                new LinkedList<>(), // empty storage
                0,                 // scheduling interval
                bw,                // uplink bandwidth
                bw,                // downlink bandwidth
                1,                 // uplink latency
                0.01               // ratePerMips
        );

        return device;
    }

    // Cloud
    public FogDevice createCloud(String name) throws Exception {
        PowerModelLinear pm = new PowerModelLinear(200, 100);
        return createFogDeviceBase(name, 50000, 40000, 10000, pm);
    }

    // Fog node
    public FogDevice createFogNode(String name, int parentId) throws Exception {
        PowerModelLinear pm = new PowerModelLinear(150, 100);
        FogDevice fog = createFogDeviceBase(name, 10000, 8000, 10000, pm);
        fog.setParentId(parentId);
        return fog;
    }

    // Edge device
    public FogDevice createEdgeDevice(String name, int parentId) throws Exception {
        PowerModelLinear pm = new PowerModelLinear(100, 75);
        FogDevice edge = createFogDeviceBase(name, 2000, 2000, 1000, pm);
        edge.setParentId(parentId);
        return edge;
    }
}
