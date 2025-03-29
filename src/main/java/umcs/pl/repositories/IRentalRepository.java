package umcs.pl.repositories;

import umcs.pl.models.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalRepository {
    Optional<Rental> findById(String id);
    List<Rental> findByVehicleId(String vehicleId);
    List<Rental> findByUserId(String userId);
    List<Rental> findAll();
    Rental save(Rental rental);
    void deleteById(String id);
}
