package parkinglot.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks hourly parking rates.
 * Example: $4 for hour 1, $3.5 for hours 2-3, $2.5 for remaining hours.
 */
public class ParkingRate {
    private String id;
    private Map<Integer, Double> hourlyRates; // hour number -> rate
    private double defaultRate;               // rate for hours beyond defined entries

    public ParkingRate(String id) {
        this.id = id;
        this.hourlyRates = new HashMap<>();
        // Default rates per the PDF specification
        this.hourlyRates.put(1, 4.0);
        this.hourlyRates.put(2, 3.5);
        this.hourlyRates.put(3, 3.5);
        this.defaultRate = 2.5;
    }

    /**
     * Calculate total fee for a given duration in minutes.
     */
    public double calculateFee(long durationMinutes) {
        if (durationMinutes <= 0) return 0.0;

        long totalHours = (long) Math.ceil(durationMinutes / 60.0);
        double totalFee = 0.0;

        for (long hour = 1; hour <= totalHours; hour++) {
            int hourKey = (int) Math.min(hour, Integer.MAX_VALUE);
            totalFee += hourlyRates.getOrDefault(hourKey, defaultRate);
        }
        return totalFee;
    }

    public void setHourRate(int hourNumber, double rate) {
        if (hourNumber < 1) throw new IllegalArgumentException("Hour number must be >= 1");
        if (rate < 0) throw new IllegalArgumentException("Rate cannot be negative");
        hourlyRates.put(hourNumber, rate);
    }

    public double getHourRate(int hourNumber) {
        return hourlyRates.getOrDefault(hourNumber, defaultRate);
    }

    public double getDefaultRate() { return defaultRate; }
    public void setDefaultRate(double defaultRate) {
        if (defaultRate < 0) throw new IllegalArgumentException("Rate cannot be negative");
        this.defaultRate = defaultRate;
    }

    public String getId() { return id; }

    @Override
    public String toString() {
        return String.format("ParkingRate{id='%s', rates=%s, defaultRate=%.2f}",
                id, hourlyRates, defaultRate);
    }
}