package umcs.pl.user;

import java.util.List;

public interface IUserRepository {
    User getUser(String login);
    List<User> getUsers();
    void save();
}
