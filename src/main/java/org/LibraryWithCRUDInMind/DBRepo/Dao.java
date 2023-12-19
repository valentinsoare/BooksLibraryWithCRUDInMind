package org.LibraryWithCRUDInMind.DBRepo;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * Defined the methods in order for them to be override in the classes
 * that implements this interface.
 */
public interface Dao<T> {
    Optional<T> get(long id);
    Deque<T> findAll();
    boolean create(T t);
    boolean update(T t);
    int delete(long id, String nameOfTable);
    boolean checkIfElementExists(String title, String author);
    int[] createAll(List<T> elements);
    int[] updateAll(List<T> books);
    int numberOfRecordsPerTable(String nameOfTheTable);
    boolean checkIfTableExists(String nameOfTheTable);
    int[] deleteAll(int[] ids, String nameOfTheTable);
}
