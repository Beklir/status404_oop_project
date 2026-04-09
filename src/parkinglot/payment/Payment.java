package parkinglot.payment;

import java.time.LocalDateTime;

public abstract class Payment {
    private final LocalDateTime creationDate;
    private double amount;
    private PaymentStatus status;

    public Payment(double amount) {
        this.amount = amount;
        this.creationDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public abstract boolean initiateTransaction();

    // Getters and Setters
    public LocalDateTime getCreationDate() { return creationDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Payment{amount=%.2f, status=%s, date=%s}", amount, status, creationDate);
    }
}
