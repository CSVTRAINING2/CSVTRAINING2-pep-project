// Controller for handling API requests
import Controller.SocialMediaController;
// Data access object for accounts
import DAO.AccountDAO;
// Data access object for messages
import DAO.MessageDAO;
// Account service for business logic
import Service.AccountService;
// Service for Message business logic
import Service.MessageService;
// Javalin framework
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Initialize DAOs
        AccountDAO accountDAO = new AccountDAO(); 
        MessageDAO messageDAO = new MessageDAO();
        // Initialize Services with DAOs
        AccountService accountService = new AccountService(accountDAO); 
        MessageService messageService = new MessageService(messageDAO, accountDAO);
        // Initialize Controller with Services
        SocialMediaController controller = new SocialMediaController(accountService, messageService);
        // Start the API and run on port 8080
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}