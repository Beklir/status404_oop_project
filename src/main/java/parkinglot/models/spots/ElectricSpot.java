package parkinglot.models.spots;

import parkinglot.constants.ParkingSpotType;
import parkinglot.constants.VehicleType;
import parkinglot.models.vehicles.Vehicle;

public class ElectricSpot extends ParkingSpot {

    public ElectricSpot(String number) {
        super(number, ParkingSpotType.ELECTRIC);
    }

    @Override
    public boolean assignVehicle(Vehicle vehicle) {
        if (vehicle.getType() != VehicleType.ELECTRIC) {
            System.out.println("Electric spot is reserved for electric vehicles only. Received: " + vehicle.getType());
            return false;
        }
        return super.assignVehicle(vehicle);
    }
}