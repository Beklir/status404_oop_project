package parkinglot.models.spots;

import parkinglot.constants.ParkingSpotType;
import parkinglot.constants.VehicleType;
import parkinglot.models.vehicles.Vehicle;

public class MotorbikeSpot extends ParkingSpot {
    public MotorbikeSpot(String number) {
        super(number, ParkingSpotType.MOTORBIKE);
    }

    @Override
    public boolean assignVehicle(Vehicle vehicle) {
        if (vehicle.getType() != VehicleType.MOTORBIKE) {
            System.out.println("Motorbike spot only accepts motorcycles. Received: " + vehicle.getType());
            return false;
        }
        return super.assignVehicle(vehicle);
    }
}