package parkinglot.managers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkinglot.constants.VehicleType;
import parkinglot.models.ParkingLot;
import parkinglot.models.ParkingTicket;
import parkinglot.users.Account;
import parkinglot.utils.LoginResponse;

public class APIManager {
    private String authToken = null;
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

    public void clearToken(){this.authToken = null;}

    private HttpEntity<Void> getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    // 1. Health Check
    public String checkHealth() {
        return restTemplate.getForObject(serverAddress + "/health", String.class);
    }

    public LoginResponse login(String username, String password) throws Exception{
        String url = UriComponentsBuilder.fromUriString(serverAddress + "/api/accounts/login")
                .queryParam("user", username)
                .queryParam("pass", password)
                .toUriString();

        try {
            LoginResponse response = restTemplate.postForObject(url, null, LoginResponse.class);
            if (response != null) {
                this.authToken = response.token();
            }
            return response;
        } catch (org.springframework.web.client.HttpClientErrorException.Unauthorized e) {
            return null;
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            throw e;
        }
    }

    public void register(Account account) throws Exception {
        try {
            restTemplate.postForEntity(serverAddress + "/api/accounts/register", account, String.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new Exception(e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new Exception("Connection error: " + e.getMessage());
        }
    }

    public void updatePerson(String username, Person person) throws Exception {
        String url = UriComponentsBuilder.fromUriString(serverAddress + "/api/accounts/update-person")
                .queryParam("username", username)
                .toUriString();
        try {
            restTemplate.postForEntity(url, person, String.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new Exception(e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new Exception("Connection error: " + e.getMessage());
        }
    }

    // 3. Get Parking Lot Status
    public ParkingLot getStatus() {
        // Use exchange instead of getForObject to send headers
        return restTemplate.exchange(
                serverAddress + "/api/parking/status",
                HttpMethod.GET,
                getAuthHeaders(),
                ParkingLot.class
        ).getBody();
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