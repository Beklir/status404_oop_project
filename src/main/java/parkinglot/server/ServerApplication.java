package parkinglot.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import parkinglot.models.Location;
import parkinglot.models.ParkingFloor;
import parkinglot.models.ParkingLot;
import parkinglot.models.spots.CompactSpot;
import parkinglot.models.spots.MotorbikeSpot;
import parkinglot.users.Admin;
import parkinglot.users.Person;
import parkinglot.server.repository.AccountRepository;
import parkinglot.server.repository.ParkingLotRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"parkinglot"})
@EntityScan(basePackages = {"parkinglot"})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ParkingLotRepository repo, AccountRepository accountRepo) {
        return args -> {
            if (repo.count() == 0) {
                ParkingLot lot = new ParkingLot("L1", "University Lot",
                        new Location("123 Uni St", "Tashkent", "UZ", "1000", "UZB"));

                ParkingFloor f1 = new ParkingFloor("Floor 1");
                f1.addParkingSlot(new CompactSpot("C1"));
                f1.addParkingSlot(new CompactSpot("C2"));
                f1.addParkingSlot(new MotorbikeSpot("M1"));

                lot.addParkingFloor(f1);
                repo.save(lot);
                System.out.println("Demo Parking Lot created in SQLite!");
            }
            if (accountRepo.count() == 0) {
                Person adminInfo = new Person("System Admin",
                        new Location("Movarounnahra street 1", "Tashkent", "TSH", "100", "UZB"),
                        "admin@newuu.uz", "998901234567");

                Admin admin = new Admin("admin", "admin", adminInfo);
                accountRepo.save(admin);

                System.out.println("Default Admin created: user: admin / pass: admin");
            }
        };
    }
}