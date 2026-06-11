package Library.model.dao;

import Library.model.entity.Membre;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DAOMembreTest {

    private DAOMembre dao;
    private final File fichier = new File("test_membres.dat");

    @BeforeEach
    void setUp() {
        if (fichier.exists()) fichier.delete();
        dao = new DAOMembre(fichier);
    }

    @AfterEach
    void tearDown() {
        if (fichier.exists()) fichier.delete();
    }

    private Membre creerMembre(String login) {
        return new Membre(0, login, "Dupont", "Alice", LocalDate.of(2024, 1, 1));
    }

    @Test
    void testAdd() {
        int id = dao.add(creerMembre("alice"));
        assertTrue(id > 0);
        assertEquals(1, dao.getList().size());
    }

    @Test
    void testGetById() {
        int id = dao.add(creerMembre("alice"));
        Membre m = dao.getById(id);
        assertNotNull(m);
        assertEquals("alice", m.getLogin());
    }

    @Test
    void testGetByIdInexistant() {
        assertNull(dao.getById(999));
    }

    @Test
    void testGetByLogin() {
        dao.add(creerMembre("alice"));
        Membre m = dao.getByLogin("alice");
        assertNotNull(m);
        assertEquals("alice", m.getLogin());
    }

    @Test
    void testGetByLoginInexistant() {
        assertNull(dao.getByLogin("inconnu"));
    }

    @Test
    void testUpdate() {
        int id = dao.add(creerMembre("alice"));
        Membre m = dao.getById(id);
        m.setNom("Martin");
        assertTrue(dao.update(m));
        assertEquals("Martin", dao.getById(id).getNom());
    }

    @Test
    void testUpdateInexistant() {
        Membre m = creerMembre("alice");
        m.setId(999);
        assertFalse(dao.update(m));
    }

    @Test
    void testDelete() {
        int id = dao.add(creerMembre("alice"));
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
        int id1 = dao.add(creerMembre("alice"));
        int id2 = dao.add(creerMembre("bob"));
        assertNotEquals(id1, id2);
    }

    @Test
    void testPersistanceApresReouverture() {
        dao.add(creerMembre("alice"));
        dao.add(creerMembre("bob"));
        DAOMembre dao2 = new DAOMembre(fichier);
        assertEquals(2, dao2.getList().size());
    }

    @Test
    void testGetList() {
        dao.add(creerMembre("alice"));
        dao.add(creerMembre("bob"));
        assertEquals(2, dao.getList().size());
    }
}