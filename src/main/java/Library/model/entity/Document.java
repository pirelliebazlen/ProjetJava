package Library.model.entity;

import Library.model.Empruntable;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Document implements Serializable, Empruntable {

    private int id;
    private String titre;
    private int annee;
    private LocalDate datePublication;
    private boolean disponible;
    private String imagePath;

    public Document(String titre, int annee, LocalDate datePublication) {
        this.titre = titre;
        this.annee = annee;
        this.datePublication = datePublication;
        this.disponible = true;
    }

    @Override public void emprunter()
    { this.disponible = false; }
    @Override public void retourner()
    { this.disponible = true; }
    @Override public boolean estDisponible()
    { return disponible; }

    public int getId()
    { return id; }
    public void setId(int id)
    { this.id = id; }
    public String getTitre()
    { return titre; }
    public void setTitre(String titre)
    { this.titre = titre; }
    public int getAnnee()
    { return annee; }
    public void setAnnee(int annee)
    { this.annee = annee; }
    public LocalDate getDatePublication()
    { return datePublication; }
    public void setDatePublication(LocalDate d)
    { this.datePublication = d; }
    public String getImagePath()
    { return imagePath; }
    public void setImagePath(String imagePath)
    { this.imagePath = imagePath; }

    public abstract String getType();

    @Override
    public String toString() {
        return "[" + id + "] " + titre + " (" + annee + ") — " + getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document d)) return false;
        return id == d.id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}