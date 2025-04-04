package umcs.pl.repositories.impl.db;

import umcs.pl.db.JdbcConnectionManager;
import umcs.pl.models.Role;
import umcs.pl.models.User;
import umcs.pl.repositories.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DBUserRepository implements IUserRepository {

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getString("id"))
                        .login(rs.getString("login"))
                        .password(rs.getString("password"))
                        .role(Role.valueOf(rs.getString("role")))
                        .build();

                list.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading users (find all)", e);
        }

        return list;
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(Role.valueOf(rs.getString("role")))
                            .build();

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading user (find by id)", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getString("id"))
                            .login(rs.getString("login"))
                            .password(rs.getString("password"))
                            .role(Role.valueOf(rs.getString("role")))
                            .build();

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading user (find by login)", e);
        }

        return Optional.empty();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO users (id, login, password, role) " +
                "VALUES (?, ?, ?, ?)" +
                " ON CONFLICT (id)" +
                "DO UPDATE SET login = ?, password = ?, role = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getLogin());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole().name());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }

        return user;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting user", e);
        }
    }
}
