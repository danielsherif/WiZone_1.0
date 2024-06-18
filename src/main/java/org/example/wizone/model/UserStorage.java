package org.example.wizone.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    private static final String DATA_FILE = "users.txt";

    public static void saveUser(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            writer.println(user.getUsername() + "," + user.getPasswordHash() + "," + user.getFirstName() + "," + user.getLastName());
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
        return users;
    }

    public static boolean doesUserExist(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
}