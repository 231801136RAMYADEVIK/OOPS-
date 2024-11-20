import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class EventManager {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Replace with your DB URL
    private static final String USER = "system"; // Database username
    private static final String PASSWORD = "saranya155"; // Database password
    private static final Logger logger = Logger.getLogger(EventManager.class.getName());
    private Connection connection;

    // Constructor to establish DB connection
    public EventManager() {
        try {
            // Register JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            logger.info("Connected to the database!");
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Failed to connect to the database.", e);
        }
    }

    // Method to add a new event
    public boolean addEvent(int eventId, String eventName, String eventDate, String eventLocation) {
        String query = "INSERT INTO EVENTS (event_id, event_name, event_date, event_location) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            stmt.setString(2, eventName);
            stmt.setString(3, eventDate);
            stmt.setString(4, eventLocation);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding event to the database", e);
        }
        return false;
    }

    // Method to fetch all events
    public List<String> getAllEvents() {
        List<String> events = new ArrayList<>();
        String query = "SELECT * FROM EVENTS";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String eventInfo = "ID: " + rs.getInt("event_id") +
                        ", Name: " + rs.getString("event_name") +
                        ", Date: " + rs.getDate("event_date") +
                        ", Location: " + rs.getString("event_location");
                events.add(eventInfo);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching events from the database", e);
        }
        return events;
    }

    // Close the DB connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing the database connection.", e);
        }
    }
}
