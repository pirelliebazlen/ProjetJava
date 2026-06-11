package Library.view;

import Library.controller.ControllerMembre;
import Library.model.entity.Document;

public interface ViewMembre {
    void afficherDocumentsDisponibles(Document[] documents);
    void afficherDocumentsEmpruntes(Document[] documents);
    void afficherNomMembre(String nom);
    void afficherNbEmprunts(int nb, int max);
    void showMessage(String message);
    void showError(String message);
    void setController(ControllerMembre c);
    void fermer();
}