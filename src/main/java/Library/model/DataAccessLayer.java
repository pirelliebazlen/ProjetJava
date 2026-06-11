package Library.model;

import java.util.ArrayList;

public interface DataAccessLayer<T> {
    int add(T item);
    boolean update(T item);
    boolean delete(int id);
    T getById(int id);
    ArrayList<T> getList();
}