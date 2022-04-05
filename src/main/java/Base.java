import org.sqlite.SQLiteDataSource;

import java.sql.*;

class Base {
    static private SQLiteDataSource dataSource;

    Base (String dbName) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbName);

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT NOT NULL," +
                    "pin TEXT NOT NULL," +
                    "balance INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean findCard (String card) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery("SELECT * FROM card WHERE number = " + card);
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertNewCard (String newCard, String pin) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
                statement.executeUpdate("INSERT INTO card (number, pin) VALUES " +
                        "(" + newCard + ", " +  pin + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean logIn(String card, String pin) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
             ResultSet res = statement.executeQuery("SELECT * FROM card WHERE number = "
                     + card + " AND pin = " + pin);
                return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int balance(String card) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery("SELECT balance FROM card WHERE " +
                    "number = " + card);
            res.next();
            return res.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // catch this error?
        }
    }

    public boolean addIncome(String card, int income) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            if (statement.executeUpdate("UPDATE card SET " +
                    "balance = balance +" + income + " WHERE number = " + card) == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean doTransfer(String card, String cardTo, int amount) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            con.setAutoCommit(false);
            Savepoint savepoint = con.setSavepoint();
            if (statement.executeUpdate("UPDATE card SET " +
                    "balance = balance -" + amount + " WHERE number = " + card) != 1 ||
                statement.executeUpdate("UPDATE card SET " +
                        "balance = balance +" + amount + " WHERE number = " + cardTo) != 1)
                con.rollback(savepoint);
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String card) {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {
            if (statement.executeUpdate("DELETE FROM card WHERE number = " + card) == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
