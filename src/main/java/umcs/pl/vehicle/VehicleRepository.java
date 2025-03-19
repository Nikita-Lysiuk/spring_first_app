package umcs.pl.vehicle;

import umcs.pl.models.Car;
import umcs.pl.models.Motorcycle;
import umcs.pl.models.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private final String filePath;
    private List<Vehicle> vehicles = new ArrayList<>();

    public VehicleRepository(String filePath) {
        this.filePath = filePath;
        load();
    }

    private void load() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            bufferedReader.readLine(); // Skip first csv line
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String brand = parts[1];
                String model = parts[2];
                int year = Integer.parseInt(parts[3]);
                double price = Double.parseDouble(parts[4]);
                boolean rented = Boolean.parseBoolean(parts[5]);
                String category = parts.length > 6 ? parts[6] : null;
                Vehicle vehicle;
                if (category != null) {
                    vehicle = new Motorcycle(id, brand, model, year, price, rented, category);
                } else {
                    vehicle = new Car(id, brand, model, year, price, rented);
                }
                vehicles.add(vehicle);
            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }


    @Override
    public void rentVehicle(String id) {
        vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .map(vehicle -> {
                    if (!vehicle.isRented()) vehicle.rent();
                    else throw new IllegalStateException("Vehicle is already rented");
                    return vehicle;
                })
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        save();
    }

    @Override
    public void returnVehicle(String id) {
        vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .map(vehicle -> {
                    if (vehicle.isRented()) vehicle.returnVehicle();
                    else throw new IllegalStateException("Vehicle is already returned");
                    return vehicle;
                })
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));


        save();
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> clonedVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            try {
                clonedVehicles.add(vehicle.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error cloning vehicle: " + e.getMessage());
            }
        }

        return clonedVehicles;
    }

    @Override
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, false))) {
            bufferedWriter.write("id,brand,model,year,price,rented,category");
            bufferedWriter.newLine();
            for (Vehicle vehicle : vehicles) {
                bufferedWriter.write(vehicle.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving vehicles: " + e.getMessage());
        }
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        try {
            vehicles.add(vehicle.clone());
            save();
        } catch (CloneNotSupportedException e) {
            System.err.println("Error cloning vehicle: " + e.getMessage());
        }
    }

    @Override
    public void removeVehicle(String id) throws IllegalArgumentException, IllegalStateException {
        Vehicle vehicleToRemove = vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (vehicleToRemove.isRented()) throw new IllegalStateException("Cannot remove rented vehicle");
        vehicles.remove(vehicleToRemove);
        save();
    }
}
