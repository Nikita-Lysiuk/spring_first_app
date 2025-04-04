package umcs.pl.services;

import org.mindrot.jbcrypt.BCrypt;
import umcs.pl.models.Role;
import umcs.pl.repositories.IUserRepository;
import umcs.pl.models.User;

import java.util.Optional;

public class AuthService {
    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));

        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Invalid login or password");
        }
    }

    public void register(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("User with this login already exists");
        }

        User newUser = User.builder()
                .id(null)
                .login(login)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .role(Role.USER)
                .build();

        userRepository.save(newUser);
    }
}
