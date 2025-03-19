package umcs.pl.auth;

import org.apache.commons.codec.digest.DigestUtils;
import umcs.pl.user.IUserRepository;
import umcs.pl.user.User;

public class Authentication {
    private final IUserRepository userRepository;

    public Authentication(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login, String password) {
        User user = userRepository.getUser(login);
        String hashedPassword = DigestUtils.sha256Hex(password);

        if (user != null && user.getPassword().equals(hashedPassword)) return user;
        else throw new IllegalArgumentException("Invalid login or password");
    }
}
