package umcs.pl.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import umcs.pl.models.User;
import umcs.pl.repositories.IUserRepository;
import umcs.pl.db.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements IUserRepository {
    private final JsonFileStorage<User> storage = new JsonFileStorage<>(
            "users.json",
            new TypeToken<List<User>>(){}.getType()
    );

    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>(storage.load());
    }


    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        } else {
            deleteById(user.getId());
        }

        users.add(user);
        storage.save(users);
        return user;
    }

    @Override
    public void deleteById(String id) {
        users.removeIf(user -> user.getId().equals(id));
        storage.save(users);
    }
}
