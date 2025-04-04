package umcs.pl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PasswordField {
    /**
     * @param prompt The prompt displayed to the user.
     * @return The password entered by the user.
     */
    public static String readPassword(String prompt) {
        EraserThread eraser = new EraserThread(prompt);
        Thread mask = new Thread(eraser);
        mask.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "";

        try {
            password = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        eraser.stopMasking();

        return password;
    }
}
