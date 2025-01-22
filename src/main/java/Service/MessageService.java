// Service layer package
package Service;

// For handling SQL exceptions
import java.sql.SQLException;
// List interface
import java.util.List;

// Data access object for accounts
import DAO.AccountDAO;
// Data access object for messages
import DAO.MessageDAO;
// Message model
import Model.Message;

public class MessageService {
    private final MessageDAO messageDAO;
    private final AccountDAO accountDAO;

    // Constructor with dependency injection
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    // Create a new message
    public Message createMessage(Message message) throws SQLException {
        // Validate message text and check if user exists
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()
                || message.getMessage_text().length() > 255) {
            return null;
        }
        if (accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    // Retrieve all messages
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    // Get a specific message by ID
    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.getMessageById(messageId);
    }

    // Delete a message by ID
    public Message deleteMessageById(int messageId) throws SQLException {
        return messageDAO.deleteMessageById(messageId);
    }

    // Update the text of a message
    public Message updateMessageText(int messageId, String newMessageText) throws SQLException {
        // Validate new message text and check if message exists
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage == null) {
            return null;
        }
        return messageDAO.updateMessageText(messageId, newMessageText);
    }

    // Get all messages by a specific account ID
    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}