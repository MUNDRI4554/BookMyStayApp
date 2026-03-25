import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.add(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:\n");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

class BookingService {

    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    public void processBookings(BookingRequestQueue queue) {

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();
            String type = request.getRoomType();

            if (inventory.getAvailability(type) > 0) {

                String roomId = type.substring(0, 2).toUpperCase() + roomCounter++;

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                Set<String> assigned = allocatedRooms.get(type);

                if (!assigned.contains(roomId)) {
                    assigned.add(roomId);
                    inventory.decrementAvailability(type);

                    System.out.println("Booking Confirmed:");
                    System.out.println("Guest: " + request.getGuestName());
                    System.out.println("Room Type: " + type);
                    System.out.println("Room ID: " + roomId + "\n");
                }

            } else {
                System.out.println("Booking Failed (No Availability):");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + type + "\n");
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 6.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================\n");

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Double Room"));
        queue.addRequest(new Reservation("Eve", "Suite Room"));

        RoomInventory inventory = new RoomInventory();

        BookingService service = new BookingService(inventory);

        service.processBookings(queue);

        inventory.displayInventory();
    }
}