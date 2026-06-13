package Library.controller;

import Library.model.authentication.PropertiesAuthenticator;
import Library.model.dao.DAODocument;
import Library.model.dao.DAOMembre;
import Library.model.entity.Membre;
import Library.view.GUI.AdminFrame;
import Library.view.GUI.LoginFrame;
import Library.view.GUI.MembreFrame;
import Library.view.ViewLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;

public class LoginController implements ActionListener {

    private final ViewLogin view;
    private final PropertiesAuthenticator auth;
    private final DAOMembre daoMembre;
    private final DAODocument daoDocument;

    public LoginController(ViewLogin view) {
        this.view        = view;
        this.auth        = new PropertiesAuthenticator("src/main/resources/users.properties");
        this.daoMembre   = new DAOMembre(new File("membres.dat"));
        this.daoDocument = new DAODocument(new File("documents.dat"));

        this.view.setController(this);
    }

    public void run() {
        ((JFrame) view).setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("LOGIN".equals(cmd)) {
            String login    = view.getLogin();
            String password = view.getPassword();

            if (login.isBlank() || password.isBlank()) {
                view.showError("Veuillez remplir tous les champs.");
                return;
            }

            if (!auth.authenticate(login, password)) {
                view.showError("Login ou mot de passe incorrect.");
                return;
            }

            String role = auth.getRole(login);
            view.close();

            if ("admin".equals(role)) {
                AdminFrame adminFrame = new AdminFrame();
                ControllerAdmin ctrl  = new ControllerAdmin(adminFrame, daoDocument);
                ctrl.run();
            } else {
                Membre membre = daoMembre.getByLogin(login);
                if (membre == null) {
                    membre = new Membre(0, login, login, login, LocalDate.now());
                    daoMembre.add(membre);
                }
                MembreFrame membreFrame = new MembreFrame();
                ControllerMembre ctrl   = new ControllerMembre(
                        membreFrame, membre, daoDocument, daoMembre);
                ctrl.run();
            }
        }

        if ("SIGNUP".equals(cmd)) {
            String login    = view.getLogin();
            String password = view.getPassword();

            if (login.isBlank() || password.isBlank()) {
                view.showError("Veuillez remplir login et mot de passe.");
                return;
            }

            boolean ok = auth.saveUser(login, password);
            if (ok) {
                JOptionPane.showMessageDialog((JFrame) view,
                        "Compte créé ! Vous pouvez vous connecter.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.showError("Ce login existe déjà.");
            }
        }

        if ("CANCEL".equals(cmd) || "QUITTER".equals(cmd)) {
            System.exit(0);
        }
    }


}
