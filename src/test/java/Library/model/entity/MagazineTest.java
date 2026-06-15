package Library.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class MagazineTest {

    private Magazine magazine;

    @BeforeEach
    void setUp() {
        magazine = new Magazine("National Geographic", 2023, LocalDate.of(2023, 1, 1), 42);
        magazine.setId(1);
    }

    //Constructeur

    @Test
    void constructeur_valeurs_correctes() {
        assertEquals("National Geographic", magazine.getTitre());
        assertEquals(2023, magazine.getAnnee());
        assertEquals(42,   magazine.getNumero());
        assertEquals(1,    magazine.getId());
        assertTrue(magazine.estDisponible());
    }

    // Getters / Setters

    @Test
    void setTitre_modifie_correctement() {
        magazine.setTitre("Science & Vie");
        assertEquals("Science & Vie", magazine.getTitre());
    }

    @Test
    void setNumero_modifie_correctement() {
        magazine.setNumero(99);
        assertEquals(99, magazine.getNumero());
    }

    @Test
    void setAnnee_modifie_correctement() {
        magazine.setAnnee(2024);
        assertEquals(2024, magazine.getAnnee());
    }

    //  getType

    @Test
    void getType_retourne_Magazine() {
        assertEquals("Magazine", magazine.getType());
    }

    // Empruntable

    @Test
    void magazine_disponible_par_defaut() {
        assertTrue(magazine.estDisponible());
    }

    @Test
    void emprunter_rend_indisponible() {
        magazine.emprunter();
        assertFalse(magazine.estDisponible());
    }

    @Test
    void retourner_rend_disponible() {
        magazine.emprunter();
        magazine.retourner();
        assertTrue(magazine.estDisponible());
    }

    //equals

    @Test
    void equals_meme_id_retourne_true() {
        Magazine autre = new Magazine("Titre différent", 1999, LocalDate.now(), 1);
        autre.setId(1);
        assertEquals(magazine, autre);
    }

    @Test
    void equals_id_different_retourne_false() {
        Magazine autre = new Magazine("National Geographic", 2023, LocalDate.now(), 42);
        autre.setId(99);
        assertNotEquals(magazine, autre);
    }

    @Test
    void equals_null_retourne_false() {
        assertNotEquals(null, magazine);
    }

    //toString

    @Test
    void toString_contient_titre_numero_type() {
        String s = magazine.toString();
        assertTrue(s.contains("National Geographic"));
        assertTrue(s.contains("42"));
        assertTrue(s.contains("Magazine"));
    }
}