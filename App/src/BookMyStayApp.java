import java.io.*;
import java.util.*;

// Reservation class representing a confirmed booking
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println("Reservation ID: " + reservationId + ", Guest: " + guestName + ", Room: " + roomType);
    }
}

// Thread-safe inventory management class with Serializable support
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> availableRooms = new HashMap<>();

    public Inventory() {
        availableRooms.put("Single Room", 5);
        availableRooms.put("Double Room", 3);
        availableRooms.put("Suite Room", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        Integer count = availableRooms.get(roomType);
        if (count == null || count <= 0) return false;
        availableRooms.put(roomType, count - 1);
        return true;
    }

    public synchronized void releaseRoom(String roomType) {
        Integer count = availableRooms.get(roomType);
        if (count == null) return;
        availableRooms.put(roomType, count + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : availableRooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public synchronized Map<String, Integer> getAvailableRooms() {
        return new HashMap<>(availableRooms);
    }

    public synchronized void setAvailableRooms(Map<String, Integer> rooms) {
        availableRooms = new HashMap<>(rooms);
    }
}

// Persistence service handles save and load from file
class PersistenceService {
    private static final String FILE_NAME = "booking_system_state.dat";

    public static void saveState(List<Reservation> bookingHistory, Inventory inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bookingHistory);
            oos.writeObject(inventory);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Reservation> loadBookingHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<Reservation> bookingHistory = (List<Reservation>) ois.readObject();
            return bookingHistory;
        } catch (FileNotFoundException e) {
            System.out.println("No previous booking history found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading booking history: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static Inventory loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            // Skip booking history (first object)
            ois.readObject();
            Inventory inventory = (Inventory) ois.readObject();
            return inventory;
        } catch (FileNotFoundException e) {
            System.out.println("No previous inventory state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
        return new Inventory();
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 12.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================");

        // Load previous state or initialize new
        List<Reservation> bookingHistory = PersistenceService.loadBookingHistory();
        Inventory inventory = PersistenceService.loadInventory();

        System.out.println("\nLoaded Booking History:");
        if (bookingHistory.isEmpty()) {
            System.out.println("No prior bookings found.");
        } else {
            bookingHistory.forEach(Reservation::display);
        }

        inventory.displayInventory();

        // Simulate new booking requests
        System.out.println("\nProcessing new bookings...");

        Reservation newBooking1 = new Reservation("R101", "Alice", "Single Room");
        Reservation newBooking2 = new Reservation("R102", "Bob", "Suite Room");

        if (inventory.allocateRoom(newBooking1.getRoomType())) {
            bookingHistory.add(newBooking1);
            System.out.println("Booking confirmed for " + newBooking1.getGuestName());
        } else {
            System.out.println("Booking failed for " + newBooking1.getGuestName() + " - No availability.");
        }

        if (inventory.allocateRoom(newBooking2.getRoomType())) {
            bookingHistory.add(newBooking2);
            System.out.println("Booking confirmed for " + newBooking2.getGuestName());
        } else {
            System.out.println("Booking failed for " + newBooking2.getGuestName() + " - No availability.");
        }

        inventory.displayInventory();

        // Save state before shutdown
        PersistenceService.saveState(bookingHistory, inventory);

        System.out.println("\nSystem shutdown complete. State saved.");
    }
}