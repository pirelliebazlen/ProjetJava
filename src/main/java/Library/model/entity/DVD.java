package Library.model.entity;

import java.time.LocalDate;

public class DVD extends Document {

    private static final long serialVersionUID = 1L;

    private int dureeMinutes;

    public DVD(String titre, int annee, LocalDate datePublication, int dureeMinutes) {
        super(titre, annee, datePublication);
        this.dureeMinutes = dureeMinutes;
    }

    @Override public String getType()             { return "DVD"; }
    public int getDureeMinutes()                  { return dureeMinutes; }
    public void setDureeMinutes(int dureeMinutes) { this.dureeMinutes = dureeMinutes; }

    @Override
    public String toString() {
        return super.toString() + " — " + dureeMinutes + " min";
    }
}