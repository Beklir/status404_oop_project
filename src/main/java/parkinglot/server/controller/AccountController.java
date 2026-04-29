package parkinglot.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkinglot.managers.JwtService;
import parkinglot.users.Account;
import parkinglot.users.ParkingAttendant;
import parkinglot.server.repository.AccountRepository;
import parkinglot.users.Person;
import parkinglot.utils.LoginResponse;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String user, @RequestParam String pass) {
        return accountRepo.findById(user)
                .filter(acc -> acc.login(user, pass))
                .map(acc -> {
                    String token = jwtService.generateToken(acc.getUserName());
                    return ResponseEntity.ok(new LoginResponse(token, acc));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // 2. Admin adds an Attendant
    @PostMapping("/add-attendant")
    public Account addAttendant(@RequestBody ParkingAttendant attendant) {
        return accountRepo.save(attendant);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Account account) {
        if (accountRepo.existsById(account.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        try {
            accountRepo.save(account);
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating account: " + e.getMessage());
        }
    }

    @PostMapping("/update-person")
    public ResponseEntity<String> updatePerson(@RequestParam String username, @RequestBody Person person) {
        return accountRepo.findById(username)
                .map(acc -> {
                    acc.setPerson(person);
                    accountRepo.save(acc);
                    return ResponseEntity.ok("Person details updated successfully");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @GetMapping("/me")
    public ResponseEntity<Account> getCurrentAccount(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return accountRepo.findById(principal.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 3. List all accounts (For Admin Dashboard)
    @GetMapping("/all")
    public List<Account> getAll() {
        return accountRepo.findAll();
    }
}