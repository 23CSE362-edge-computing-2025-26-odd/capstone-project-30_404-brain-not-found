public class Fuzzy {
    // Membership function for "Normal"
    private double normal(double phe) {
        if (phe <= 200) return 1.0;           // fully normal
        else if (phe > 200 && phe < 400)     // decreasing normal
            return (400 - phe) / 200.0;
        else return 0.0;
    }

    // Membership function for "Slightly High"
    private double slightlyHigh(double phe) {
        if (phe >= 300 && phe <= 500) {
            if (phe <= 400)
                return (phe - 300) / 100.0; // rising slope
            else
                return (500 - phe) / 100.0; // falling slope
        }
        return 0.0;
    }

    // Membership function for "High/Dangerous"
    private double high(double phe) {
        if (phe >= 600 && phe <= 800) {
            return (phe - 600) / 200.0;  // rising
        } else if (phe > 800) {
            return 1.0;                  // fully dangerous
        }
        return 0.0;
    }

    // Inference: decide fuzzy risk category
    public String classify(double phe) {
        double normalVal = normal(phe);
        double slightVal = slightlyHigh(phe);
        double highVal = high(phe);

        // pick the label with highest membership degree
        if (highVal >= slightVal && highVal >= normalVal) {
            return "Dangerous (" + highVal + ")";
        } else if (slightVal >= normalVal) {
            return "Slightly High (" + slightVal + ")";
        } else {
            return "Normal (" + normalVal + ")";
        }
    }

    // Example usage
    public static void main(String[] args) {
        FuzzyPheMonitor monitor = new FuzzyPheMonitor();

        double[] sampleReadings = {150, 350, 450, 700, 900};

        for (double phe : sampleReadings) {
            System.out.println("Phe: " + phe + " µmol/L => " + monitor.classify(phe));
        }
    }
}

