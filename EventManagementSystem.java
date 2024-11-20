import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.*;

public class EventManagementSystem extends JFrame {

    private EventManager eventManager;
    private JTextField txtEventId, txtEventName, txtEventDate, txtEventLocation;
    private JTextArea txtAreaEvents;
    private static final Logger logger = Logger.getLogger(EventManagementSystem.class.getName());

    public EventManagementSystem() {
        eventManager = new EventManager();
        createUI();
    }

    private void createUI() {
        setTitle("Event Management System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel lblEventId = new JLabel("Event ID:");
        JLabel lblEventName = new JLabel("Event Name:");
        JLabel lblEventDate = new JLabel("Event Date (YYYY-MM-DD):");
        JLabel lblEventLocation = new JLabel("Event Location:");

        txtEventId = new JTextField();
        txtEventName = new JTextField();
        txtEventDate = new JTextField();
        txtEventLocation = new JTextField();

        JButton btnAddEvent = new JButton("Add Event");
        JButton btnShowEvents = new JButton("Show Events");

        txtAreaEvents = new JTextArea();
        txtAreaEvents.setEditable(false);

        panel.add(lblEventId);
        panel.add(txtEventId);
        panel.add(lblEventName);
        panel.add(txtEventName);
        panel.add(lblEventDate);
        panel.add(txtEventDate);
        panel.add(lblEventLocation);
        panel.add(txtEventLocation);
        panel.add(btnAddEvent);
        panel.add(btnShowEvents);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(txtAreaEvents), BorderLayout.CENTER);

        btnAddEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String eventId = txtEventId.getText();
                    String eventName = txtEventName.getText();
                    String eventDate = txtEventDate.getText();
                    String eventLocation = txtEventLocation.getText();

                    if (eventId.isEmpty() || eventName.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty()) {
                        JOptionPane.showMessageDialog(EventManagementSystem.this, "Please fill all fields!");
                    } else {
                        boolean success = eventManager.addEvent(Integer.parseInt(eventId), eventName, eventDate, eventLocation);
                        if (success) {
                            JOptionPane.showMessageDialog(EventManagementSystem.this, "Event added successfully!");
                            txtEventId.setText("");
                            txtEventName.setText("");
                            txtEventDate.setText("");
                            txtEventLocation.setText("");
                        } else {
                            JOptionPane.showMessageDialog(EventManagementSystem.this, "Error adding event!");
                        }
                    }
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Error while adding event", ex);
                    JOptionPane.showMessageDialog(EventManagementSystem.this, "An error occurred: " + ex.getMessage());
                }
            }
        });

        btnShowEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        List<String> events = eventManager.getAllEvents();
                        for (String event : events) {
                            publish(event);
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<String> chunks) {
                        for (String event : chunks) {
                            txtAreaEvents.append(event + "\n");
                        }
                    }

                    @Override
                    protected void done() {
                        // Any post-processing after background task is completed
                    }
                };
                worker.execute();
            }
        });
    }

    public static void main(String[] args) {
        EventManagementSystem window = new EventManagementSystem();
        window.setVisible(true);
    }
}
