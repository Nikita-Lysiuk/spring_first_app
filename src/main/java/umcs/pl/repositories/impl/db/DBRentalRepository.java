package umcs.pl.repositories.impl.db;

import umcs.pl.db.JdbcConnectionManager;
import umcs.pl.models.Rental;
import umcs.pl.repositories.IRentalRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DBRentalRepository implements IRentalRepository {
    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT * FROM rental WHERE id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .userId(rs.getString("user_id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .rentDateTime(rs.getString("rent_date"))
                            .returnDateTime(rs.getString("return_date"))
                            .build();

                    return Optional.of(rental);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while reading rental by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Rental> findByVehicleId(String vehicleId) {
        String sql = "SELECT * FROM rental WHERE vehicle_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Rental> rentals = new ArrayList<>();
                while (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .userId(rs.getString("user_id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .rentDateTime(rs.getString("rent_date"))
                            .returnDateTime(rs.getString("return_date"))
                            .build();

                    rentals.add(rental);
                }
                return rentals;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rentals by vehicle ID", e);
        }
    }

    @Override
    public List<Rental> findByUserId(String userId) {
        String sql = "SELECT * FROM rental WHERE user_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Rental> rentals = new ArrayList<>();
                while (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .userId(rs.getString("user_id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .rentDateTime(rs.getString("rent_date"))
                            .returnDateTime(rs.getString("return_date"))
                            .build();

                    rentals.add(rental);
                }
                return rentals;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rentals by vehicle ID", e);
        }
    }

    @Override
    public List<Rental> findAll() {
        String sql = "SELECT * FROM rental";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            List<Rental> rentals = new ArrayList<>();

            while (rs.next()) {
                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .userId(rs.getString("user_id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .rentDateTime(rs.getString("rent_date"))
                        .returnDateTime(rs.getString("return_date"))
                        .build();

                rentals.add(rental);
            }

            return rentals;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading all rentals", e);
        }
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO rental (id, user_id, vehicle_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)" +
                " ON CONFLICT (id)" +
                " DO UPDATE SET user_id = ?, vehicle_id = ?, rent_date = ?, return_date = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rental.getId());
            stmt.setString(2, rental.getUserId());
            stmt.setString(3, rental.getVehicleId());
            stmt.setString(4, rental.getRentDateTime());
            stmt.setString(5, rental.getReturnDateTime());
            stmt.setString(6, rental.getUserId());
            stmt.setString(7, rental.getVehicleId());
            stmt.setString(8, rental.getRentDateTime());
            stmt.setString(9, rental.getReturnDateTime());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving rental", e);
        }

        return rental;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting rental by ID", e);
        }
    }
}
