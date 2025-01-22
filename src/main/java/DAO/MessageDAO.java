// Data Access Object package
package DAO;

// JDBC classes for database interaction
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// ArrayList for storing multiple messages
import java.util.ArrayList;
// List interface
import java.util.List;

// Message model
import Model.Message;
// Utility for database connection
import Util.ConnectionUtil;

public class MessageDAO {
    // Create a new message in the database
    public Message createMessage(Message message) throws SQLException {
        // Implementation details
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt("message_id"));
            }

            return message;
        }
    }

    // Delete a message by its ID
    public Message deleteMessageById(int messageId) throws SQLException {
        // Implementation details
        Message deletedMessage = getMessageById(messageId);

        if (deletedMessage != null) {
            try (Connection connection = ConnectionUtil.getConnection();
                    PreparedStatement statement = connection
                            .prepareStatement("DELETE FROM message WHERE message_id = ?")) {

                statement.setInt(1, messageId);
                statement.executeUpdate();
            }
        }
        return deletedMessage;
    }

    // Retrieve all messages from the database
    public List<Message> getAllMessages() throws SQLException {
        // Implementation details
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM message");
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        }
        return messages;
    }

    // Retrieve a specific message by its ID
    public Message getMessageById(int messageId) throws SQLException {
        // Implementation details
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("SELECT * FROM message WHERE message_id = ?")) {

            statement.setInt(1, messageId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                            rs.getLong("time_posted_epoch"));
                }
            }
        }
        return null;
    }

    // Update the text of a specific message
    public Message updateMessageText(int messageId, String newMessageText) throws SQLException {
        // Implementation details
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?")) {

            statement.setString(1, newMessageText);
            statement.setInt(2, messageId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }
        }
        return getMessageById(messageId);
    }

    // Retrieve all messages posted by a specific account
    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        // Implementation details
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection
                        .prepareStatement("SELECT * FROM message WHERE posted_by = ?")) {

            statement.setInt(1, accountId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                            rs.getString("message_text"), rs.getLong("time_posted_epoch")));
                }
            }
        }
        return messages;
    }
}