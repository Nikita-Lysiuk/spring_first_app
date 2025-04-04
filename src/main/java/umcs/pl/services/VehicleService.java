package umcs.pl.services;

import umcs.pl.models.Vehicle;
import umcs.pl.repositories.IVehicleRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class VehicleService {
    private final IVehicleRepository vehicleRepository;

    public VehicleService(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void listVehicles() {
        vehicleRepository.findAll().forEach(this::showVehicleInfo);
    }

    public void showVehicleInfo(Vehicle vehicle) {
        System.out.println("ID: " + vehicle.getId());
        System.out.println("Category: " + vehicle.getCategory());
        System.out.println("Brand: " + vehicle.getBrand());
        System.out.println("Model: " + vehicle.getModel());
        System.out.println("Year: " + vehicle.getYear());
        System.out.println("Plate: " + vehicle.getPlate());
        Map<String, Object> attributes = vehicle.getAttributes();
        if (attributes != null && !attributes.isEmpty()) {
            System.out.println("Attributes:");
            attributes.forEach((key, value) -> System.out.println(key + ": " + value));
        }
        System.out.println("-------------------------");
    }

    public Vehicle addVehicle(String category, String brand, String model, int year, String plate, double price, Map<String, Object> attributes) {
        Vehicle vehicle = Vehicle.builder()
                .category(category)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .price(price)
                .attributes(attributes)
                .build();

        return vehicleRepository.save(vehicle);
    }

    public void removeVehicle(String vehicleId) {
        vehicleRepository.findById(vehicleId).ifPresentOrElse(
                v -> {
                    vehicleRepository.deleteById(vehicleId);
                },
                () -> { throw new IllegalArgumentException("Vehicle not found"); }
        );
    }

    public Optional<Vehicle> findVehicleById(String vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}