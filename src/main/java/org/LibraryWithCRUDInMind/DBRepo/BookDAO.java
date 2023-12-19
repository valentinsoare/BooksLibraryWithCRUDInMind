package org.LibraryWithCRUDInMind.DBRepo;

import org.LibraryWithCRUDInMind.model.Book;

import java.sql.*;
import java.util.*;

/**
 * As you can see there is an interface called Dao which is a generic one,  where we defined all these methods and then override
 * them in BookDAO which extens ConnectionToDb in order to be possible to call getConnection.

 * As you can see from each method defined in the Dao interface we have two methods in BookDao. First version which
 * we commented is a naive approach without using design patterns, SOLID principles and how to implement CRUD guidelines.
 */
public class BookDAO extends ConnectionToDb implements Dao<Book> {
//    @Override
//    public Optional<Book> get(long id) {
//        String sqlQueryToSelectBookById = "SELECT id, title, author from BOOK where id = ?";
//
//        try (Connection connectionToDB = getConnection();
//             PreparedStatement preparedStatement = connectionToDB.prepareStatement(sqlQueryToSelectBookById)) {
//            preparedStatement.setLong(1, id);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    Book book = new Book();
//
//                    book.setId(resultSet.getLong("id"));
//                    book.setTitle(resultSet.getString("title"));
//                    book.setAuthor(resultSet.getString("author"));
//
//                    return Optional.of(book);
//                }
//            }
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        }
//
//        return Optional.empty();
//    }
    /**
     * This is a get method which in turn is returning an optional in order to get rid of null pointer exception
     * if it is the case. We search an item, which is a book, by id in the databsase.
     */
    @Override
    public Optional<Book> get(long id) {
        String sqlQueryToSelectBookById = "SELECT id, title, author from BOOK where id = ?";
        QueryTemplate<Book> qt = accessToMethods();

        return qt.queryForAnElement(sqlQueryToSelectBookById, id);

    }

    /**
     * Now here we have a method that deliver an anonymous class with several methods and this class exten the parent class
     * QueryTemplate with several methods and these methods can be called from the initial class where we define the methods.
     *
     * QueryTemplate is a generic class and an abstract one with several methods defined as abstract and also some of them
     * that actual do something and help us to run this CRUD application.
     *
     * This anonim class it is possible to implement with functional interfaces, but I felt this approach is a nicer one.
     */
    private QueryTemplate<Book> accessToMethods() {
        return new QueryTemplate<>() {

            /**
             * We execute a query and then we map result set to an item and in this case is a book with 3 attributes,
             * id a synthetic one and title, author were the business attributes.
             */
            public Book mapItem(ResultSet resultSet) throws SQLException {
                Book book = new Book();

                book.setId(resultSet.getLong("id"));
                book.setAuthor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));

                return book;
            }

            /**
             * Here we extract an integer which is a numberOFRecords in the table using a column name from a resultSet
             * after executing the query.
             */
            @Override
            public int extractIntegerFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
                if (resultSet.next()) return resultSet.getInt(columnName);
                return -1;
            }

            /**
             * We validate if resultSet is not null and return a boolean.
             */
            @Override
            public int executeStatementAndCheckIfSuccess(Connection connection, int resultOfCreateOrUpdate) throws SQLException {
                if (resultOfCreateOrUpdate > 0) {
                    connection.commit();
                    return resultOfCreateOrUpdate;
                }

                connection.rollback();

                return -1;
            }

            /**
             * We validate if a batch query was executing on any element.
             */
            public int[] execPreparedStatementAsBatchAndCheck(Connection connection, int[] resultsAfterExecutionOfBatch) throws SQLException {
                if (resultsAfterExecutionOfBatch.length > 0) {
                    connection.commit();
                    return resultsAfterExecutionOfBatch;
                }

                connection.rollback();

                return new int[] {};
            }
        };
    }
    /**
     *
     * With this we get all the records from the Book class.
     */
    @Override
    public Deque<Book> findAll() {
        String sqlQueryForASpecificBookById = "SELECT * FROM BOOK order by Id";
        QueryTemplate<Book> qt = accessToMethods();

        return qt.queryForAllToAList(sqlQueryForASpecificBookById);
    }
//    @Override
//    public Deque<Book> findAll() {
//        Deque<Book> books = new LinkedList<>();
//        String sqlQueryForASpecificBookById = "SELECT * FROM BOOK order by Id";
//
//        try (
//                Connection connectionToDB = getConnection();
//                Statement statement = connectionToDB.createStatement();
//                ResultSet resultSet = statement.executeQuery(sqlQueryForASpecificBookById)
//                ) {
//
//            while (resultSet.next()) {
//                Book book =  new Book();
//
//                book.setId(resultSet.getLong("id"));
//                book.setAuthor(resultSet.getString("author"));
//                book.setTitle(resultSet.getString("title"));
//
//                books.offer(book);
//            }
//
//            return books;
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        }
//
//       return books;
//    }
    /**
     * We have a table and with this we extract the number of records from a table.
     */
    @Override
    public int numberOfRecordsPerTable(String nameOfTheTable) {
        String nrOfRecords = "SELECT COUNT(*) AS numberOfRecords FROM $tableName";
        String queryForReplacement = nrOfRecords.replace("$tableName", nameOfTheTable);

        QueryTemplate<Book> template = accessToMethods();

        return  template.countNumberOfRecordsFromATable( queryForReplacement);
    }
