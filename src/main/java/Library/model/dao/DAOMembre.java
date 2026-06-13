package Library.model.dao;

import Library.model.DataAccessLayer;
import Library.model.entity.Membre;

import java.io.*;
import java.util.ArrayList;

public class DAOMembre implements DataAccessLayer<Membre> {

    private ArrayList<Membre> membres;
    private int idCourant;
    private final File fichier;

    public DAOMembre(File fichier) {
        this.fichier = fichier;
        if (fichier.exists()) {
            readData();
        } else {
            membres = new ArrayList<>();
            idCourant = 1;
            writeData();
        }
    }

    @Override
    public int add(Membre membre) {
        membre.setId(idCourant++);
        membres.add(membre);
        writeData();
        return membre.getId();
    }

    @Override
    public boolean update(Membre membre) {
        for (int i = 0; i < membres.size(); i++) {
            if (membres.get(i).getId() == membre.getId()) {
                membres.set(i, membre);
                writeData();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        boolean deleted = membres.removeIf(m -> m.getId() == id);
        if (deleted) writeData();
        return deleted;
    }

    @Override
    public Membre getById(int id) {
        for (Membre m : membres)
            if (m.getId() == id) return m;
        return null;
    }

    public Membre getByLogin(String login) {
        for (Membre m : membres)
            if (m.getLogin().equals(login)) return m;
        return null;
    }

    @Override
    public ArrayList<Membre> getList() { return membres; }

    private void writeData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeInt(idCourant);
            oos.writeObject(membres);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void readData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))){
            idCourant = ois.readInt();
            membres = (ArrayList<Membre>) ois.readObject();
        } catch (Exception e) {
            membres = new ArrayList<>();
            idCourant = 1;
        }
    }
}