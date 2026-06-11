package Library.controller;

import Library.model.dao.DAODocument;
import Library.model.dao.DAOMembre;
import Library.model.entity.Document;
import Library.model.entity.Membre;
import Library.view.ViewMembre;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Library.view.GUI.LoginFrame;

public class ControllerMembre implements ActionListener {

    private final ViewMembre view;
    private final Membre membre;
    private final DAODocument daoDocument;
    private final DAOMembre daoMembre;

    public ControllerMembre(ViewMembre view, Membre membre,
                            DAODocument daoDocument, DAOMembre daoMembre) {
        this.view        = view;
        this.membre      = membre;
        this.daoDocument = daoDocument;
        this.daoMembre   = daoMembre;
        this.view.setController(this);
    }

    public void run() {
        rafraichir();
        ((JFrame) view).setVisible(true);
    }

    /** Met à jour les deux tableaux dans la vue. */
    private void rafraichir() {
        view.afficherNomMembre(membre.toString());
        view.afficherNbEmprunts(
                membre.getNbEmpruntsActuels(), Membre.MAX_EMPRUNTS);

        // Documents disponibles = tous les documents non empruntés
        Document[] disponibles = daoDocument.getList().stream()
                .filter(Document::estDisponible)
                .toArray(Document[]::new);
        view.afficherDocumentsDisponibles(disponibles);

        // Documents empruntés par ce membre
        view.afficherDocumentsEmpruntes(membre.getDocumentsEmpruntes());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("EMPRUNTER".equals(cmd)) {
            int docId = view instanceof Library.view.GUI.MembreFrame mf
                    ? mf.getSelectedDocumentDisponibleId()
                    : -1;

            if (docId == -1) {
                view.showError("Sélectionnez un document disponible.");
                return;
            }

            if (membre.tableauPlein()) {
                view.showError("Vous avez atteint la limite de "
                        + Membre.MAX_EMPRUNTS + " emprunts.");
                return;
            }

            Document doc = daoDocument.getById(docId);
            if (doc == null) return;

            boolean ok = membre.emprunter(doc);
            if (ok) {
                daoDocument.update(doc);   // persister le changement de dispo
                daoMembre.update(membre);  // persister l'état du membre
                view.showMessage("« " + doc.getTitre() + " » emprunté !");
            } else {
                view.showError("Ce document n'est plus disponible.");
            }
            rafraichir();
        }

        if ("RETOURNER".equals(cmd)) {
            int docId = view instanceof Library.view.GUI.MembreFrame mf
                    ? mf.getSelectedDocumentEmprunteId()
                    : -1;

            if (docId == -1) {
                view.showError("Sélectionnez un document à retourner.");
                return;
            }

            Document doc = daoDocument.getById(docId);
            if (doc == null) return;

            boolean ok = membre.retourner(doc);
            if (ok) {
                daoDocument.update(doc);
                daoMembre.update(membre);
                view.showMessage("« " + doc.getTitre() + " » retourné !");
            } else {
                view.showError("Ce document n'est pas dans vos emprunts.");
            }
            rafraichir();
        }

        if ("DECONNEXION".equals(cmd)) {
            ((JFrame) view).dispose();
            LoginFrame loginFrame = new LoginFrame();
            LoginController loginCtrl = new LoginController(loginFrame);
            loginCtrl.run();
        }
    }
}