package parkinglot.models.vehicles;

import parkinglot.constants.VehicleType;
import parkinglot.models.ParkingTicket;

public abstract class Vehicle {
    private String licenseNumber;
    private VehicleType type;
    private ParkingTicket ticket;

    public Vehicle(String licenseNumber, VehicleType type) {
        this.licenseNumber = licenseNumber;
        this.type = type;
    }

    public void assignTicket(ParkingTicket ticket) {
        this.ticket = ticket;
        System.out.println("Ticket " + ticket.getTicketNumber() + " assigned to vehicle " + licenseNumber);
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public VehicleType getType() { return type; }

    public ParkingTicket getTicket() { return ticket; }
    public void setTicket(ParkingTicket ticket) { this.ticket = ticket; }

    @Override
    public String toString() {
        return String.format("%s{license='%s'}", getClass().getSimpleName(), licenseNumber);
    }
}