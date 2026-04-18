package parkinglot.models.spots;

import parkinglot.constants.ParkingSpotType;

public class LargeSpot extends ParkingSpot {
    public LargeSpot(String number) {
        super(number, ParkingSpotType.LARGE);
    }
    // Large spots can accommodate all vehicle types - no override needed
}