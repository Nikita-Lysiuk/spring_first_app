package umcs.pl;

import umcs.pl.repositories.IRentalRepository;
import umcs.pl.repositories.IUserRepository;
import umcs.pl.repositories.IVehicleRepository;
import umcs.pl.repositories.impl.db.DBRentalRepository;
import umcs.pl.repositories.impl.db.DBUserRepository;
import umcs.pl.repositories.impl.db.DBVehicleJdbcRepository;
import umcs.pl.repositories.impl.json.RentalRepository;
import umcs.pl.repositories.impl.json.UserRepository;
import umcs.pl.services.AuthService;
import umcs.pl.models.Vehicle;
import umcs.pl.services.RentalService;
import umcs.pl.services.UserService;
import umcs.pl.services.VehicleService;
import umcs.pl.models.Role;
import umcs.pl.models.User;
import umcs.pl.repositories.impl.json.VehicleRepository;
import umcs.pl.utils.PasswordField;
import umcs.pl.utils.VehicleValidator;

import java.io.Console;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static VehicleService vehicleService;
    private static UserService userService;
    private static RentalService rentalService;
    private static AuthService authService;

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
        IVehicleRepository vehicleRepository;
        IUserRepository userRepository;
        IRentalRepository rentalRepository;

        System.out.print("Choose database (json/db): ");
        String databaseChoice = scanner.nextLine();
        if (databaseChoice.equalsIgnoreCase("json")) {
            vehicleRepository = new VehicleRepository();
            userRepository = new UserRepository();
            rentalRepository = new RentalRepository();
        } else if (databaseChoice.equalsIgnoreCase("db")) {
            vehicleRepository = new DBVehicleJdbcRepository();
            userRepository = new DBUserRepository();
            rentalRepository = new DBRentalRepository();
        } else {
            throw new IllegalArgumentException("Invalid database choice. Please choose 'json' or 'db'.");
        }


        vehicleService = new VehicleService(vehicleRepository);
        userService = new UserService(userRepository);
        rentalService = new RentalService(rentalRepository);
        authService = new AuthService(userRepository);
    }

    private static void showMainMenu() throws CloneNotSupportedException {
        while (true) {
            System.out.println("\n=== Vehicle Rental System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Test Vehicle Features");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleRegister();
                case 3 -> testVehicleFeatures();
                case 4 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showUserMenu(User user) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. View My Details");
            System.out.println("4. View My Rentals");
            System.out.println("5. Show available vehicles");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> handleRentVehicle(user);
                case 2 -> handleReturnVehicle(user);
                case 3 -> System.out.println(user);
                case 4 -> rentalService.showUserRentals(user.getId());
                case 5 -> showAvailableVehicles();
                case 6 -> {
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
            System.out.println("7. Show available vehicles");
            System.out.println("8. Show rentals");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> handleRentVehicle(user);
                case 2 -> handleReturnVehicle(user);
                case 3 -> handleAddVehicle();
                case 4 -> handleRemoveVehicle();
                case 5 -> userService.listUsers();
                case 6 -> vehicleService.listVehicles();
                case 7 -> showAvailableVehicles();
                case 8 -> showRentals();
                case 9 -> {
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
        // It does not work in IDE, but works in terminal
        // String password = PasswordField.readPassword("*");
        // String password = new String(System.console().readPassword());
        String password = scanner.nextLine();

        try {
            User user = authService.login(login, password);

            System.out.println("Login successful: " + user);
            if (user.getRole().equals(Role.ADMIN)) {
                showAdminMenu(user);
            } else {
                showUserMenu(user);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
    }

    private static void handleRegister() {
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        try {
            authService.register(login, password);
            System.out.println("Registration successful. You can now log in.");
        } catch (IllegalArgumentException e) {
            System.err.println("Registration failed: " + e.getMessage());
        }
    }

    private static void handleRentVehicle(User user) {
        System.out.print("Enter vehicle ID to rent: ");
        String vehicleId = scanner.nextLine();

        try {
            rentalService.rentVehicle(user.getId(), vehicleId);
            System.out.println("Vehicle rented successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleReturnVehicle(User user) {
        System.out.print("Enter vehicle ID to return: ");
        String vehicleId = scanner.nextLine();

        try {
            rentalService.returnVehicle(user.getId(), vehicleId);
            System.out.println("Vehicle returned successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void showAvailableVehicles() {
        vehicleService.findAll().stream()
                .filter(vehicle -> rentalService.isVehicleFree(vehicle))
                .forEach(vehicle -> vehicleService.showVehicleInfo(vehicle));
    }

    private static void showRentals() {
        vehicleService.findAll().stream()
                .filter(vehicle -> rentalService.isVehicleRented(vehicle))
                .forEach(vehicle -> vehicleService.showVehicleInfo(vehicle));
    }

    private static void handleAddVehicle() {
        System.out.print("Enter vehicle category: ");
        String category = scanner.nextLine();
        System.out.print("Enter vehicle brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter vehicle model: ");
        String model = scanner.nextLine();
        System.out.print("Enter vehicle year: ");
        int year = getIntInput();
        System.out.print("Enter vehicle plate: ");
        String plate = scanner.nextLine();
        System.out.print("Enter vehicle price: ");
        double price = getIntInput();

        System.out.print("Enter vehicle attributes (key:value, separated by commas): ");
        String attributesInput = scanner.nextLine();
        Map<String, Object> attributes = new java.util.HashMap<>(Map.of());
        if (!attributesInput.isBlank()) {
            String[] pairs = attributesInput.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    attributes.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }

        try {
            if (!VehicleValidator.validate(category, attributes)) {
                throw new IllegalArgumentException("Invalid vehicle attributes for category: " + category);
            }

            vehicleService.addVehicle(category, brand, model, year, plate, price, attributes);
            System.out.println("Vehicle added successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleRemoveVehicle() {
        System.out.print("Enter vehicle ID to remove: ");
        String vehicleId = scanner.nextLine();
        try {
            vehicleService.removeVehicle(vehicleId);
            System.out.println("Vehicle removed successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testVehicleFeatures() {
        System.out.println("\n=== Testing Vehicle Features ===");
        System.out.println("Sorry, this feature is not implemented yet.");
//        Car car1 = new Car("1", "Toyota", "Corolla", 2020, 20000, false);
//        Car car2 = new Car("2", "Honda", "Civic", 2021, 22000, false);
//        Car car3 = car1.clone();
//
//        System.out.println("Test 1: Two different vehicles");
//        testHashCode(car1, car2);
//        testEquals(car1, car2);
//
//        System.out.println("Test 2: Cloned vehicle");
//        testHashCode(car1, car3);
//        testEquals(car1, car3);
//
//        System.out.println("Test 3: Rent cloned vehicle");
//        car3.rent();
//        testHashCode(car1, car3);
//        testEquals(car1, car3);
//
//        System.out.println("Test 4: Return cloned vehicle");
//        car3.returnVehicle();
//        testHashCode(car1, car3);
//        testEquals(car1, car3);
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