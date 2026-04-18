package parkinglot.models.spots;

import parkinglot.constants.ParkingSpotType;

public class HandicappedSpot extends ParkingSpot {
    public HandicappedSpot(String number) {
        super(number, ParkingSpotType.HANDICAPPED);
    }
}