//    @Override
//    public int numberOfRecordsPerTable(String nameOfTheTable) throws SQLException {
//        String nrOfRecords = "SELECT COUNT(*) AS numberOfRecords FROM $tableName";
//        String queryForReplacement = nrOfRecords.replace("$tableName", nameOfTheTable);
//
//
////        try (Connection connection = getConnection();
////             PreparedStatement preparedStmt = connection.prepareStatement(queryForReplacement, Statement.RETURN_GENERATED_KEYS)) {
////
////            try (ResultSet resultSet = preparedStmt.executeQuery()) {
////
////                if (resultSet.next()) {
////                    return resultSet.getInt("numberOfRecords");
////                }
////            }
////
////        } catch (SQLException e) {
////            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
////        }
////
//        return -1;
//    }
    /**
     * We check if a table exists in the database.
     */
    @Override
    public boolean checkIfTableExists(String nameOfTheTable) {
        String checkTableIfExists = "SHOW TABLES LIKE ?";
        QueryTemplate<Book> qt = accessToMethods();

        return qt.toCheckIfAnElementExist(new String[]{nameOfTheTable}, checkTableIfExists);
    }
//    @Override
//    public boolean checkIfTableExists(String nameOfTheTable) {
//        String checkTableIfExists = "SHOW TABLES LIKE ?";
//
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(checkTableIfExists)) {
//            preparedStatement.setString(1, nameOfTheTable);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                return resultSet.next();
//            }
//
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        }
//
//        return false;
//    }
    /**
     * Check if an item exists using all of its attributes except idm the synthetic one.
     */
    @Override
    public boolean checkIfElementExists(String title, String author) {
        String sqlQueryForCheckingExistence =
                "SELECT BOOK.title, BOOK.author from BOOK where BOOK.title = ? and BOOK.author = ?";

        QueryTemplate<Book> template = accessToMethods();
        return template.toCheckIfAnElementExist(new String[]{title, author}, sqlQueryForCheckingExistence);
    }
//    @Override
//    public boolean checkIfElementExists(String title, String author) {
//        String sqlQueryForCheckingExistence =
//                "SELECT BOOK.title, BOOK.author from BOOK where BOOK.title = ? and BOOK.author = ?";
//
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sqlQueryForCheckingExistence,
//                     new String[] {"id", "title", "author"})) {
//
//            preparedStatement.setString(1, title);
//            preparedStatement.setString(2, author);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next() && resultSet.getString("title") != null) {
//                    return true;
//                }
//            }
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        }
//
//        return false;
//    }
    /**
     * We have a list of elements, and we add them in the databsase.
     */
    @Override
    public int[] createAll(List<Book> elements) {
        String insertion = "INSERT INTO BOOK (TITLE, AUTHOR) VALUES (?, ?)";
        QueryTemplate<Book> template = accessToMethods();

        List<String[]> booksAsAttributes = elements.stream()
                .map(Book::getAttributesAsAnArray)
                .toList();

        int[] ints = template.implementingCreateAllOrUpdateAll(booksAsAttributes, insertion, true);
        return (ints.length == elements.size()) ? ints : new int[] {};
    }
