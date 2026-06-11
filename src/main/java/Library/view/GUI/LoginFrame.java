package Library.view.GUI;

import Library.controller.LoginController;
import Library.view.ViewLogin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame implements ViewLogin {

    private JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton okButton;
    private JButton cancelButton;
    private JButton signupButton;

    public LoginFrame() {
        super("Login");

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel background = new JLabel(
                new ImageIcon("src/main/resources/EntréBibliothéque.jpeg")
        );
        background.setLayout(new GridBagLayout());
        setContentPane(background);


        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));
        mainPanel.setBackground(new Color(255, 255, 255, 220));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel lblLogin = new JLabel("Login :");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblLogin);

        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(textField1);


        JLabel lblPassword = new JLabel("Password :");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblPassword);

        passwordField1 = new JPasswordField();
        passwordField1.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(passwordField1);


        JPanel panelButtons = new JPanel();
        panelButtons.setOpaque(false);

        okButton     = new JButton("Connexion");
        signupButton = new JButton("Signup");
        cancelButton = new JButton("Quitter");

        okButton.setActionCommand("LOGIN");
        signupButton.setActionCommand("SIGNUP");
        cancelButton.setActionCommand("CANCEL");

        panelButtons.add(okButton);
        panelButtons.add(signupButton);
        panelButtons.add(cancelButton);

        mainPanel.add(new JLabel());
        mainPanel.add(panelButtons);

        background.add(mainPanel);

        // Enter valide le login
        getRootPane().setDefaultButton(okButton);
    }

    @Override
    public String getLogin() {
        return textField1.getText();
    }

    @Override
    public String getPassword() {
        return new String(passwordField1.getPassword());
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setController(LoginController c) {
        okButton.addActionListener(c);
        signupButton.addActionListener(c);
        cancelButton.addActionListener(c);
    }

    @Override
    public void close() {
        dispose();
    }
}