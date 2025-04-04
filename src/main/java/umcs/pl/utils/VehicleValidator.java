package umcs.pl.utils;

import java.util.Map;
import java.util.Set;

public class VehicleValidator {
    /**
     * Map of valid vehicle categories and their attributes.
     *
     * @param category The category of the vehicle.
     * @param attributes The attributes of the vehicle.
     */
    private static final Map<String, Set<String>> validAttributes = Map.of(
            "Car", Set.of(""),
            "Bus", Set.of("seats"),
            "Motorcycle", Set.of("drive", "license_category")
    );

    public static boolean validate(String category, Map<String, Object> attributes) {
        Set<String> validKeys = validAttributes.get(category);
        if (validKeys == null) {
            return false; // Invalid category
        }

        if (attributes.size() != validKeys.size()) {
            System.out.println("Invalid number of attributes for category: " + category);
            return false; // Invalid number of attributes
        }

        for (String key : attributes.keySet()) {
            if (!validKeys.contains(key)) {
                System.out.println("Invalid attribute: " + key + " for category: " + category);
                return false; // Invalid attribute for the given category
            }
        }

        return true;
    }
}
