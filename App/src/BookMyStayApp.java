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

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean roomExists(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:\n");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

class BookingHistory {

    private Map<String, Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new LinkedHashMap<>();
    }

    public void addReservation(Reservation r) {
        confirmedBookings.put(r.getReservationId(), r);
    }

    public Reservation removeReservation(String reservationId) {
        return confirmedBookings.remove(reservationId);
    }

    public boolean exists(String reservationId) {
        return confirmedBookings.containsKey(reservationId);
    }

    public void displayHistory() {
        System.out.println("\nBooking History:");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No confirmed bookings.");
            return;
        }
        for (Reservation r : confirmedBookings.values()) {
            r.display();
        }
    }
}

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation failed: Reservation ID " + reservationId + " does not exist.");
            return;
        }

        Reservation r = history.removeReservation(reservationId);
        rollbackStack.push(r.getReservationId());
        inventory.increment(r.getRoomType());
        System.out.println("Cancellation successful for reservation:");
        r.display();
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Most recent cancellations on top):");
        for (String id : rollbackStack) {
            System.out.println(" - " + id);
        }
    }
}

public class BookMyStayApp{

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 10.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================\n");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, history);

        Reservation r1 = new Reservation("Alice", "Single Room", "R001");
        Reservation r2 = new Reservation("Bob", "Double Room", "R002");
        Reservation r3 = new Reservation("Charlie", "Suite Room", "R003");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        System.out.println("Before cancellation:");
        history.displayHistory();
        inventory.displayInventory();

        System.out.println("\nProcessing cancellations:\n");

        cancellationService.cancelReservation("R002"); // Valid
        cancellationService.cancelReservation("R004"); // Invalid
        cancellationService.cancelReservation("R003"); // Valid

        System.out.println("\nAfter cancellations:");
        history.displayHistory();
        inventory.displayInventory();

        cancellationService.displayRollbackStack();
    }
}