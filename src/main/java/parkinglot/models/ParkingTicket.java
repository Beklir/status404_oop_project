package parkinglot.models;

import parkinglot.constants.ParkingTicketStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_tickets")
public class ParkingTicket {

    @Id
    private String ticketNumber;

    private LocalDateTime issuedAt;
    private LocalDateTime payedAt;
    private double payedAmount;

    @Enumerated(EnumType.STRING)
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
        return true;
    }

    public boolean markLost() {
        this.status = ParkingTicketStatus.LOST;
        return true;
    }

    @Transient // This tells JPA not to save this as a column (it's a helper method)
    public boolean isPaid() {
        return status == ParkingTicketStatus.PAID;
    }

    // Getters and Setters
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public LocalDateTime getPayedAt() { return payedAt; }
    public void setPayedAt(LocalDateTime payedAt) { this.payedAt = payedAt; }

    public double getPayedAmount() { return payedAmount; }
    public void setPayedAmount(double payedAmount) { this.payedAmount = payedAmount; }

    public ParkingTicketStatus getStatus() { return status; }
    public void setStatus(ParkingTicketStatus status) { this.status = status; }

    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }

    public String getVehicleLicense() { return vehicleLicense; }
    public void setVehicleLicense(String vehicleLicense) { this.vehicleLicense = vehicleLicense; }

}