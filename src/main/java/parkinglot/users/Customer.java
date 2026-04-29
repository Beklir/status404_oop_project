package parkinglot.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends Account {

    public Customer(String userName, String password, Person person) {
        super(userName, password, person);
    }

    protected Customer() {
        super();
    }
}
