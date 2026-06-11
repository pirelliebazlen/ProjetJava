package Library.model.dao;

import Library.model.entity.DVD;
import Library.model.entity.Livre;
import Library.model.entity.Magazine;
import Library.model.entity.Document;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DAODocumentTest {

    private DAODocument dao;
    private final File fichier = new File("test_documents.dat");

    @BeforeEach
    void setUp() {
        if (fichier.exists()) fichier.delete();
        dao = new DAODocument(fichier);
    }

    @AfterEach
    void tearDown() {
        if (fichier.exists()) fichier.delete();
    }

    private Livre creerLivre() {
        return new Livre("Clean Code", 2008,
                LocalDate.of(2008, 1, 1),
                "Robert C. Martin", 431);
    }

    private DVD creerDVD() {
        return new DVD("Inception", 2010,
                LocalDate.of(2010, 7, 16), 148);
    }

    private Magazine creerMagazine() {
        return new Magazine("National Geographic", 2024,
                LocalDate.of(2024, 3, 1), 42);
    }

    @Test
    void testAddLivre() {
        int id = dao.add(creerLivre());
        assertTrue(id > 0);
        assertEquals(1, dao.getList().size());
    }

    @Test
    void testAddDVD() {
        int id = dao.add(creerDVD());
        assertTrue(id > 0);
        assertEquals(1, dao.getList().size());
    }

    @Test
    void testAddMagazine() {
        int id = dao.add(creerMagazine());
        assertTrue(id > 0);
        assertEquals(1, dao.getList().size());
    }

    @Test
    void testGetById() {
        int id = dao.add(creerLivre());
        Document d = dao.getById(id);
        assertNotNull(d);
        assertEquals("Clean Code", d.getTitre());
    }

    @Test
    void testGetByIdInexistant() {
        assertNull(dao.getById(999));
    }

    @Test
    void testUpdate() {
        int id = dao.add(creerLivre());
        Document d = dao.getById(id);
        d.setTitre("Nouveau titre");
        assertTrue(dao.update(d));
        assertEquals("Nouveau titre", dao.getById(id).getTitre());
    }

    @Test
    void testUpdateInexistant() {
        Livre l = creerLivre();
        l.setId(999);
        assertFalse(dao.update(l));
    }

    @Test
    void testDelete() {
        int id = dao.add(creerLivre());
        assertTrue(dao.delete(id));
        assertNull(dao.getById(id));
        assertEquals(0, dao.getList().size());
    }

    @Test
    void testDeleteInexistant() {
        assertFalse(dao.delete(999));
    }

    @Test
    void testIdsUniques() {
        int id1 = dao.add(creerLivre());
        int id2 = dao.add(creerDVD());
        int id3 = dao.add(creerMagazine());
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
    }

    @Test
    void testPersistanceApresReouverture() {
        dao.add(creerLivre());
        dao.add(creerDVD());
        DAODocument dao2 = new DAODocument(fichier);
        assertEquals(2, dao2.getList().size());
    }

    @Test
    void testGetList() {
        dao.add(creerLivre());
        dao.add(creerDVD());
        dao.add(creerMagazine());
        assertEquals(3, dao.getList().size());
    }
}