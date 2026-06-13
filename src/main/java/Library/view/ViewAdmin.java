package Library.view;
import Library.controller.ControllerAdmin;
import Library.model.dao.DAODocument;
import Library.model.entity.Document;
import Library.model.entity.Livre;
import Library.model.entity.DVD;
import Library.model.entity.Magazine;

import java.util.ArrayList;
public interface ViewAdmin {
    void afficherLivres(ArrayList<Document> livres);
    void afficherDVDs(ArrayList<Document> dvds);
    void afficherMagazines(ArrayList<Document> magazines);
    void showMessage(String message);
    void showError(String message);
    void setController(ControllerAdmin c);
    void fermer();

    void setDAODocument(DAODocument dao);

    Document getSelectedDocument();

    Livre promptNouveauLivre();
    DVD promptNouveauDVD();
    Magazine promptNouveauMagazine();

    Livre promptModifierLivre(Livre l);
    DVD promptModifierDVD(DVD d);
    Magazine promptModifierMagazine(Magazine m);
}
