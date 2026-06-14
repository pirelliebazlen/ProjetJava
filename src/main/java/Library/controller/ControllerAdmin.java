package Library.controller;

import Library.model.dao.DAODocument;
import Library.model.entity.Document;
import Library.model.entity.DVD;
import Library.model.entity.Livre;
import Library.model.entity.Magazine;
import Library.view.ViewAdmin;
import Library.view.GUI.AdminFrame;
import Library.view.GUI.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ControllerAdmin implements ActionListener {


    private final ViewAdmin view;
    private final DAODocument daoDocument;

    public ControllerAdmin(AdminFrame view, DAODocument daoDocument) {
        this.view        = view;
        this.daoDocument = daoDocument;
        view.setDAODocument(daoDocument);
        this.view.setController(this);
    }

    public void run() {
        rafraichir();
        ((JFrame) view).setVisible(true);
    }

    private void rafraichir() {
        ArrayList<Document> livres    = new ArrayList<>();
        ArrayList<Document> dvds      = new ArrayList<>();
        ArrayList<Document> magazines = new ArrayList<>();

        for (Document d : daoDocument.getList()) {
            if (d instanceof Livre)        livres.add(d);
            else if (d instanceof DVD)     dvds.add(d);
            else if (d instanceof Magazine) magazines.add(d);
        }

        view.afficherLivres(livres);
        view.afficherDVDs(dvds);
        view.afficherMagazines(magazines);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("AJOUTER".equals(cmd)) {
            String[] types = {"Livre", "DVD", "Magazine"};
            String choix = (String) JOptionPane.showInputDialog(
                    (JFrame) view, "Quel type de document ?", "Ajouter",
                    JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
            if (choix == null) return;

            Document nouveau = null;

            if (choix.equals("Livre")) {
                nouveau = view.promptNouveauLivre();
            } else if (choix.equals("DVD")) {
                nouveau = view.promptNouveauDVD();
            } else {
                nouveau = view.promptNouveauMagazine();
            }

            if (nouveau != null) {
                daoDocument.add(nouveau);
                view.showMessage("Document ajouté !");
                rafraichir();
            }
        }

        if ("MODIFIER".equals(cmd)) {
            Document selectionne = view.getSelectedDocument();
            if (selectionne == null) {
                view.showError("Sélectionnez un document à modifier.");
                return;
            }

            Document modifie = null;

            if (selectionne instanceof Livre l) {
                modifie = view.promptModifierLivre(l);
            } else if (selectionne instanceof DVD d) {
                modifie = view.promptModifierDVD(d);
            } else if (selectionne instanceof Magazine m) {
                modifie = view.promptModifierMagazine(m);
            }

            if (modifie != null) {
                daoDocument.update(modifie);
                view.showMessage("Document modifié !");
                rafraichir();
            }
        }

        if ("SUPPRIMER".equals(cmd)) {
            Document selectionne = view.getSelectedDocument();
            if (selectionne == null) {
                view.showError("Sélectionnez un document à supprimer.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    (JFrame) view,
                    "Supprimer « " + selectionne.getTitre() + " » ?",
                    "Confirmer", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                daoDocument.delete(selectionne.getId());
                view.showMessage("Document supprimé !");
                rafraichir();
            }
        }

        if ("DECONNEXION".equals(cmd)) {
            ((JFrame) view).dispose();
            LoginFrame loginFrame = new LoginFrame();
            LoginController loginCtrl = new LoginController(loginFrame);
            loginCtrl.run();
        }
    }
}
