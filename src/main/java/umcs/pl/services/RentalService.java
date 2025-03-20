package umcs.pl.services;

import umcs.pl.models.Vehicle;
import umcs.pl.user.User;
import umcs.pl.user.UserRepository;
import umcs.pl.vehicle.VehicleRepository;

import java.util.Scanner;

public class RentalService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final Scanner scanner = new Scanner(System.in);

    public RentalService(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public void rentVehicle(User user) {
        if (user.getRentedVehicle() != null) {
            System.out.println("You already have a rented vehicle.");
            return;
        }

        System.out.print("Enter vehicle ID to rent: ");
        String vehicleId = scanner.nextLine();

        try {
            vehicleRepository.rentVehicle(vehicleId);
            Vehicle rentedVehicle = vehicleRepository.getVehicles().stream()
                    .filter(v -> v.getId().equals(vehicleId))
                    .findFirst().orElse(null);
            user.setRentedVehicle(rentedVehicle);
            userRepository.save();
            System.out.println("Vehicle rented successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void returnVehicle(User user) {
        if (user.getRentedVehicle() == null) {
            System.out.println("You have no rented vehicle.");
            return;
        }

        vehicleRepository.returnVehicle(user.getRentedVehicle().getId());
        user.setRentedVehicle(null);
        System.out.println("Vehicle returned successfully.");
    }
}