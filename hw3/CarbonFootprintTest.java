import java.util.*;

interface CarbonFootprint {
    double getCFP();
}

// type 1：building
class Building implements CarbonFootprint {
    private double elec_use; // unit: kWh

    public Building(double elec_use) {
        this.elec_use = elec_use;
    }

    @Override
    public double getCFP() {
        return elec_use * 0.474; // 1 kWh electricity emits 0.474 kg CO2
    }

    @Override
    public String toString() {
        return "Building using " + elec_use + " kWh electricity";
    }
}

// type 2：car
class Car implements CarbonFootprint {
    private double fuel_liters; // unit: liters

    public Car(double fuel_liters) {
        this.fuel_liters = fuel_liters;
    }

    @Override
    public double getCFP() {
        return fuel_liters * 2.3; // 1 liter gasoline emits 2.3 kg CO2
    }

    @Override
    public String toString() {
        return "Car using " + fuel_liters + " liters of gasoline";
    }
}

// type 3：bicycle
class Bicycle implements CarbonFootprint {
    private boolean is_elec;
    private double dis;

    public Bicycle(boolean is_elec, double dis) {
        this.is_elec = is_elec;
        this.dis = dis;
    }

    @Override
    public double getCFP() {
        return is_elec ? dis * 0.003 : 0.0; // electric bicycle emits 0.003 kg CO2 per km, regular bicycle emits 0
    }

    @Override
    public String toString() {
        return (is_elec ? "Electric" : "Regular") + " bicycle ridden " + dis + " km";
    }
}

// main class to test the CarbonFootprint interface and its implementations
public class CarbonFootprintTest {
    public static void main(String[] args) {
        ArrayList<CarbonFootprint> list = new ArrayList<>();

        list.add(new Building(2500));             // 2500 kWh electricity
        list.add(new Car(40));                    // 40 liters of gasoline
        list.add(new Bicycle(true, 15));          // electric bicycle ridden 15 km
        list.add(new Bicycle(false, 15));         // regular bicycle ridden 15 km

        for (CarbonFootprint item : list) {
            System.out.println(item.toString());
            System.out.printf("Carbon Footprint: %.3f kg CO2\n\n", item.getCFP());
        }
    }
}
