package umcs.pl;

import org.apache.commons.codec.digest.DigestUtils;
import umcs.pl.auth.Authentication;
import umcs.pl.models.Car;
import umcs.pl.models.Motorcycle;
import umcs.pl.models.Vehicle;
import umcs.pl.services.RentalService;
import umcs.pl.services.UserService;
import umcs.pl.services.VehicleService;
import umcs.pl.user.Role;
import umcs.pl.user.User;
import umcs.pl.user.UserRepository;
import umcs.pl.vehicle.VehicleRepository;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static VehicleService vehicleService;
    private static UserService userService;
    private static RentalService rentalService;
    private static Authentication auth;

    public static void main(String[] args) {

        try {
            initializeRepositories();
            showMainMenu();
        } catch (CloneNotSupportedException | IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void initializeRepositories() {
        VehicleRepository vehicleRepository = new VehicleRepository("vehicles.csv");
        vehicleService = new VehicleService(vehicleRepository);
        UserRepository userRepository = new UserRepository("users.csv", vehicleRepository);
        userService = new UserService(userRepository);
        auth = new Authentication(userRepository);
        rentalService = new RentalService(vehicleRepository, userRepository);
    }

    private static void showMainMenu() throws CloneNotSupportedException {
        while (true) {
            System.out.println("\n=== Vehicle Rental System ===");
            System.out.println("1. Login");
            System.out.println("2. Test Vehicle Features");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> testVehicleFeatures();
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = auth.login(login, password);
        if (user != null) {
            System.out.println("Login successful: " + user);
            if (user.getRole().equals(Role.ADMIN)) {
                showAdminMenu(user);
            } else {
                showUserMenu(user);
            }
        } else {
            System.out.println("Login failed. Incorrect credentials.");
        }
    }

    private static void showUserMenu(User user) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. View My Details");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> rentalService.rentVehicle(user);
                case 2 -> rentalService.returnVehicle(user);
                case 3 -> System.out.println(user);
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showAdminMenu(User user) {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. Add a Vehicle");
            System.out.println("4. Remove a Vehicle");
            System.out.println("5. List All Users");
            System.out.println("6. List All Vehicles");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> rentalService.rentVehicle(user);
                case 2 -> rentalService.returnVehicle(user);
                case 3 -> vehicleService.addVehicle();
                case 4 -> vehicleService.removeVehicle();
                case 5 -> userService.listUsers();
                case 6 -> vehicleService.listVehicles();
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void testVehicleFeatures() throws CloneNotSupportedException {
        System.out.println("\n=== Testing Vehicle Features ===");
        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, false);
        Car car2 = new Car("2", "Honda", "Civic", 2021, 22000, false);
        Car car3 = car1.clone();

        System.out.println("Test 1: Two different vehicles");
        testHashCode(car1, car2);
        testEquals(car1, car2);

        System.out.println("Test 2: Cloned vehicle");
        testHashCode(car1, car3);
        testEquals(car1, car3);

        System.out.println("Test 3: Rent cloned vehicle");
        car3.rent();
        testHashCode(car1, car3);
        testEquals(car1, car3);

        System.out.println("Test 4: Return cloned vehicle");
        car3.returnVehicle();
        testHashCode(car1, car3);
        testEquals(car1, car3);
    }

    private static void testHashCode(Vehicle v1, Vehicle v2) {
        System.out.printf("Hash codes: %d vs %d - %s%n",
                v1.hashCode(), v2.hashCode(),
                v1.hashCode() == v2.hashCode() ? "Same" : "Different");
    }

    private static void testEquals(Vehicle v1, Vehicle v2) {
        System.out.println("Equality: " + (v1.equals(v2) ? "Equal" : "Not Equal"));
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }
}