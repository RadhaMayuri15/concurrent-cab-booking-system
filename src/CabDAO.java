
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CabDAO {

    // Executor service for running background tasks
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public boolean bookCab(int userId, String startLocation, String endLocation) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Step 1: Find and book an available cab
            String checkAndBookQuery = "UPDATE cabs SET current_status = 'busy', current_location = ? "
                    + "WHERE current_status = 'available' AND current_location = ? LIMIT 1";

            String getBookedCabQuery = "SELECT licence_number FROM cabs WHERE current_status = 'busy' AND current_location = ? LIMIT 1";

            String insertBookingQuery = "INSERT INTO bookings (user_id, start_location, drop_location, licence_number) "
                    + "VALUES (?, ?, ?, ?)";

            // Update cab status to 'busy'
            try (PreparedStatement checkAndBookStmt = conn.prepareStatement(checkAndBookQuery)) {
                checkAndBookStmt.setString(1, endLocation);
                checkAndBookStmt.setString(2, startLocation);

                int rowsUpdated = checkAndBookStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    conn.rollback();
                    return false; // No available cab was found to book
                }
            }

            // Retrieve the booked cab's licence_number
            String licenceNumber;
            try (PreparedStatement getBookedCabStmt = conn.prepareStatement(getBookedCabQuery)) {
                getBookedCabStmt.setString(1, endLocation);

                try (ResultSet rs = getBookedCabStmt.executeQuery()) {
                    if (rs.next()) {
                        licenceNumber = rs.getString("licence_number");
                    } else {
                        conn.rollback();
                        return false; // No cab found after update (should not happen)
                    }
                }
            }

            // Insert booking entry into the bookings table
            try (PreparedStatement insertBookingStmt = conn.prepareStatement(insertBookingQuery)) {
                insertBookingStmt.setInt(1, userId);
                insertBookingStmt.setString(2, startLocation);
                insertBookingStmt.setString(3, endLocation);
                insertBookingStmt.setString(4, licenceNumber);

                insertBookingStmt.executeUpdate();
            }

            // Simulate the trip in a background thread
            int travelTime = calculateTravelTime(startLocation, endLocation);
            simulateTripInBackground(conn, licenceNumber, endLocation, travelTime);

            // Commit the transaction
            conn.commit();
            return true;

        } catch (Exception e) {
            System.err.println("Error during booking: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Run the trip simulation and release the cab after the trip is finished
    private void simulateTripInBackground(Connection conn, String licenceNumber, String endLocation, int travelTime) {
        // Submit the task to the executor service for running in the background
        executorService.submit(() -> {
            // Ensure that we get a new connection for the background thread
            try (Connection backgroundConn = DatabaseConnection.getConnection()) {
                // Simulate a trip by sleeping for the calculated travel time (in seconds)
                Thread.sleep(travelTime * 1000L);
    
                // After the trip, release the cab (mark it available again)
                releaseCabAfterTrip(backgroundConn, licenceNumber, endLocation);
    
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        });
    }
    

    // Function to release the cab after the trip
    private void releaseCabAfterTrip(Connection conn, String licenceNumber, String endLocation) throws SQLException {
        String releaseQuery = "UPDATE cabs SET current_status = 'available' WHERE licence_number = ?";
        try (PreparedStatement releaseStmt = conn.prepareStatement(releaseQuery)) {
            releaseStmt.setString(1, licenceNumber);
            releaseStmt.executeUpdate();
            System.out.println("Cab with licence number " + licenceNumber + " released and marked as available.");
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km
        return distance;
    }

    private int calculateTravelTime(String startLocation, String endLocation) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String locationQuery = "SELECT latitude, longitude from places where place_name= ?";
            double x1 = 0, y1 = 0;
            try (PreparedStatement releaseStmt = conn.prepareStatement(locationQuery)) {
                releaseStmt.setString(1, startLocation);
                try (ResultSet rs = releaseStmt.executeQuery()) {
                    if (rs.next()) {
                        x1 = rs.getDouble("latitude");
                        y1 = rs.getDouble("longitude");
                    } else {
                        System.err.println("Start location not found.");
                        return -1;
                    }
                }
            }
            double x2 = 0, y2 = 0;
            try (PreparedStatement releaseStmt = conn.prepareStatement(locationQuery)) {
                releaseStmt.setString(1, endLocation);
                try (ResultSet rs = releaseStmt.executeQuery()) {
                    if (rs.next()) {
                        x2 = rs.getDouble("latitude");
                        y2 = rs.getDouble("longitude");
                    } else {
                        System.err.println("End location not found.");
                        return -1;
                    }
                }
            }
            double distance = calculateDistance(x1, y1, x2, y2);
            double speed = 60;  // Assuming average speed of 60 km/h
            double travelTime = distance / speed; // Travel time in hours
            return (int) (travelTime * 60);  // Convert to minutes
        } catch (Exception e) {
            System.err.println("Error during booking: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
}
