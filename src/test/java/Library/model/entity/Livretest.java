package Library.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Livretest {

    private Livre livre;

    @BeforeEach
    void setUp() {
        livre = new Livre("Harry Potter", 2001, LocalDate.of(2001, 6, 26), "J.K. Rowling", 300);
        livre.setId(1);
    }

    // Constructeur

    @Test
    void constructeur_valeurs_correctes() {
        assertEquals("Harry Potter", livre.getTitre());
        assertEquals(2001,           livre.getAnnee());
        assertEquals("J.K. Rowling", livre.getAuteur());
        assertEquals(300,            livre.getNbPages());
        assertEquals(1,              livre.getId());
        assertTrue(livre.estDisponible());
    }

    // ── Getters / Setters ───────────────────────────────────────────────────

    @Test
    void setTitre_modifie_correctement() {
        livre.setTitre("Le Seigneur des Anneaux");
        assertEquals("Le Seigneur des Anneaux", livre.getTitre());
    }

    @Test
    void setAuteur_modifie_correctement() {
        livre.setAuteur("Tolkien");
        assertEquals("Tolkien", livre.getAuteur());
    }

    @Test
    void setNbPages_modifie_correctement() {
        livre.setNbPages(500);
        assertEquals(500, livre.getNbPages());
    }

    @Test
    void setAnnee_modifie_correctement() {
        livre.setAnnee(1999);
        assertEquals(1999, livre.getAnnee());
    }

    @Test
    void setImagePath_modifie_correctement() {
        livre.setImagePath("/img/hp.jpg");
        assertEquals("/img/hp.jpg", livre.getImagePath());
    }

    // ── getType ─────────────────────────────────────────────────────────────

    @Test
    void getType_retourne_Livre() {
        assertEquals("Livre", livre.getType());
    }

    //  Empruntable

    @Test
    void livre_disponible_par_defaut() {
        assertTrue(livre.estDisponible());
    }

    @Test
    void emprunter_rend_indisponible() {
        livre.emprunter();
        assertFalse(livre.estDisponible());
    }

    @Test
    void retourner_rend_disponible() {
        livre.emprunter();
        livre.retourner();
        assertTrue(livre.estDisponible());
    }

    //  equals

    @Test
    void equals_meme_id_retourne_true() {
        Livre autre = new Livre("Titre différent", 1999, LocalDate.now(), "Autre", 100);
        autre.setId(1);
        assertEquals(livre, autre);
    }

    @Test
    void equals_id_different_retourne_false() {
        Livre autre = new Livre("Harry Potter", 2001, LocalDate.now(), "J.K. Rowling", 300);
        autre.setId(99);
        assertNotEquals(livre, autre);
    }

    @Test
    void equals_null_retourne_false() {
        assertNotEquals(null, livre);
    }

    // toString

    @Test
    void toString_contient_titre_et_type() {
        String s = livre.toString();
        assertTrue(s.contains("Harry Potter"));
        assertTrue(s.contains("Livre"));
    }
}
