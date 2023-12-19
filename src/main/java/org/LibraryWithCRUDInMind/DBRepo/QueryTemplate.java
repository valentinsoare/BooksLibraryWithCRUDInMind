package org.LibraryWithCRUDInMind.DBRepo;

import java.sql.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This is a generic class made with tempalte method pattern in mind
 * in order to streamline the CRUD app implementation.
 */
public abstract class QueryTemplate<T> extends ConnectionToDb {
    protected QueryTemplate() {}

    public Deque<T> queryForAllToAList(String sql) {
        Deque<T> items = new LinkedList<>();

        try (Connection connectionToDB = ConnectionToDb.getAlreadyOpenConnection();
             Statement statement = connectionToDB.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                items.offer(mapItem(resultSet));
            }
        } catch (SQLException e) {
            System.out.printf("%sERROR [queryForAList] - %s", " ".repeat(2), e.getMessage());
        }

        return items;
    }
    public Optional<T> queryForAnElement(String sql, long id) {
        try (Connection connection = ConnectionToDb.getAlreadyOpenConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapItem(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.printf("%sERROR [queryForAnElement] - %s", " ".repeat(2), e.getMessage());
        }

        return Optional.empty();
    }
    public boolean toCheckIfAnElementExist(String[] args, String sql) {
        try (Connection connection = ConnectionToDb.getAlreadyOpenConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {;

            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setString((i + 1), args[i]);
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            System.out.printf("%sERROR [toCheck] - %s", " ".repeat(2), e.getMessage());
        }

        return false;
    }
    public int countNumberOfRecordsFromATable(String sql) {
        Connection connection = ConnectionToDb.getAlreadyOpenConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return extractIntegerFromResultSet(resultSet, "numberOfRecords");
            }
        } catch (SQLException e) {
            System.out.printf("%sERROR [countNumberOfRecordsFromATable] - %s", " ".repeat(2), e.getMessage());
        } finally {
            close();
        }

        return -1;
    }
    public boolean implementCreateAndUpdateElement(String[] attributes, String sql, boolean toCreate) {
        Connection connection = ConnectionToDb.getAlreadyOpenConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int i = 0, j = 1;

            if (!toCreate) {
                preparedStatement.setLong(j++, Long.parseLong(attributes[i++]));
            } else {
                i++;
            }

            for (; i < attributes.length; i++, j++) {
                preparedStatement.setString(j, attributes[i]);
            }

            if (executeStatementAndCheckIfSuccess(connection,
                    preparedStatement.executeUpdate()) > 0) {
                return true;
            }

        } catch (SQLException e) {
            System.out.printf("%sERROR [implementCreateAndUpdateElement] - %s", " ".repeat(2), e.getMessage());
        } finally {
            close();
        }

        return false;
    }
    public int[] implementingCreateAllOrUpdateAll(List<String[]> books, String sql, boolean ifCreateAll) {
        Connection connection = ConnectionToDb.getAlreadyOpenConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int i = 0, j = 1;

            for (String[] book : books) {
                if (!ifCreateAll) {
                    preparedStatement.setLong(j++, Long.parseLong(book[i]));
                } else {
                    ++i;
                }

                for (; i < book.length; i++) {
                    preparedStatement.setString(j, book[i]);
                }

                preparedStatement.addBatch();
            }

            return execPreparedStatementAsBatchAndCheck(connection,
                    preparedStatement.executeBatch());

        } catch (SQLException e) {
            System.out.printf("%sERROR [implementingCreateAllOrUpdateAll] - %s", " ".repeat(2), e.getMessage());
        } finally {
           close();
        }

        return new int[]{};
    }
    public int implementDelete(long id, String nameOfTheTable, String sql) {
        Connection connection = ConnectionToDb.getAlreadyOpenConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nameOfTheTable);
            preparedStatement.setLong(2, id);

            return executeStatementAndCheckIfSuccess(connection, preparedStatement.executeUpdate());

        } catch (SQLException e) {
            System.out.printf("%sERROR [implementDelete] - %s", " ".repeat(2), e.getMessage());
        } finally {
            close();
        }

        return -1;
    }

    /**
     * We implement these methods in the anonymous class defined in the BookDAO class.
     */
    public abstract T mapItem(ResultSet resultSet) throws SQLException;
    public abstract int extractIntegerFromResultSet(ResultSet resultSet, String columnName) throws SQLException;
    public abstract int executeStatementAndCheckIfSuccess(Connection connection, int resultOfCreateOrUpdate) throws SQLException;
    public abstract int[] execPreparedStatementAsBatchAndCheck(Connection connection, int[] resultsAfterExecutionOfBatch) throws SQLException;
}

