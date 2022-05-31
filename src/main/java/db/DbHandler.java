package db;

import model.Person;
import model.Record;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbHandler {
    private static final String CONNECTION_STR = "jdbc:sqlite:C://sqlite/project.db";
    private static DbHandler instance = null;

    private final Connection connection;

    public DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CONNECTION_STR);
        createTables();
    }

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbHandler();
        }
        return instance;
    }

    private void createTables() {
        try (var statement = this.connection.createStatement()) {
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS persons (id INTEGER PRIMARY KEY NOT NULL," +
                "fullName TEXT NOT NULL," +
                "phoneNumber CHAR(11) NOT NULL," +
                "address TEXT NOT NULL)"
            );
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS records (id INTEGER PRIMARY KEY NOT NULL," +
                "personId INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "payment INTEGER NOT NULL," +
                "date CHAR(10) NOT NULL," +
                "monthOfPayment INTEGER NOT NULL," +
                "purchaseDeadline INTEGER NOT NULL, " +
                "cost INTEGER NOT NULL, " +
                "organization TEXT NOT NULL, " +
                "FOREIGN KEY (personId) REFERENCES persons(id))"
            );
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<Person> getPersonsList() {
        try (var statement = this.connection.createStatement()) {
            var persons = new ArrayList<Person>();
            var result = statement.executeQuery("SELECT id, fullName, phoneNumber, address FROM persons");
            while (result.next()) {
                persons.add(new Person(
                        result.getInt("id"),
                        result.getString("fullName"),
                        result.getString("phoneNumber"),
                        result.getString("address")
                    )
                );
            }
            return persons;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Person getPerson(int id) {
        try (var statement = this.connection.prepareStatement(
                "SELECT id, fullName, phoneNumber, address FROM persons WHERE id = ?"
        )) {
            statement.setObject(1, id);
            var result = statement.executeQuery();
            return new Person(
                result.getInt("id"),
                result.getString("fullName"),
                result.getString("phoneNumber"),
                result.getString("address")
            );
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void addPerson(Person person) {
        try (var statement = this.connection.prepareStatement(
                "INSERT INTO persons(id, fullName, phoneNumber, address) " +
                        "VALUES(?, ?, ?, ?)")) {
            statement.setObject(1, person.getId());
            statement.setObject(2, person.getFullName());
            statement.setObject(3, person.getPhoneNumber());
            statement.setObject(4, person.getAddress());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int id) {
        try (var statement = this.connection.prepareStatement(
                "DELETE FROM persons WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<Record> getRecordsList() {
        try (var statement = this.connection.createStatement()) {
            var persons = new ArrayList<Record>();
            var result = statement.executeQuery(
                    "SELECT id, personId, name, payment, date, monthOfPayment, cost, organization FROM records"
            );
            while (result.next()) {
                persons.add(new Record(
                                result.getInt("id"),
                                getPerson(result.getInt("personId")),
                                result.getString("name"),
                                result.getInt("payment"),
                                result.getString("date"),
                                result.getInt("monthOfPayment"),
                                result.getInt("cost"),
                                result.getString("organization")
                        )
                );
            }
            return persons;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void addRecord(Record record) {
        try (var statement = this.connection.prepareStatement(
                "INSERT INTO records(id, personId, name, payment, date, monthOfPayment, purchaseDeadline, cost, organization) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, record.getId());
            statement.setObject(2, record.getPerson().getId());
            statement.setObject(3, record.getName());
            statement.setObject(4, record.getPayment());
            statement.setObject(5, record.getDate());
            statement.setObject(6, record.getMonthOfPayment());
            statement.setObject(7, record.getPurchaseDeadline());
            statement.setObject(8, record.getCost());
            statement.setObject(9, record.getOrganization());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(int id) {
        try (var statement = this.connection.prepareStatement(
                "DELETE FROM records WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
