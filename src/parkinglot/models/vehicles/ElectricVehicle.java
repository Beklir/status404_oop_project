package parkinglot.models.vehicles;

import parkinglot.constants.VehicleType;

public class ElectricVehicle extends Vehicle {
    public ElectricVehicle(String licenseNumber) {
        super(licenseNumber, VehicleType.ELECTRIC);
    }
}