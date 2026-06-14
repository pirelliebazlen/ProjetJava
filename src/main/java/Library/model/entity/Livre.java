package Library.model.entity;

import java.time.LocalDate;

public class Livre extends Document {

    private String auteur;
    private int nbPages;

    public Livre(String titre, int annee, LocalDate datePublication,
                 String auteur, int nbPages) {
        super(titre, annee, datePublication);
        this.auteur = auteur;
        this.nbPages = nbPages;
    }

    @Override public String getType()      { return "Livre"; }
    public String getAuteur()              { return auteur; }
    public void setAuteur(String auteur)   { this.auteur = auteur; }
    public int getNbPages()                { return nbPages; }
    public void setNbPages(int nbPages)    { this.nbPages = nbPages; }

    @Override
    public String toString() {
        return super.toString() + " — " + auteur;
    }
}