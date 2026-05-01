import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class FlightBooking {

    private static final String URL = "jdbc:mysql://localhost:3306/airlinedb";
    private static final String USER = "root";
    private static final String PASSWORD = "dhruvil2207";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter flight ID: ");
            int flightId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter passenger name: ");
            String passengerName = scanner.nextLine();

            System.out.print("Enter seats requested: ");
            int seatsRequested = scanner.nextInt();

            bookFlight(flightId, passengerName, seatsRequested);
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void bookFlight(int flightId, String passengerName, int seatsRequested) {
        String selectSql = "SELECT available_seats, price_per_seat FROM flights WHERE flight_id = ?";
        String updateSql = "UPDATE flights SET available_seats = available_seats - ? WHERE flight_id = ?";
        String insertSql = "INSERT INTO bookings (passenger_name, flight_id, seats_booked, total_amount) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, flightId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (!resultSet.next()) {
                    connection.rollback();
                    System.out.println("Booking Failed: Invalid flight ID");
                    return;
                }

                int availableSeats = resultSet.getInt("available_seats");
                double pricePerSeat = resultSet.getDouble("price_per_seat");

                if (availableSeats < seatsRequested) {
                    connection.rollback();
                    System.out.println("Booking Failed: Not enough seats available");
                    return;
                }

                double totalAmount = seatsRequested * pricePerSeat;

                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                     PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

                    updateStatement.setInt(1, seatsRequested);
                    updateStatement.setInt(2, flightId);
                    updateStatement.executeUpdate();

                    insertStatement.setString(1, passengerName);
                    insertStatement.setInt(2, flightId);
                    insertStatement.setInt(3, seatsRequested);
                    insertStatement.setDouble(4, totalAmount);
                    insertStatement.executeUpdate();

                    connection.commit();
                    System.out.println("Booking Successful!");
                }
            } catch (Exception e) {
                connection.rollback();
                System.out.println("Booking Failed: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
