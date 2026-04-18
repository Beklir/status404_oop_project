package parkinglot.hardware;

import parkinglot.models.ParkingTicket;
import parkinglot.models.vehicles.Vehicle;

public class EntrancePanel {
    private String id;

    public EntrancePanel(String id) {
        this.id = id;
    }

    /**
     * Issues a new parking ticket for an arriving vehicle.
     * Returns null if the lot is full (caller must check isFull first).
     */
    public ParkingTicket printTicket(Vehicle vehicle, String assignedSpotNumber) {
        ParkingTicket ticket = new ParkingTicket(vehicle.getLicenseNumber(), assignedSpotNumber);
        vehicle.assignTicket(ticket);
        System.out.println("[EntrancePanel " + id + "] Printed ticket: " + ticket.getTicketNumber()
                + " for vehicle: " + vehicle.getLicenseNumber()
                + " -> Spot: " + assignedSpotNumber);
        return ticket;
    }

    public void showFullMessage() {
        System.out.println("[EntrancePanel " + id + "] *** PARKING LOT IS FULL. No entry allowed. ***");
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return "EntrancePanel{id='" + id + "'}";
    }
}