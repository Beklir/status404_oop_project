package parkinglot;

import parkinglot.constants.AccountStatus;
import parkinglot.hardware.*;
import parkinglot.models.*;
import parkinglot.models.spots.*;
import parkinglot.models.vehicles.*;
import parkinglot.users.*;

/**
 * Entry point — demonstrates a complete end-to-end parking lot workflow:
 *  1. Admin sets up the parking lot (floors, spots, panels)
 *  2. Vehicles arrive, get tickets, and are assigned spots
 *  3. Display boards update in real time
 *  4. Customers pay via different methods (cash, credit card, info portal)
 *  5. Vehicles exit and spots are freed
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. Bootstrap the parking lot ────────────────────────────────────
        Location address = new Location("123 Main St", "Springfield", "IL", "62701", "USA");
        ParkingLot lot = ParkingLot.getInstance("LOT-001", "Downtown Parking Center", address);

        // ── 2. Create admin and attendant ────────────────────────────────────
        Person adminPerson = new Person("Alice Admin", address, "alice@park.com", "555-0100");
        Admin admin = new Admin("alice", "admin123", adminPerson);

        Person attendantPerson = new Person("Bob Booth", address, "bob@park.com", "555-0101");
        ParkingAttendant attendant = new ParkingAttendant("bob", "attend123", attendantPerson);
        admin.addParkingAttendant(attendant);

        // ── 3. Add entrance & exit panels ───────────────────────────────────
        EntrancePanel entrance1 = new EntrancePanel("EP-NORTH");
        EntrancePanel entrance2 = new EntrancePanel("EP-SOUTH");
        admin.addEntrancePanel(lot, entrance1);
        admin.addEntrancePanel(lot, entrance2);

        ExitPanel exit1 = new ExitPanel("XP-NORTH");
        ExitPanel exit2 = new ExitPanel("XP-SOUTH");
        admin.addExitPanel(lot, exit1);
        admin.addExitPanel(lot, exit2);

        // ── 4. Build Floor 1 ────────────────────────────────────────────────
        ParkingFloor floor1 = new ParkingFloor("Floor-1");
        admin.addParkingSpot(floor1, new CompactSpot("F1-C01"));
        admin.addParkingSpot(floor1, new CompactSpot("F1-C02"));
        admin.addParkingSpot(floor1, new CompactSpot("F1-C03"));
        admin.addParkingSpot(floor1, new LargeSpot("F1-L01"));
        admin.addParkingSpot(floor1, new LargeSpot("F1-L02"));
        admin.addParkingSpot(floor1, new HandicappedSpot("F1-H01"));
        admin.addParkingSpot(floor1, new MotorbikeSpot("F1-M01"));
        admin.addParkingSpot(floor1, new ElectricSpot("F1-E01"));
        admin.addParkingFloor(lot, floor1);

        // ── 5. Build Floor 2 ────────────────────────────────────────────────
        ParkingFloor floor2 = new ParkingFloor("Floor-2");
        admin.addParkingSpot(floor2, new CompactSpot("F2-C01"));
        admin.addParkingSpot(floor2, new CompactSpot("F2-C02"));
        admin.addParkingSpot(floor2, new LargeSpot("F2-L01"));
        admin.addParkingSpot(floor2, new ElectricSpot("F2-E01"));
        admin.addParkingFloor(lot, floor2);

        // ── 6. Show initial status ───────────────────────────────────────────
        System.out.println("\n========== INITIAL STATE ==========");
        lot.showStatus();

        // ── 7. Vehicles arrive ───────────────────────────────────────────────
        System.out.println("\n========== VEHICLES ARRIVING ==========");
        Car car1         = new Car("ABC-1111");
        Car car2         = new Car("ABC-2222");
        Van van1         = new Van("VAN-0001");
        Truck truck1     = new Truck("TRK-0001");
        Motorbike moto1  = new Motorbike("MTB-0001");
        ElectricVehicle ev1 = new ElectricVehicle("EV-0001");

        ParkingTicket ticket1 = lot.vehicleEntry(car1);
        ParkingTicket ticket2 = lot.vehicleEntry(car2);
        ParkingTicket ticket3 = lot.vehicleEntry(van1);
        ParkingTicket ticket4 = lot.vehicleEntry(truck1);
        ParkingTicket ticket5 = lot.vehicleEntry(moto1);
        ParkingTicket ticket6 = lot.vehicleEntry(ev1);

        // ── 8. Show live status after arrivals ───────────────────────────────
        System.out.println("\n========== STATUS AFTER ARRIVALS ==========");
        lot.showStatus();

        // ── 9. Payment scenarios ─────────────────────────────────────────────

        // 9a: car1 pays with credit card at exit panel (direct exit)
        System.out.println("\n--- Scenario A: car1 pays at exit panel (credit card) ---");
        if (ticket1 != null) {
            exit1.processPayment(ticket1, lot.getParkingRate(), "Alice Smith", "4111111111111111");
            lot.vehicleExit(car1);
        }

        // 9b: car2 pays at customer info portal (then exits without paying again)
        System.out.println("\n--- Scenario B: car2 pays at info portal first ---");
        CustomerInfoPortal portal1 = new CustomerInfoPortal("CIP-F1");
        if (ticket2 != null) {
            portal1.processPayment(ticket2, lot.getParkingRate(), "Bob Jones", "5500005555555559");
            // At the exit, ticket is already paid — gate opens automatically
            exit2.processPayment(ticket2, lot.getParkingRate(), "Bob Jones", "5500005555555559");
            lot.vehicleExit(car2);
        }

        // 9c: van1 pays with cash at exit panel via attendant portal
        System.out.println("\n--- Scenario C: van1 pays via attendant portal (cash) ---");
        ParkingAttendantPortal attendantPortal = new ParkingAttendantPortal("ATP-01");
        if (ticket3 != null) {
            attendantPortal.processPayment(ticket3, lot.getParkingRate(), 20.00);
            lot.vehicleExit(van1);
        }

        // 9d: truck1 pays at exit panel with cash
        System.out.println("\n--- Scenario D: truck1 pays at exit panel (cash) ---");
        if (ticket4 != null) {
            exit1.processPayment(ticket4, lot.getParkingRate(), 15.00);
            lot.vehicleExit(truck1);
        }

        // 9e: motorbike pays at exit with credit card
        System.out.println("\n--- Scenario E: motorbike pays at exit (credit card) ---");
        if (ticket5 != null) {
            exit2.processPayment(ticket5, lot.getParkingRate(), "Mike Rider", "4000056655665556");
            lot.vehicleExit(moto1);
        }

        // 9f: electric vehicle — demo ElectricPanel charging + exit payment
        System.out.println("\n--- Scenario F: EV charges and pays at electric panel ---");
        ElectricPanel evPanel = new ElectricPanel("EVP-F1-E01");
        evPanel.startCharging(60, "Eve Electric", "4111222233334444");
        if (ticket6 != null) {
            evPanel.processPayment(ticket6, lot.getParkingRate(), "Eve Electric", "4111222233334444");
            lot.vehicleExit(ev1);
        }

        // ── 10. Attempt overflow to test full message ────────────────────────
        System.out.println("\n========== OVERFLOW TEST (Compact spots may be full) ==========");
        Car overflow1 = new Car("OVF-0001");
        Car overflow2 = new Car("OVF-0002");
        Car overflow3 = new Car("OVF-0003");
        Car overflow4 = new Car("OVF-0004");
        Car overflow5 = new Car("OVF-0005");
        lot.vehicleEntry(overflow1);
        lot.vehicleEntry(overflow2);
        lot.vehicleEntry(overflow3);
        lot.vehicleEntry(overflow4);
        lot.vehicleEntry(overflow5); // should trigger "full" message

        // ── 11. Admin updates parking rate ───────────────────────────────────
        System.out.println("\n========== ADMIN UPDATES PARKING RATE ==========");
        admin.modifyParkingRate(lot.getParkingRate(), 1, 5.0);
        admin.modifyDefaultRate(lot.getParkingRate(), 3.0);
        System.out.println("Updated rate: " + lot.getParkingRate());

        // ── 12. Final status ─────────────────────────────────────────────────
        System.out.println("\n========== FINAL STATE ==========");
        lot.showStatus();
    }
}