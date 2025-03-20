package umcs.pl.services;

import umcs.pl.models.Car;
import umcs.pl.models.Motorcycle;
import umcs.pl.models.Vehicle;
import umcs.pl.vehicle.VehicleRepository;

import java.util.Scanner;

public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final Scanner scanner = new Scanner(System.in);

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void listVehicles() {
        vehicleRepository.getVehicles().forEach(System.out::println);
    }

    public void addVehicle() {
        System.out.println("Enter vehicle details (brand,model,year,price[,category]):");
        String[] parts = scanner.nextLine().split(",");
        try {
            String brand = parts[0];
            String model = parts[1];
            int year = Integer.parseInt(parts[2]);
            double price = Double.parseDouble(parts[3]);
            String category = parts.length > 5 ? parts[4] : null;

            String id = String.valueOf(vehicleRepository.getVehicles().stream()
                    .mapToInt(v -> Integer.parseInt(v.getId())).max()
                    .orElse(0) + 1);

            Vehicle vehicle = category != null
                    ? new Motorcycle(id, brand, model, year, price, false, category)
                    : new Car(id, brand, model, year, price, false);

            vehicleRepository.addVehicle(vehicle);
            System.out.println("Vehicle added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
        }
    }

    public void removeVehicle() {
        System.out.print("Enter vehicle ID to remove: ");
        String vehicleId = scanner.nextLine();
        try {
            vehicleRepository.removeVehicle(vehicleId);
            System.out.println("Vehicle removed successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}