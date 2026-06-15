
package Library.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
public class DVDtest {

    private DVD dvd;

    @BeforeEach
    void setUp() {
        dvd = new DVD("Inception", 2010, LocalDate.of(2010, 7, 16), 148);
        dvd.setId(1);
    }

    //  Constructeur

    @Test
    void constructeur_valeurs_correctes() {
        assertEquals("Inception", dvd.getTitre());
        assertEquals(2010,        dvd.getAnnee());
        assertEquals(148,         dvd.getDureeMinutes());
        assertEquals(1,           dvd.getId());
        assertTrue(dvd.estDisponible());
    }

    // Getters / Setters

    @Test
    void setTitre_modifie_correctement() {
        dvd.setTitre("Interstellar");
        assertEquals("Interstellar", dvd.getTitre());
    }

    @Test
    void setDureeMinutes_modifie_correctement() {
        dvd.setDureeMinutes(169);
        assertEquals(169, dvd.getDureeMinutes());
    }

    @Test
    void setAnnee_modifie_correctement() {
        dvd.setAnnee(2014);
        assertEquals(2014, dvd.getAnnee());
    }

    // getType

    @Test
    void getType_retourne_DVD() {
        assertEquals("DVD", dvd.getType());
    }

    //  Empruntable

    @Test
    void dvd_disponible_par_defaut() {
        assertTrue(dvd.estDisponible());
    }

    @Test
    void emprunter_rend_indisponible() {
        dvd.emprunter();
        assertFalse(dvd.estDisponible());
    }

    @Test
    void retourner_rend_disponible() {
        dvd.emprunter();
        dvd.retourner();
        assertTrue(dvd.estDisponible());
    }

    // equals

    @Test
    void equals_meme_id_retourne_true() {
        DVD autre = new DVD("Titre différent", 1999, LocalDate.now(), 90);
        autre.setId(1);
        assertEquals(dvd, autre);
    }

    @Test
    void equals_id_different_retourne_false() {
        DVD autre = new DVD("Inception", 2010, LocalDate.now(), 148);
        autre.setId(99);
        assertNotEquals(dvd, autre);
    }

    @Test
    void equals_null_retourne_false() {
        assertNotEquals(null, dvd);
    }

    // toString

    @Test
    void toString_contient_titre_duree_type() {
        String s = dvd.toString();
        assertTrue(s.contains("Inception"));
        assertTrue(s.contains("148"));
        assertTrue(s.contains("DVD"));
    }
}