package Library;

import Library.controller.LoginController;
import Library.view.GUI.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
            LoginFrame loginFrame = new LoginFrame();
            LoginController controller = new LoginController(loginFrame);
            controller.run();
    }
}