// Data Access Object package
package DAO;

// JDBC classes for database interaction
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Account model
import Model.Account;
// Utility for database connection
import Util.ConnectionUtil;

public class AccountDAO {
    // Create a new account in the database
    public Account createAccount(Account account) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO account (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                account.setAccount_id(rs.getInt(1));
            }
            return account;

        }
    }

    // Retrieve an account by username
    public Account getAccountByUsername(String username) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM account WHERE username = ?")) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    // Retrieve an account by account ID
    public Account getAccountById(int accountId) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("SELECT * FROM account WHERE account_id = ?")) {

            statement.setInt(1, accountId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}