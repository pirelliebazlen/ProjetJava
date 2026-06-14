package Library.model.entity;

import java.time.LocalDate;

public class Magazine extends Document {

    private int numero;

    public Magazine(String titre, int annee, LocalDate datePublication, int numero) {
        super(titre, annee, datePublication);
        this.numero = numero;
    }

    @Override public String getType()    { return "Magazine"; }
    public int getNumero()               { return numero; }
    public void setNumero(int numero)    { this.numero = numero; }

    @Override
    public String toString() {
        return super.toString() + " — n°" + numero;
    }
}
