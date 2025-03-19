package umcs.pl.user;

import umcs.pl.vehicle.IVehicleRepository;
import umcs.pl.models.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private final IVehicleRepository vehicleRepository;
    private final String filePath;
    private final List<User> users;

    public UserRepository(String filePath, IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.filePath = filePath;
        users = loadUsers();
    }

    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String login = parts[1];
                String password = parts[2];
                Role role = Role.valueOf(parts[3]);
                String rentedVehicleId = parts.length > 4 && !parts[4].isEmpty() ? parts[4] : null;
                Vehicle rentedVehicle = rentedVehicleId != null
                        ? vehicleRepository.getVehicles().stream()
                                .filter(vehicle -> vehicle.getId().equals(rentedVehicleId))
                                .findFirst()
                                .orElse(null)
                        : null;

                if (rentedVehicle != null && !rentedVehicle.isRented()) {
                    rentedVehicle.rent();
                    vehicleRepository.rentVehicle(rentedVehicleId);
                }
                users.add(new User(id, login, password, role, rentedVehicle));
            }
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid role: " + e.getMessage());
        }

        return users;
    }

    @Override
    public User getUser(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getUsers() {
        List<User> clonedUsers = new ArrayList<>();
        users.forEach(user -> {
            try {
                clonedUsers.add((User) user.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("User clone failed: " + e.getMessage());
            }
        });

        return clonedUsers;
    }

    @Override
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, false))) {
            bufferedWriter.write("id,login,password,role,rentedVehicle");
            bufferedWriter.newLine();
            users.forEach(user -> {
                try {
                    bufferedWriter.write(user.toCsv());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
