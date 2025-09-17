package org.fog.test.perfeval;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.entities.Tuple;
import org.fog.policy.AppModuleAllocationPolicy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PKUFogModule extends FogDevice {

    public PKUFogModule(
            String name,
            long mips,
            int ram,
            double uplinkBandwidth,
            double downlinkBandwidth,
            double ratePerMips,
            PowerModelLinear powerModel
    ) throws Exception {

        super(
                name,
                new FogDeviceCharacteristics(
                        "x86", "Linux", "Xen",
                        createHost(mips, ram, uplinkBandwidth, powerModel),
                        0.0, 3.0, 0.05, 0.001, 0.0
                ),
                new AppModuleAllocationPolicy(new ArrayList<Host>() {{
                    add(createHost(mips, ram, uplinkBandwidth, powerModel));
                }}),
                new LinkedList<Storage>(),
                10,
                uplinkBandwidth,
                downlinkBandwidth,
                0,
                ratePerMips
        );
    }

    private static Host createHost(long mips, int ram, double uplinkBandwidth, PowerModelLinear powerModel) {
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));

        return new PowerHost(
                0,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple((long) uplinkBandwidth),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList),
                powerModel
        );
    }

    @Override
    public void processTupleArrival(SimEvent ev) {
        try {
            Tuple tuple = (Tuple) ev.getData();

            switch (tuple.getTupleType()) {
                case "PKU_DATA":
                    double pkuValue = tuple.getCloudletLength();
                    System.out.println("\n📥 FogModule (" + getName() +
                            ") received PKU_DATA with value = " + pkuValue);

                    String classification = Fuzzy1.classify(pkuValue);
                    String diet = DietPSO1.getSuggestion(pkuValue);
                    String risk = SimpleNN1.predictRisk(pkuValue);

                    System.out.println("🔎 Classification: " + classification);
                    System.out.println("🥗 Dietary Suggestion: " + diet);
                    System.out.println("⚠ Risk Prediction: " + risk);

                    if (classification.equals("HIGH")) {
                        System.out.println("🚨 ALERT: High PKU detected!");
                    }
                    break;

                case "ALERT":
                    System.out.println("🚨 CRITICAL ALERT received at FogModule " + getName());
                    break;

                case "LOW_BATTERY":
                    System.out.println("🔋 Low Battery Alert from " + tuple.getSrcModuleName());
                    break;

                default:
                    System.out.println("ℹ Unknown tuple type: " + tuple.getTupleType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.processTupleArrival(ev);
    }
}
