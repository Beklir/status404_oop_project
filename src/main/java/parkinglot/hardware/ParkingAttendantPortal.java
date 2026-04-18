package parkinglot.hardware;

import parkinglot.models.ParkingTicket;
import parkinglot.models.ParkingRate;
import parkinglot.payment.CashTransaction;
import parkinglot.payment.CreditCardTransaction;

public class ParkingAttendantPortal {
    private String id;

    public ParkingAttendantPortal(String id) {
        this.id = id;
    }

    public double scanTicket(ParkingTicket ticket, ParkingRate rate) {
        if (ticket == null) {
            System.out.println("[AttendantPortal " + id + "] Invalid ticket.");
            return -1;
        }
        long durationMinutes = ticket.getParkingDurationMinutes();
        double fee = rate.calculateFee(durationMinutes);
        System.out.printf("[AttendantPortal %s] Ticket: %s | Duration: %d min | Fee: $%.2f | Status: %s%n",
                id, ticket.getTicketNumber(), durationMinutes, fee, ticket.getStatus());
        return fee;
    }

//    Cash Payment
    public boolean processPayment(ParkingTicket ticket, ParkingRate rate, double cashTendered) {
        if (ticket.isPaid()) {
            System.out.println("[AttendantPortal " + id + "] Ticket already paid.");
            return false;
        }
        double fee = scanTicket(ticket, rate);
        if (fee < 0) return false;

        CashTransaction tx = new CashTransaction(fee, cashTendered);
        boolean success = tx.initiateTransaction();
        if (success) ticket.markPaid(fee);
        return success;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

}