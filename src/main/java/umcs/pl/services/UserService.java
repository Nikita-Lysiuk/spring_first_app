package umcs.pl.services;


import umcs.pl.repositories.IUserRepository;

public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void listUsers() {
        userRepository.findAll().forEach(user -> {
            System.out.println("User ID: " + user.getId());
            System.out.println("Login: " + user.getLogin());
            System.out.println("Role: " + user.getRole());
            System.out.println("-------------------------");
        });
    }
}