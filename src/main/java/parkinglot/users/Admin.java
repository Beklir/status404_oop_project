package parkinglot.users;

import parkinglot.models.ParkingFloor;
import parkinglot.models.ParkingLot;
import parkinglot.models.ParkingRate;
import parkinglot.models.spots.*;
import parkinglot.hardware.EntrancePanel;
import parkinglot.hardware.ExitPanel;

public class Admin extends Account {

    public Admin(String userName, String password, Person person) {
        super(userName, password, person);
    }

    public boolean addParkingFloor(ParkingLot lot, ParkingFloor floor) {
        lot.addParkingFloor(floor);
        System.out.println("Admin " + getUserName() + " added floor: " + floor.getName());
        return true;
    }

    public boolean addParkingSpot(ParkingFloor floor, ParkingSpot spot) {
        floor.addParkingSlot(spot);
        System.out.println("Admin " + getUserName() + " added spot " + spot.getNumber()
                + " (" + spot.getType() + ") to floor " + floor.getName());
        return true;
    }

    public boolean addEntrancePanel(ParkingLot lot, EntrancePanel panel) {
        lot.addEntrancePanel(panel);
        System.out.println("Admin " + getUserName() + " added entrance panel: " + panel.getId());
        return true;
    }

    public boolean addExitPanel(ParkingLot lot, ExitPanel panel) {
        lot.addExitPanel(panel);
        System.out.println("Admin " + getUserName() + " added exit panel: " + panel.getId());
        return true;
    }

    public boolean addParkingAttendant(ParkingAttendant attendant) {
        System.out.println("Admin " + getUserName() + " registered attendant: "
                + attendant.getUserName());
        return true;
    }

    public boolean removeParkingAttendant(ParkingAttendant attendant) {
        System.out.println("Admin " + getUserName() + " removed attendant: "
                + attendant.getUserName());
        return true;
    }

    public boolean modifyParkingRate(ParkingRate rate, int hourNumber, double newRate) {
        rate.setHourRate(hourNumber, newRate);
        System.out.printf("Admin %s updated hour %d rate to $%.2f%n",
                getUserName(), hourNumber, newRate);
        return true;
    }

    public boolean modifyDefaultRate(ParkingRate rate, double newRate) {
        rate.setDefaultRate(newRate);
        System.out.printf("Admin %s updated default rate to $%.2f%n", getUserName(), newRate);
        return true;
    }
}