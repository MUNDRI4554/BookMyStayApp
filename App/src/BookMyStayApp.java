import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {

    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) throws InvalidBookingException {
        if (guestName == null || guestName.isEmpty())
            throw new InvalidBookingException("Guest name cannot be empty.");
        if (roomType == null || roomType.isEmpty())
            throw new InvalidBookingException("Room type cannot be empty.");
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

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType))
            throw new InvalidBookingException("Invalid room type: " + roomType);
    }

    public int getAvailability(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        return inventory.get(roomType);
    }

    public void decrementAvailability(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        int current = inventory.get(roomType);
        if (current <= 0)
            throw new InvalidBookingException("No availability left for room type: " + roomType);
        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:\n");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(Reservation reservation) {
        try {
            inventory.validateRoomType(reservation.getRoomType());
            if (inventory.getAvailability(reservation.getRoomType()) > 0) {
                inventory.decrementAvailability(reservation.getRoomType());
                System.out.println("Booking confirmed:");
                reservation.display();
            } else {
                System.out.println("Booking failed (no availability):");
                reservation.display();
            }
        } catch (InvalidBookingException e) {
            System.out.println("Booking error: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 9.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        try {
            Reservation r1 = new Reservation("Alice", "Single Room", "R001");
            Reservation r2 = new Reservation("Bob", "Double Room", "R002");
            Reservation r3 = new Reservation("Charlie", "Penthouse", "R003"); // Invalid room type
            Reservation r4 = new Reservation("", "Suite Room", "R004"); // Invalid guest name

            bookingService.bookRoom(r1);
            bookingService.bookRoom(r2);
            bookingService.bookRoom(r3);
            bookingService.bookRoom(r4);

        } catch (InvalidBookingException e) {
            System.out.println("Reservation creation error: " + e.getMessage());
        }

        inventory.displayInventory();
    }
}