package Controller;

// SQL exception handling
import java.sql.SQLException;
// List interface
import java.util.List;

// Account exception handling
import javax.security.auth.login.AccountException;

// Data access object for accounts
import DAO.AccountDAO;
// Data access object for messages
import DAO.MessageDAO;
// Account model
import Model.Account;
// Message model
import Model.Message;
// Account service for business logic
import Service.AccountService;
// Message service for business logic
import Service.MessageService;
// Javalin framework
import io.javalin.Javalin;
// Javalin handler for HTTP requests
import io.javalin.http.Handler;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    // Constructor with dependency injection
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Default constructor
    public SocialMediaController() {
        this.accountService = new AccountService(new AccountDAO());
        this.messageService = new MessageService(new MessageDAO(), new AccountDAO());
    }

    // Handler for user registration
    private final Handler registerHandler = ctx -> {
        // Implementation details
        try {
            Account newAccount = ctx.bodyAsClass(Account.class);
            Account createdAccount = accountService.registerAccount(newAccount);
            if (createdAccount != null) {
                ctx.status(200).json(createdAccount);
            } else {
                ctx.status(400).result("");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        } catch (AccountException e) {
            ctx.status(400).result("");
        }
    };

    // Handler for user login
    private final Handler loginHandler = ctx -> {
        // Implementation details
        try {
            Account loginAttempt = ctx.bodyAsClass(Account.class);
            Account authenticatedAccount = accountService.login(loginAttempt);

            if (authenticatedAccount != null) {
                ctx.json(authenticatedAccount).status(200);
            } else {
                ctx.status(401);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        }
    };

    // Handler for creating a new message
    private final Handler createMessageHandler = ctx -> {
        // Implementation details
        try {
            Message newMessage = ctx.bodyAsClass(Message.class);
            Message createdMessage = messageService.createMessage(newMessage);

            if (createdMessage != null) {
                ctx.json(createdMessage).status(200);
            } else {
                ctx.status(400).result("");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        }
    };

    // Handler for getting all messages
    private final Handler getAllMessagesHandler = ctx -> {
        // Implementation details
        try {
            List<Message> allMessages = messageService.getAllMessages();
            ctx.json(allMessages).status(200);
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        }
    };

    // Handler for getting a message by ID
    private final Handler getMessageByIdHandler = ctx -> {
        // Implementation details
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);

            if (message != null) {
                ctx.json(message).status(200);
            } else {
                ctx.status(200);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID.");
        }
    };

    // Handler for deleting a message by ID
    private final Handler deleteMessageByIdHandler = ctx -> {
        // Implementation details
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessageById(messageId);

            if (deletedMessage != null) {
                ctx.json(deletedMessage).status(200);
            } else {
                ctx.status(200);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID.");
        }
    };

    // Handler for updating a message text
    private final Handler updateMessageTextHandler = ctx -> {
        // Implementation details
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message messageUpdate = ctx.bodyAsClass(Message.class);
            String newMessageText = messageUpdate.getMessage_text();
            Message updatedMessage = messageService.updateMessageText(messageId, newMessageText);

            if (updatedMessage != null) {
                ctx.json(updatedMessage).status(200);
            } else {
                ctx.status(400).result("");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ctx.status(400).result("");
        }
    };

    // Handler for getting messages by account ID
    private final Handler getMessagesByAccountIdHandler = ctx -> {
        // Implementation details
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            ctx.json(messages).status(200);

        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid account ID.");
        }
    };

    // Method to start the API and define routes
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // Define routes and their corresponding handlers
        app.post("/register", registerHandler);
        app.post("/login", loginHandler);
        app.post("/messages", createMessageHandler);
        app.get("/messages", getAllMessagesHandler);
        app.get("/messages/{message_id}", getMessageByIdHandler);
        app.delete("/messages/{message_id}", deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", updateMessageTextHandler);
        app.get("/accounts/{account_id}/messages", getMessagesByAccountIdHandler);
        return app;
    }
}