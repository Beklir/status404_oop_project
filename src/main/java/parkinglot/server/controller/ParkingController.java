package parkinglot.server.controller;


import parkinglot.constants.VehicleType;
import parkinglot.models.ParkingLot;
import parkinglot.models.ParkingTicket;
import parkinglot.models.vehicles.Car;
import parkinglot.models.vehicles.Motorbike;
import parkinglot.models.vehicles.Vehicle;
import parkinglot.payment.CashTransaction;
import parkinglot.server.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;
import parkinglot.constants.ParkingSpotType;
import parkinglot.models.ParkingFloor;
import parkinglot.models.spots.*;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin
public class ParkingController {

    @Autowired
    private ParkingLotRepository lotRepo;

    @GetMapping("/status")
    public ParkingLot getStatus() {
        return lotRepo.findAll().getFirst();
    }

    // --- Admin Operations ---

    @PostMapping("/floors")
    @PreAuthorize("hasRole('ADMIN')")
    public ParkingFloor addFloor(@RequestParam String name) {
        ParkingLot lot = lotRepo.findAll().getFirst();
        ParkingFloor floor = new ParkingFloor(name);
        lot.addParkingFloor(floor);
        lotRepo.save(lot);
        return floor;
    }

    @DeleteMapping("/floors/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFloor(@PathVariable String name) {
        ParkingLot lot = lotRepo.findAll().getFirst();
        lot.getFloors().removeIf(f -> f.getName().equals(name));
        lotRepo.save(lot);
    }

    @PostMapping("/floors/{floorName}/spots")
    @PreAuthorize("hasRole('ADMIN')")
    public void addSpot(@PathVariable String floorName, @RequestParam String number, @RequestParam ParkingSpotType type) {
        ParkingLot lot = lotRepo.findAll().getFirst();
        ParkingFloor floor = lot.getFloors().stream()
                .filter(f -> f.getName().equals(floorName))
                .findFirst()
                .orElseThrow();

        ParkingSpot spot = createSpot(number, type);
        floor.addParkingSlot(spot);
        lotRepo.save(lot);
    }

    @DeleteMapping("/floors/{floorName}/spots/{spotNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSpot(@PathVariable String floorName, @PathVariable String spotNumber) {
        ParkingLot lot = lotRepo.findAll().getFirst();
        ParkingFloor floor = lot.getFloors().stream()
                .filter(f -> f.getName().equals(floorName))
                .findFirst()
                .orElseThrow();

        floor.getSpots().removeIf(s -> s.getNumber().equals(spotNumber));
        lotRepo.save(lot);
    }

    private ParkingSpot createSpot(String number, ParkingSpotType type) {
        return switch (type) {
            case HANDICAPPED -> new HandicappedSpot(number);
            case LARGE -> new LargeSpot(number);
            case MOTORBIKE -> new MotorbikeSpot(number);
            case ELECTRIC -> new ElectricSpot(number);
            default -> new CompactSpot(number);
        };
    }

    // --- Operations ---

    @PostMapping("/entry")
    public ParkingTicket enter(@RequestParam String license, @RequestParam VehicleType type) {
        ParkingLot lot = lotRepo.findAll().getFirst();
        Vehicle v = (type == VehicleType.CAR) ? new Car(license) : new Motorbike(license);
        ParkingTicket ticket = lot.vehicleEntry(v);
        if (ticket != null) lotRepo.save(lot);
        return ticket;
    }

    @PostMapping("/pay-cash")
    public String pay(@RequestParam String ticketNumber, @RequestParam double amount) {
        ParkingLot lot = lotRepo.findAll().getFirst();
        ParkingTicket ticket = lot.findTicket(ticketNumber);
        CashTransaction payment = new CashTransaction(ticket.getPayedAmount(), amount);
        if (payment.initiateTransaction()) {
            ticket.markPaid(amount);
            lotRepo.save(lot);
            return "Success. Change: " + payment.getChange();
        }
        return "Failed: Insufficient amount.";
    }
}