package umcs.pl;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import umcs.pl.services.AuthService;
import umcs.pl.models.User;
import umcs.pl.user.UserRepository;
import umcs.pl.repositories.impl.VehicleRepository;

import static org.junit.Assert.*;

public class HashPassword {
    private static VehicleRepository vehicleRepository;
    private static UserRepository userRepository;
    private static AuthService auth;

    @Before
    public void setUp() {
        vehicleRepository = new VehicleRepository("vehicles.csv");
        userRepository = new UserRepository("users.csv", vehicleRepository);
        auth = new AuthService(userRepository);
    }

    @Test
    public void testHashPassword() {
        String validPassword = "password";
        String invalidPassword = "wrongpassword";

        String hashedPassword = DigestUtils.sha256Hex(validPassword);

        User user = auth.login("admin", validPassword);
        assertNotNull(user);
        assertEquals(hashedPassword, user.getPassword());
        assertNotEquals(DigestUtils.sha256Hex(invalidPassword), user.getPassword());
    }
}
