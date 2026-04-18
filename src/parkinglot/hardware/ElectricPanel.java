package parkinglot.hardware;

import parkinglot.models.ParkingTicket;
import parkinglot.models.ParkingRate;
import parkinglot.payment.CreditCardTransaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Mounted at electric spots — allows EV customers to start/stop charging
 * and pay for both parking and charging.
 */
public class ElectricPanel {
    private String id;
    private int payedForMinutes;
    private LocalDateTime chargingStartTime;
    private boolean charging;

    // Charging rate: per-minute cost
    private static final double CHARGING_RATE_PER_MINUTE = 0.05; // $0.05/min = $3/hr

    public ElectricPanel(String id) {
        this.id = id;
        this.charging = false;
        this.payedForMinutes = 0;
    }

    public boolean startCharging(int requestedMinutes, String nameOnCard, String cardNumber) {
        if (charging) {
            System.out.println("[ElectricPanel " + id + "] Already charging.");
            return false;
        }
        double chargingFee = requestedMinutes * CHARGING_RATE_PER_MINUTE;
        System.out.printf("[ElectricPanel %s] Charging for %d minutes. Fee: $%.2f%n",
                id, requestedMinutes, chargingFee);

        CreditCardTransaction tx = new CreditCardTransaction(chargingFee, nameOnCard, cardNumber);
        boolean success = tx.initiateTransaction();
        if (success) {
            this.payedForMinutes = requestedMinutes;
            this.chargingStartTime = LocalDateTime.now();
            this.charging = true;
            System.out.println("[ElectricPanel " + id + "] Charging started.");
        }
        return success;
    }

    public boolean cancelCharging() {
        if (!charging) {
            System.out.println("[ElectricPanel " + id + "] No active charging session.");
            return false;
        }
        long minutesUsed = ChronoUnit.MINUTES.between(chargingStartTime, LocalDateTime.now());
        long minutesRemaining = payedForMinutes - minutesUsed;
        this.charging = false;
        System.out.printf("[ElectricPanel %s] Charging stopped. Used: %d min, Remaining credit: %d min%n",
                id, minutesUsed, Math.max(0, minutesRemaining));
        return true;
    }

    /**
     * Combined payment: parking fee + charging fee at exit.
     */
    public boolean processPayment(ParkingTicket ticket, ParkingRate parkingRate,
                                  String nameOnCard, String cardNumber) {
        if (ticket.isPaid()) {
            System.out.println("[ElectricPanel " + id + "] Ticket already paid.");
            return false;
        }
        long parkingMinutes = ticket.getParkingDurationMinutes();
        double parkingFee = parkingRate.calculateFee(parkingMinutes);
        long chargingMinutes = chargingStartTime != null
                ? Math.min(payedForMinutes,
                (int) ChronoUnit.MINUTES.between(chargingStartTime, LocalDateTime.now()))
                : 0;
        double totalFee = parkingFee; // Charging was pre-paid at start

        System.out.printf("[ElectricPanel %s] Parking fee: $%.2f | Total: $%.2f%n",
                id, parkingFee, totalFee);

        CreditCardTransaction tx = new CreditCardTransaction(totalFee, nameOnCard, cardNumber);
        boolean success = tx.initiateTransaction();
        if (success) ticket.markPaid(totalFee);
        return success;
    }

    public int getPayedForMinutes() { return payedForMinutes; }
    public LocalDateTime getChargingStartTime() { return chargingStartTime; }
    public boolean isCharging() { return charging; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return String.format("ElectricPanel{id='%s', charging=%b, payedForMinutes=%d}",
                id, charging, payedForMinutes);
    }
}