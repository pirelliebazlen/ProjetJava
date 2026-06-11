package Library.model.entity;

import java.time.LocalDate;
import java.io.Serializable;
public class Membre implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MAX_EMPRUNTS = 5;

    private int id;
    private String login;
    private String nom;
    private String prenom;
    private LocalDate dateInscription;

    // Tableau de taille fixe MAX_EMPRUNTS (null = slot vide)
    private final Document[] documentsEmpruntes;

    public Membre(int id, String login, String nom,
                  String prenom, LocalDate dateInscription) {
        this.id = id;
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.dateInscription = dateInscription;
        this.documentsEmpruntes = new Document[MAX_EMPRUNTS];
    }

    public boolean emprunter(Document doc) {
        if (!doc.estDisponible()) return false;

        for (int i = 0; i < MAX_EMPRUNTS; i++) {
            if (documentsEmpruntes[i] == null) {
                documentsEmpruntes[i] = doc;
                doc.emprunter();
                return true;
            }
        }
        return false;   // tableau plein
    }

    public boolean retourner(Document doc) {
        for (int i = 0; i < MAX_EMPRUNTS; i++) {
            if (documentsEmpruntes[i] != null
                    && documentsEmpruntes[i].equals(doc)) {
                documentsEmpruntes[i] = null;
                doc.retourner();
                return true;
            }
        }
        return false;
    }

    public int getNbEmpruntsActuels() {
        int count = 0;
        for (Document d : documentsEmpruntes)
            if (d != null) count++;
        return count;
    }

    public boolean tableauPlein() {
        return getNbEmpruntsActuels() >= MAX_EMPRUNTS;
    }


    public int getId()
    { return id; }
    public void setId(int id)
    { this.id = id; }
    public String getLogin()
    { return login; }
    public String getNom()
    { return nom; }
    public void setNom(String nom)
    { this.nom = nom; }
    public String getPrenom()
    { return prenom; }
    public void setPrenom(String prenom)
    { this.prenom = prenom; }
    public LocalDate getDateInscription()
    { return dateInscription; }
    public Document[] getDocumentsEmpruntes()
    { return documentsEmpruntes; }

    @Override
    public String toString() { return prenom + " " + nom; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Membre m)) return false;
        return id == m.id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}