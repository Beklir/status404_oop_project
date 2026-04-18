package parkinglot.models.vehicles;

import parkinglot.constants.VehicleType;

public class Motorbike extends Vehicle {
    public Motorbike(String licenseNumber) {
        super(licenseNumber, VehicleType.MOTORBIKE);
    }
}