package parkinglot.models;

import java.util.HashMap;
import java.util.Map;

public class ParkingRate {
    private double rate = 0.25;

    public double calculateFee(long durationMinutes) {
        if (durationMinutes <= 0) return 0.0;
        return  rate * durationMinutes;
    }

}