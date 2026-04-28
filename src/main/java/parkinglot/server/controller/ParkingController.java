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

    // 2. Process a vehicle entry
    @PostMapping("/entry")
    public ParkingTicket enter(@RequestParam String license, @RequestParam VehicleType type) {
        ParkingLot lot = lotRepo.findAll().getFirst();

        // Simple factory logic
        Vehicle v = (type == VehicleType.CAR) ? new Car(license) : new Motorbike(license);

        ParkingTicket ticket = lot.vehicleEntry(v);
        if (ticket != null) lotRepo.save(lot); // This saves the ticket and updates the spot!
        return ticket;
    }

    // 3. Process a cash payment
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