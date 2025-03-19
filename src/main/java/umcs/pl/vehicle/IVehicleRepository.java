package umcs.pl.vehicle;

import umcs.pl.models.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    void rentVehicle(String id);
    void returnVehicle(String id);
    List<Vehicle> getVehicles();
    void save();
    void addVehicle(Vehicle vehicle);
    void removeVehicle(String id);
}
