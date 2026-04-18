package parkinglot.models;

import parkinglot.constants.ParkingTicketStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ParkingTicket {
    private String ticketNumber;
    private LocalDateTime issuedAt;
    private LocalDateTime payedAt;
    private double payedAmount;
    private ParkingTicketStatus status;
    private String spotNumber;
    private String vehicleLicense;

    public ParkingTicket() {
        this.ticketNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.issuedAt = LocalDateTime.now();
        this.status = ParkingTicketStatus.ACTIVE;
        this.payedAmount = 0.0;
    }

    public ParkingTicket(String vehicleLicense, String spotNumber) {
        this();
        this.vehicleLicense = vehicleLicense;
        this.spotNumber = spotNumber;
    }

    /**
     * Returns duration parked in minutes.
     */
    public long getParkingDurationMinutes() {
        LocalDateTime end = (payedAt != null) ? payedAt : LocalDateTime.now();
        return ChronoUnit.MINUTES.between(issuedAt, end);
    }

    public boolean markPaid(double amount) {
        if (status == ParkingTicketStatus.PAID) {
            System.out.println("Ticket " + ticketNumber + " is already paid.");
            return false;
        }
        this.payedAmount = amount;
        this.payedAt = LocalDateTime.now();
        this.status = ParkingTicketStatus.PAID;
        System.out.printf("Ticket %s marked as PAID. Amount: $%.2f%n", ticketNumber, amount);
        return true;
    }

    public boolean markLost() {
        this.status = ParkingTicketStatus.LOST;
        System.out.println("Ticket " + ticketNumber + " marked as LOST.");
        return true;
    }

    public boolean isPaid() { return status == ParkingTicketStatus.PAID; }

    // Getters and Setters
    public String getTicketNumber() { return ticketNumber; }

    public LocalDateTime getIssuedAt() { return issuedAt; }

    public LocalDateTime getPayedAt() { return payedAt; }

    public double getPayedAmount() { return payedAmount; }

    public ParkingTicketStatus getStatus() { return status; }
    public void setStatus(ParkingTicketStatus status) { this.status = status; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public String getVehicleLicense() { return vehicleLicense; }
    public void setVehicleLicense(String vehicleLicense) { this.vehicleLicense = vehicleLicense; }

    @Override
    public String toString() {
        return String.format(
                "ParkingTicket{number='%s', vehicle='%s', spot='%s', issuedAt=%s, status=%s, duration=%d min}",
                ticketNumber, vehicleLicense, spotNumber, issuedAt, status, getParkingDurationMinutes()
        );
    }
}