package parkinglot.models.spots;

import parkinglot.constants.ParkingSpotType;
import parkinglot.models.vehicles.Vehicle;

public abstract class ParkingSpot {
    private String number;
    private boolean free;
    private ParkingSpotType type;
    private Vehicle currentVehicle;

    public ParkingSpot(String number, ParkingSpotType type) {
        this.number = number;
        this.type = type;
        this.free = true;
        this.currentVehicle = null;
    }

    public boolean isFree() { return free; }

    public boolean assignVehicle(Vehicle vehicle) {
        if (!free) {
            System.out.println("Spot " + number + " is already occupied.");
            return false;
        }
        this.currentVehicle = vehicle;
        this.free = false;
        System.out.println("Vehicle " + vehicle.getLicenseNumber() + " assigned to spot " + number);
        return true;
    }

    public boolean removeVehicle() {
        if (free) {
            System.out.println("Spot " + number + " is already empty.");
            return false;
        }
        System.out.println("Vehicle " + currentVehicle.getLicenseNumber() + " removed from spot " + number);
        this.currentVehicle = null;
        this.free = true;
        return true;
    }

    // Getters and Setters
    public boolean getIsFree() { return free; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public ParkingSpotType getType() { return type; }

    public Vehicle getCurrentVehicle() { return currentVehicle; }

}