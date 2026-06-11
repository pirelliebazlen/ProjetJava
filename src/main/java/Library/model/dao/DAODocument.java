package Library.model.dao;
import Library.model.DataAccessLayer;
import Library.model.entity.Document;

import java.io.*;
import java.util.ArrayList;

public class DAODocument implements DataAccessLayer<Document> {

    private ArrayList<Document> documents;
    private int idCourant;
    private final File fichier;

    public DAODocument(File fichier) {
        this.fichier = fichier;
        if (fichier.exists()) {
            readData();
        } else {
            documents = new ArrayList<>();
            idCourant = 1;
            writeData();
        }
    }

    @Override
    public int add(Document doc) {
        doc.setId(idCourant++);
        documents.add(doc);
        writeData();
        return doc.getId();
    }

    @Override
    public boolean update(Document doc) {
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getId() == doc.getId()) {
                documents.set(i, doc);
                writeData();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        boolean deleted = documents.removeIf(d -> d.getId() == id);
        if (deleted) writeData();
        return deleted;
    }

    @Override
    public Document getById(int id) {
        for (Document d : documents)
            if (d.getId() == id) return d;
        return null;
    }

    @Override
    public ArrayList<Document> getList() { return documents; }


    private void writeData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeInt(idCourant);
            oos.writeObject(documents);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void readData() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(fichier))) {
            idCourant = ois.readInt();
            documents = (ArrayList<Document>) ois.readObject();
        } catch (Exception e) {
            documents = new ArrayList<>();
            idCourant = 1;
        }
    }
}