package parkinglot.managers;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkinglot.constants.VehicleType;
import parkinglot.models.ParkingLot;
import parkinglot.models.ParkingTicket;
import parkinglot.users.Account;

public class APIManager {
    private ServerAddress serverAddress;
    private final RestTemplate restTemplate = new RestTemplate();

    public APIManager(String ip, int port){
        setServerAddress(ip, port);
    }

    public APIManager(){
        setServerAddress("127.0.0.1",8080);
    }

    public void setServerAddress(String ip, int port){
        serverAddress = new ServerAddress(ip, port);
    }

    public void setServerIp(String ip){
        setServerAddress(ip, serverAddress.port);
    }
    public void setServerPort(int port){
        setServerAddress(serverAddress.ip, port);
    }

    public ServerAddress getServerAddress() {return serverAddress;}

    // 1. Health Check
    public String checkHealth() {
        return restTemplate.getForObject(serverAddress + "/health", String.class);
    }

    public Account login(String username, String password) {
        String url = UriComponentsBuilder.fromUriString(serverAddress + "/api/accounts/login")
                .queryParam("user", username)
                .queryParam("pass", password)
                .toUriString();

        return restTemplate.postForObject(url, null, Account.class);
    }

    // 3. Get Parking Lot Status
    public ParkingLot getStatus() {
        return restTemplate.getForObject(serverAddress + "/api/parking/status", ParkingLot.class);
    }

    // 4. Vehicle Entry
    public ParkingTicket issueTicket(String license, VehicleType type) {
        String url = UriComponentsBuilder.fromUriString(serverAddress + "/api/parking/entry")
                .queryParam("license", license)
                .queryParam("type", type)
                .toUriString();

        return restTemplate.postForObject(url, null, ParkingTicket.class);
    }
}