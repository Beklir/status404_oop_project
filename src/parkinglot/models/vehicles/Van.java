package parkinglot.models.vehicles;

import parkinglot.constants.VehicleType;

public class Van extends Vehicle {
    public Van(String licenseNumber) {
        super(licenseNumber, VehicleType.VAN);
    }
}