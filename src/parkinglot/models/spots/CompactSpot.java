package parkinglot.models.spots;

import parkinglot.constants.ParkingSpotType;
import parkinglot.constants.VehicleType;
import parkinglot.models.vehicles.Vehicle;

public class CompactSpot extends ParkingSpot {
    public CompactSpot(String number) {
        super(number, ParkingSpotType.COMPACT);
    }

    @Override
    public boolean assignVehicle(Vehicle vehicle) {
        if (vehicle.getType() == VehicleType.TRUCK || vehicle.getType() == VehicleType.VAN) {
            System.out.println("Compact spot cannot accommodate " + vehicle.getType());
            return false;
        }
        return super.assignVehicle(vehicle);
    }
}