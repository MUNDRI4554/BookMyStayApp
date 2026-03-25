import java.util.HashMap;
import java.util.Map;

abstract class Room {

    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }

    public void displayDetails() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: " + getPrice());
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 2000.0);
    }

    public void displayDetails() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: " + getPrice());
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 5000.0);
    }

    public void displayDetails() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: " + getPrice());
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}

class RoomSearchService {

    public void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {

        System.out.println("\nAvailable Rooms:\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 4.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================");

        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(rooms, inventory);
    }
}