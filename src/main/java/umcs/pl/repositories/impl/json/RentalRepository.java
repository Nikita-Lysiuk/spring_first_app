package umcs.pl.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import umcs.pl.models.Rental;
import umcs.pl.repositories.IRentalRepository;
import umcs.pl.db.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalRepository implements IRentalRepository {
    private final JsonFileStorage<Rental> storage = new JsonFileStorage<>(
            "rentals.json",
            new TypeToken<List<Rental>>(){}.getType()
    );

    private final List<Rental> rentals;

    public RentalRepository() {
        this.rentals = new ArrayList<>(storage.load());
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream()
                .filter(rental -> rental.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Rental> findByVehicleId(String vehicleId) {
        return rentals.stream()
                .filter(rental -> rental.getVehicleId().equals(vehicleId))
                .toList();
    }

    @Override
    public List<Rental> findByUserId(String userId) {
        return rentals.stream()
                .filter(rental -> rental.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }

        rentals.add(rental);
        storage.save(rentals);
        return rental;
    }

    @Override
    public void deleteById(String id) {
        rentals.removeIf(rental -> rental.getId().equals(id));
        storage.save(rentals);
    }
}
