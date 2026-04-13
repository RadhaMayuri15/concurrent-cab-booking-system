import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CabBookingGUI extends Frame implements ActionListener {
    private TextField userIdField;
    private TextField startLocationField;
    private TextField endLocationField;
    private TextArea responseArea;
    private Canvas progressBar; // Progress bar for animation
    private List<UserBookingRequest> bookingRequests;
    private int progress = 0;

    public CabBookingGUI() {
        setTitle("Cab Booking System");
        setSize(600, 500);
        setLayout(new BorderLayout());
        bookingRequests = new ArrayList<>();

        // Header Panel
        Panel headerPanel = new Panel();
        headerPanel.setBackground(new Color(0x007BFF));
        Label headerLabel = new Label("Cab Booking System", Label.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        Panel formPanel = new Panel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(new Color(0xF5F5F5));

        formPanel.add(new Label("User ID:"));
        userIdField = new TextField();
        formPanel.add(userIdField);

        formPanel.add(new Label("Start Location:"));
        startLocationField = new TextField();
        formPanel.add(startLocationField);

        formPanel.add(new Label("End Location:"));
        endLocationField = new TextField();
        formPanel.add(endLocationField);

        Button addUserButton = new Button("Add User");
        addUserButton.setBackground(new Color(0x28A745));
        addUserButton.setForeground(Color.BLACK);
        addUserButton.addActionListener(e -> addUserBooking());
        formPanel.add(addUserButton);

        Button bookAllButton = new Button("Book All");
        bookAllButton.setBackground(new Color(0xFFC107));
        bookAllButton.setForeground(Color.BLACK);
        bookAllButton.addActionListener(this);
        formPanel.add(bookAllButton);

        add(formPanel, BorderLayout.CENTER);

        // Response and Progress Panel
        Panel responsePanel = new Panel(new BorderLayout());
        responseArea = new TextArea("", 8, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        responseArea.setEditable(false);
        responseArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        responsePanel.add(responseArea, BorderLayout.CENTER);

        progressBar = new Canvas() {
            @Override
            public void paint(Graphics g) {
                g.setColor(new Color(0x28A745));
                g.fillRect(0, 0, (int) (getWidth() * (progress / 100.0)), getHeight());
                g.setColor(Color.GRAY);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        progressBar.setSize(400, 20);
        responsePanel.add(progressBar, BorderLayout.SOUTH);

        add(responsePanel, BorderLayout.SOUTH);

        // Window Closing Behavior
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void addUserBooking() {
        String userId = userIdField.getText();
        String startLocation = startLocationField.getText();
        String endLocation = endLocationField.getText();

        if (userId.isEmpty() || startLocation.isEmpty() || endLocation.isEmpty()) {
            responseArea.setText("Please fill out all fields before adding a user.");
            return;
        }

        bookingRequests.add(new UserBookingRequest(Integer.parseInt(userId), startLocation, endLocation));
        responseArea.append("Added booking request for User ID: " + userId + "\n");

        userIdField.setText("");
        startLocationField.setText("");
        endLocationField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        responseArea.setText("Processing bookings...\n");
        progress = 0;

        // Use threads for each booking
        List<Thread> threads = new ArrayList<>();
        for (UserBookingRequest request : bookingRequests) {
            if (request.booked) {
                continue;
            }
            Thread bookingThread = new Thread(() -> {
                try {
                    CabDAO dao = new CabDAO();
                    boolean bookingSuccess = dao.bookCab(request.userId, request.startLocation, request.endLocation);
                    if (bookingSuccess) {
                        responseArea.append("Booking successful for User ID: " + request.userId + "\n");
                        request.booked = true;
                    } else {
                        responseArea.append("Booking failed for User ID: " + request.userId + "\n");
                    }
                } catch (Exception ex) {
                    responseArea.append("Error booking for User ID: " + request.userId + " - " + ex.getMessage() + "\n");
                }

                // Update progress bar
                synchronized (CabBookingGUI.this) {
                    progress += 100 / bookingRequests.size();
                    progressBar.repaint();
                }
            });
            threads.add(bookingThread);
            bookingThread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                responseArea.append("Error: " + ex.getMessage() + "\n");
            }
        }

        responseArea.append("All bookings processed.\n");
    }

    public static void main(String[] args) {
        CabBookingGUI bookingGUI = new CabBookingGUI();
        bookingGUI.setVisible(true);
    }

    static class UserBookingRequest {
        int userId;
        String startLocation;
        String endLocation;
        boolean booked;

        UserBookingRequest(int userId, String startLocation, String endLocation) {
            this.userId = userId;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.booked = false;
        }
    }
}



// queue and UI and 