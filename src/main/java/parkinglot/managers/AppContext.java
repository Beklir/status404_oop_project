package parkinglot.managers;

import javafx.stage.Stage;
import parkinglot.users.Account;

public class AppContext {
    public final Stage stage;
    public final APIManager apiManager = new APIManager();
    public Account account;

    public AppContext(Stage stage) {
        this.stage = stage;
    }

    public void setAccount(Account account){
        this.account = account;
    }
    public void logOut(){
        setAccount(null);
        apiManager.clearToken();
    }
}
