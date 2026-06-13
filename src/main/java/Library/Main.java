package Library;

import Library.controller.LoginController;
import Library.view.GUI.LoginFrame;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
// ou FlatDarkLaf, FlatIntelliJLaf, FlatDarculaLaf

/**
 * Point d'entrée de l'application de gestion de bibliothèque.
 * <p>
 * Cette classe initialise le Look and Feel graphique de l'application
 * (via la librairie externe FlatLaf) et lance la fenêtre de connexion
 * ainsi que son contrôleur associé.
 */

public class Main {
    public static void main(String[] args) {
        try {
           //UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        LoginFrame loginFrame = new LoginFrame();
        LoginController controller = new LoginController(loginFrame);
        controller.run();
    }
}