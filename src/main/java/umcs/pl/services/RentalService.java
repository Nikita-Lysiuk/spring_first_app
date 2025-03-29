package umcs.pl.services;

import umcs.pl.models.Rental;
import umcs.pl.models.Vehicle;
import umcs.pl.repositories.IRentalRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class RentalService {
    private final IRentalRepository rentalRepository;

    public RentalService(IRentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental rentVehicle(String userId, String vehicleId) {
        boolean isRented = rentalRepository.findByVehicleId(vehicleId).stream()
                .anyMatch(rental -> rental.getReturnDateTime() == null);

        if (isRented)
            throw new IllegalArgumentException("Vehicle is already rented.");

        Rental rental = Rental.builder()
                .userId(userId)
                .vehicleId(vehicleId)
                .rentDateTime(LocalDateTime.now().toString())
                .returnDateTime(null)
                .build();

        return rentalRepository.save(rental);
    }

    public Rental returnVehicle(String userId, String vehicleId) {
        boolean isRented = rentalRepository.findByVehicleId(vehicleId).stream()
                .anyMatch(rental -> rental.getReturnDateTime() == null);

        if (!isRented)
            throw new IllegalArgumentException("Vehicle is not rented.");

        Rental rental = rentalRepository.findByVehicleId(vehicleId).stream()
                .filter(r -> r.getReturnDateTime() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rental not found."));

        if (!rental.getUserId().equals(userId)) {
            throw new IllegalArgumentException("This vehicle was not rented by you.");
        }

        rental.setReturnDateTime(LocalDateTime.now().toString());
        return rentalRepository.save(rental);
    }

    public void showUserRentals(String userId) {
        System.out.println("Rentals for user ID: " + userId);
        rentalRepository.findByUserId(userId).forEach(rental -> {
            System.out.println("Rental ID: " + rental.getId());
            System.out.println("Vehicle ID: " + rental.getVehicleId());
            System.out.println("Rent Date: " + rental.getRentDateTime());
            System.out.println("Return Date: " + rental.getReturnDateTime());
            System.out.println("-----------------------------");
        });
    }

    public boolean isVehicleFree(Vehicle vehicle) {
        return rentalRepository.findByVehicleId(vehicle.getId()).stream()
                .noneMatch(rental -> rental.getReturnDateTime() == null);
    }

    public boolean isVehicleRented(Vehicle vehicle) {
        return rentalRepository.findByVehicleId(vehicle.getId()).stream()
                .anyMatch(rental -> rental.getReturnDateTime() == null);
    }
}