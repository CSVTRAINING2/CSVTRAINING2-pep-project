// Service layer package
package Service;

// SQL exception handling
import java.sql.SQLException;

// For handling account-related exceptions
import javax.security.auth.login.AccountException;

// Data access object for accounts
import DAO.AccountDAO;

// Account model
import Model.Account;

public class AccountService {

    private final AccountDAO accountDAO;

    // Constructor with dependency injection
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Register a new account
    public Account registerAccount(Account account) throws SQLException, AccountException {
        // Validate username and password
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new AccountException("Username cannot be blank.");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new AccountException("Password must be at least 4 characters long.");
        }
        // Check if username already exists
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        // Create the account
        return accountDAO.createAccount(account);
    }

    // Authenticate user login
    public Account login(Account account) throws SQLException {
        Account storedAccount = accountDAO.getAccountByUsername(account.getUsername());
        // Check if account exists and password matches
        if (storedAccount == null || !storedAccount.getPassword().equals(account.getPassword())) {
            return null;
        }

        return storedAccount;
    }
}