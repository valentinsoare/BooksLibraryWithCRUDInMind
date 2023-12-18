package org.LibraryWithCRUDInMind;

import org.LibraryWithCRUDInMind.DBRepo.BookDAO;
import org.LibraryWithCRUDInMind.DBRepo.Dao;
import org.LibraryWithCRUDInMind.model.Book;

import java.util.Deque;

public class App {
    public static void main( String[] args ) {
        Dao<Book> bookDao = new BookDAO();

        /*
         *  We get all the records from the BOOK table.
         */
        Deque<Book> all = bookDao.findAll();
        all.forEach(e -> System.out.printf("%n%s", e));

        /*
         * With this we check if a table exists in the DB.
         */
        boolean b = bookDao.checkIfTableExists("BOOK");
        System.out.printf("%nBOOK table exists ? %s", b);

        /*
         * Number of records per table.
         */
        int i = bookDao.numberOfRecordsPerTable("BOOK");
        System.out.printf("%n%s", i);
    }
}
