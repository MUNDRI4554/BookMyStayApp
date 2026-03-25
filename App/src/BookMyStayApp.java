import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId + " | Guest: " + guestName + " | Room: " + roomType);
    }
}

class BookingHistory {

    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return confirmedBookings;
    }
}

class BookingReportService {

    public void generateReport(BookingHistory history) {
        System.out.println("\nBooking History Report:\n");
        if (history.getAllReservations().isEmpty()) {
            System.out.println("No confirmed bookings yet.\n");
            return;
        }
        for (Reservation r : history.getAllReservations()) {
            r.display();
        }
        System.out.println("\nTotal Confirmed Bookings: " + history.getAllReservations().size() + "\n");
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 8.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================\n");

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Alice", "Single Room", "R001");
        Reservation r2 = new Reservation("Bob", "Double Room", "R002");
        Reservation r3 = new Reservation("Charlie", "Suite Room", "R003");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}