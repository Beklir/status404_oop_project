package parkinglot.models;

import parkinglot.constants.ParkingSpotType;
import parkinglot.constants.VehicleType;
import parkinglot.hardware.ParkingDisplayBoard;
import parkinglot.models.spots.*;
import parkinglot.models.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingFloor {
    private String name;
    private List<ParkingSpot> spots;
    private ParkingDisplayBoard displayBoard;

    public ParkingFloor(String name) {
        this.name = name;
        this.spots = new ArrayList<>();
        this.displayBoard = new ParkingDisplayBoard("DB-" + name);
    }

    public void updateDisplayBoard() {
        long freeHandicapped = spots.stream()
                .filter(s -> s.getType() == ParkingSpotType.HANDICAPPED && s.isFree()).count();
        long freeCompact = spots.stream()
                .filter(s -> s.getType() == ParkingSpotType.COMPACT && s.isFree()).count();
        long freeLarge = spots.stream()
                .filter(s -> s.getType() == ParkingSpotType.LARGE && s.isFree()).count();
        long freeMotorbike = spots.stream()
                .filter(s -> s.getType() == ParkingSpotType.MOTORBIKE && s.isFree()).count();
        long freeElectric = spots.stream()
                .filter(s -> s.getType() == ParkingSpotType.ELECTRIC && s.isFree()).count();

        displayBoard.updateFreeCounts(
                (int) freeHandicapped, (int) freeCompact,
                (int) freeLarge, (int) freeMotorbike, (int) freeElectric
        );
    }

    public void addParkingSlot(ParkingSpot spot) {
        spots.add(spot);
        updateDisplayBoard();
        System.out.println("Spot " + spot.getNumber() + " (" + spot.getType() + ") added to floor " + name);
    }

    /**
     * Assigns the first suitable free spot to the vehicle.
     * Returns the assigned spot or null if none available.
     */
    public ParkingSpot assignVehicleToSlot(Vehicle vehicle) {
        ParkingSpotType required = getRequiredSpotType(vehicle.getType());

        ParkingSpot spot = spots.stream()
                .filter(s -> s.getType() == required && s.isFree())
                .findFirst()
                .orElse(null);

        if (spot == null) {
            System.out.println("No free " + required + " spot available on floor " + name);
            return null;
        }

        boolean assigned = spot.assignVehicle(vehicle);
        if (assigned) {
            updateDisplayBoard();
            return spot;
        }
        return null;
    }

    /**
     * Frees the spot occupied by a vehicle.
     */
    public boolean freeSlot(ParkingSpot spot) {
        boolean removed = spot.removeVehicle();
        if (removed) updateDisplayBoard();
        return removed;
    }

    private ParkingSpotType getRequiredSpotType(VehicleType vehicleType) {
        switch (vehicleType) {
            case MOTORBIKE: return ParkingSpotType.MOTORBIKE;
            case ELECTRIC:  return ParkingSpotType.ELECTRIC;
            case TRUCK:
            case VAN:       return ParkingSpotType.LARGE;
            default:        return ParkingSpotType.COMPACT;
        }
    }

    public void showDisplayBoard() {
        updateDisplayBoard();
        displayBoard.showEmptySpotNumber();
    }

    public boolean hasAvailableSpot(ParkingSpotType type) {
        return spots.stream().anyMatch(s -> s.getType() == type && s.isFree());
    }

    public int getTotalSpots() { return spots.size(); }

    public long getFreeSpotCount() {
        return spots.stream().filter(ParkingSpot::isFree).count();
    }

    public List<ParkingSpot> getSpots() { return spots; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ParkingDisplayBoard getDisplayBoard() { return displayBoard; }

    @Override
    public String toString() {
        return String.format("ParkingFloor{name='%s', totalSpots=%d, freeSpots=%d}",
                name, getTotalSpots(), getFreeSpotCount());
    }
}