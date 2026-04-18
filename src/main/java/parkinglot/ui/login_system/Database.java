package parkinglot.ui.login_system;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String FILE_PATH = "./database/users.txt";

    public static void saveUser(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.username() + "|" + user.password() + "|" + user.email());
            writer.newLine();
        }
    }

    public static List<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}