package umcs.pl.services;

import umcs.pl.user.User;
import umcs.pl.user.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void listUsers() {
        userRepository.getUsers().forEach(System.out::println);
    }
}