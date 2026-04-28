package parkinglot.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkinglot.users.Account;
import parkinglot.users.ParkingAttendant;
import parkinglot.server.repository.AccountRepository;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestParam String user, @RequestParam String pass) {
        return accountRepo.findById(user)
                .filter(acc -> acc.login(user, pass))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // 2. Admin adds an Attendant
    @PostMapping("/add-attendant")
    public Account addAttendant(@RequestBody ParkingAttendant attendant) {
        return accountRepo.save(attendant);
    }

    // 3. List all accounts (For Admin Dashboard)
    @GetMapping("/all")
    public List<Account> getAll() {
        return accountRepo.findAll();
    }
}