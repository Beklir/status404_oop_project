package parkinglot.hardware;

import parkinglot.models.ParkingTicket;
import parkinglot.models.ParkingRate;
import parkinglot.payment.CashTransaction;
import parkinglot.payment.CreditCardTransaction;

public class ExitPanel {
    private String id;

    public ExitPanel(String id) {
        this.id = id;
    }

    /**
     * Scans a ticket and returns the fee due (0 if already paid).
     */
    public double scanTicket(ParkingTicket ticket, ParkingRate rate) {
        if (ticket == null) {
            System.out.println("[ExitPanel " + id + "] Invalid ticket.");
            return -1;
        }
        if (ticket.isPaid()) {
            System.out.println("[ExitPanel " + id + "] Ticket " + ticket.getTicketNumber()
                    + " already paid. Opening gate.");
            return 0;
        }
        long durationMinutes = ticket.getParkingDurationMinutes();
        double fee = rate.calculateFee(durationMinutes);
        System.out.printf("[ExitPanel %s] Ticket: %s | Duration: %d min | Fee Due: $%.2f%n",
                id, ticket.getTicketNumber(), durationMinutes, fee);
        return fee;
    }

    /**
     * Process credit card payment at the exit panel.
     */
    public boolean processPayment(ParkingTicket ticket, ParkingRate rate,
                                  String nameOnCard, String cardNumber) {
        double fee = scanTicket(ticket, rate);
        if (fee < 0) return false;
        if (fee == 0) return true; // already paid

        CreditCardTransaction tx = new CreditCardTransaction(fee, nameOnCard, cardNumber);
        boolean success = tx.initiateTransaction();
        if (success) {
            ticket.markPaid(fee);
            openGate();
        }
        return success;
    }

    /**
     * Process cash payment at the exit panel.
     */
    public boolean processPayment(ParkingTicket ticket, ParkingRate rate, double cashTendered) {
        double fee = scanTicket(ticket, rate);
        if (fee < 0) return false;
        if (fee == 0) { openGate(); return true; }

        CashTransaction tx = new CashTransaction(fee, cashTendered);
        boolean success = tx.initiateTransaction();
        if (success) {
            ticket.markPaid(fee);
            openGate();
        }
        return success;
    }

    private void openGate() {
        System.out.println("[ExitPanel " + id + "] Gate opened. Safe travels!");
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return "ExitPanel{id='" + id + "'}";
    }
}