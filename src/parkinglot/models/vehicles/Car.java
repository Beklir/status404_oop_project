package parkinglot.models.vehicles;

import parkinglot.constants.VehicleType;

public class Car extends Vehicle {
    public Car(String licenseNumber) {
        super(licenseNumber, VehicleType.CAR);
    }
}