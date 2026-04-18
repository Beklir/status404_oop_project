package parkinglot.hardware;

import parkinglot.models.ParkingTicket;
import parkinglot.models.ParkingRate;
import parkinglot.payment.CreditCardTransaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ElectricPanel {
    private String id;
    private int payedForMinutes;
    private LocalDateTime chargingStartTime;
    private boolean charging;

    public ElectricPanel(String id) {
        this.id = id;
        this.charging = false;
        this.payedForMinutes = 0;
    }

//    Getters and Setters
    public int getPayedForMinutes() { return payedForMinutes; }
    public LocalDateTime getChargingStartTime() { return chargingStartTime; }
    public boolean isCharging() { return charging; }
    public String getId() { return id; }

}