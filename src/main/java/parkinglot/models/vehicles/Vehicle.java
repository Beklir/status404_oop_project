package parkinglot.models.vehicles;

import parkinglot.constants.VehicleType;
import parkinglot.models.ParkingTicket;
import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_category")
public abstract class Vehicle {
    @Id
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    // Relationship to the Ticket
    // cascade = ALL means if you save the Vehicle, the ticket is saved too.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    private ParkingTicket ticket;

    protected Vehicle() {}

    public Vehicle(String licenseNumber, VehicleType type) {
        this.licenseNumber = licenseNumber;
        this.type = type;
    }

    public void assignTicket(ParkingTicket ticket) {
        this.ticket = ticket;
        System.out.println("Ticket " + ticket.getTicketNumber() + " assigned to vehicle " + licenseNumber);
    }

    // Getters and Setters
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public VehicleType getType() { return type; }

    public ParkingTicket getTicket() { return ticket; }
    public void setTicket(ParkingTicket ticket) { this.ticket = ticket; }
}