//    @Override
//    public int[] createAll(List<Book> books) {
//        String insertion = "INSERT INTO BOOK (TITLE, AUTHOR) VALUES (?, ?)";
//        Connection connection = getConnection();
//
//        try (PreparedStatement preparedStmt =
//                     connection.prepareStatement(insertion)) {
//            connection.setAutoCommit(false);
//
//            for (Book b : books) {
//                preparedStmt.setString(1, b.getTitle());
//                preparedStmt.setString(2, b.getAuthor());
//
//                preparedStmt.addBatch();
//            }
//
//            int[] batchExecutionResult = preparedStmt.executeBatch();
//
//            if (batchExecutionResult.length == books.size()) {
//                connection.commit();
//                return batchExecutionResult;
//            }
//
//            connection.rollback();
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException f) {
//                System.out.printf("%sERROR [autoCommitSetToTrue] - %s", " ".repeat(2), f.getMessage());
//            } finally {
//                close();
//            }
//        }
//
//        return new int[] {};
//    }
    /**
     * Add only an item in DB.
     */
    @Override
    public boolean create(Book book) {
        String insertARecord = "INSERT INTO BOOK (TITLE, AUTHOR) VALUES (?, ?)";
        String[] elements = book.getAttributesAsAnArray();

        QueryTemplate<Book> template = accessToMethods();
        return template.implementCreateAndUpdateElement(elements, insertARecord, true);
    }
    @Override
    public boolean update(Book book) {
        String sqlForUpdatingElement = "UPDATE BOOK SET BOOK.title = ?, BOOK.author = ? where BOOK.id = ?";
        String[] elements = book.getAttributesAsAnArray();

        QueryTemplate<Book> template = accessToMethods();
        return template.implementCreateAndUpdateElement(elements, sqlForUpdatingElement, false);
    }
    /**
     * We create elements with exactly the values we want for them attributes and then we update the DB. Like if we need the book
     * with id 5 to be updated, we made an oobject of book with id of 5 and our attributes values and then we push this element
     * in the database.
     */
    @Override
    public int[] updateAll(List<Book> books) {
        String updateAll = "UPDATE BOOK SET BOOK.title = ?, BOOK.author = ? where BOOK.id = ?";

        if (books.isEmpty()) return new int[] {};
        QueryTemplate<Book> t = accessToMethods();

        List<String[]> booksAsAttributes = books.stream()
                .map(Book::getAttributesAsAnArray)
                .toList();

        int[] ints = t.implementingCreateAllOrUpdateAll(booksAsAttributes, updateAll, false);
        return (ints.length == books.size()) ? ints : new int[] {};
    }
    //    @Override
//    public int[] updateAll(List<Book> books, long[] ids) {
//        String updateAll = "UPDATE BOOK SET BOOK.title = ?, BOOK.author = ? where BOOK.id = ?";
//
//        if (books.isEmpty() || (books.size() != ids.length)) return new int[] {};
//        Connection connection = getConnection();
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(updateAll)) {
//            connection.setAutoCommit(false);
//
//            for (int i = 0; i < books.size(); i++) {
//                Book b = books.get(i);
//
//                preparedStatement.setString(1, b.getTitle());
//                preparedStatement.setString(2, b.getAuthor());
//                preparedStatement.setLong(3, ids[i]);
//
//                preparedStatement.addBatch();
//            }
//
//            int[] resultAfterBatch = preparedStatement.executeBatch();
//
//            if (resultAfterBatch.length == books.size()) {
//                connection.commit();
//                return resultAfterBatch;
//            }
//
//            connection.rollback();
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException f) {
//                System.out.printf("%sERROR [autoCommitSetToTrue] - %s", " ".repeat(2), f.getMessage());
//            } finally {
//                close();
//            }
//        }
//
//        return new int[] {};
//    }
    /**
     * We delete an item by id from the supplied table.
     */
    @Override
    public int delete(long id, String nameOfTable) {
        String recordDeletion = "DELETE from ? where id = ?";

        QueryTemplate<Book> template = accessToMethods();

        return template.implementDelete(id, nameOfTable, recordDeletion);
    }
//    @Override
//    public int delete(int id, String nameOfTable) {
//        String recordDeletion = "DELETE from ? where ?.id = ?";
//        Connection connection = getConnection();
//
//        if ((id <= 0) || (numberOfRecordsPerTable(nameOfTable) < id)) {
//            return -1;
//        }
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(recordDeletion)) {
//            connection.setAutoCommit(false);
//
//            preparedStatement.setString(1, nameOfTable);
//            preparedStatement.setLong(2, id);
//
//            int numberOfRowsAffected = preparedStatement.executeUpdate();
//
//            if (numberOfRowsAffected > 0) {
//                connection.commit();
//
//                return numberOfRowsAffected;
//            }
//
//            connection.rollback();
//        } catch (SQLException e) {
//            System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException f) {
//                System.out.printf("%sERROR [autoCommitSetToTrue] - %s", " ".repeat(2), f.getMessage());
//            } finally {
//                close();
//            }
//        }
//
//        return 0;
//    }
    /**
     * Just for the fun of it this deleteAll method will remain like this, in a raw format, without anything in
     * the template pattern. We are going into the wild with this :)).
     */
    @Override
    public int[] deleteAll(int[] ids, String nameOfTheTable) {
        String initialDeleteSQL = "DELETE FROM $tableName where id = ?";

        if (checkIfTableExists(nameOfTheTable)) {
            String finalDeleteSQL = initialDeleteSQL.replace("$tableName", nameOfTheTable);

            Connection connection = getAlreadyOpenConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(finalDeleteSQL)) {

                connection.setAutoCommit(false);

                for (int id : ids) {
                    preparedStatement.setLong(1, id);
                    preparedStatement.addBatch();
                }

                int[] resultAfterDeleteAll = preparedStatement.executeBatch();

                if (resultAfterDeleteAll.length > 0) {
                    connection.commit();
                    return resultAfterDeleteAll;
                }

                connection.rollback();
            } catch (SQLException e) {
                System.out.printf("%sERROR - %s", " ".repeat(2), e.getMessage());
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException f) {
                    System.out.printf("%sERROR [autoCommitSetToTrue] - %s", " ".repeat(2), f.getMessage());
                } finally {
                    close();
                }
            }
        }

        return new int[] {};
    }
}