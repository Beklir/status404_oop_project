package parkinglot.hardware;

import parkinglot.models.ParkingTicket;
import parkinglot.models.ParkingRate;
import parkinglot.payment.CashTransaction;
import parkinglot.payment.CreditCardTransaction;


public class CustomerInfoPortal {
    private String id;

    public CustomerInfoPortal(String id) {
        this.id = id;
    }

    public boolean scanTicket(ParkingTicket ticket, ParkingRate rate) {
        if (ticket == null) {
            System.out.println("[CustomerInfoPortal " + id + "] No ticket provided.");
            return false;
        }
        long durationMinutes = ticket.getParkingDurationMinutes();
        double fee = rate.calculateFee(durationMinutes);
        System.out.printf("[CustomerInfoPortal %s] Ticket: %s | Parked: %d min | Fee: $%.2f%n",
                id, ticket.getTicketNumber(), durationMinutes, fee);
        return true;
    }

//    Cash Payment
    public boolean processPayment(ParkingTicket ticket, ParkingRate rate, double cashTendered) {
        if (ticket.isPaid()) {
            System.out.println("[CustomerInfoPortal " + id + "] Ticket already paid.");
            return false;
        }
        long durationMinutes = ticket.getParkingDurationMinutes();
        double fee = rate.calculateFee(durationMinutes);

        CashTransaction tx = new CashTransaction(fee, cashTendered);
        boolean success = tx.initiateTransaction();
        if (success) {
            ticket.markPaid(fee);
            System.out.println("[CustomerInfoPortal " + id
                    + "] Payment recorded. Proceed to exit within 15 minutes.");
        }
        return success;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

}