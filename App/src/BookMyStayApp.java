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

public class BookMyStayApp
{

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 2.1";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailability = 10;
        int doubleAvailability = 5;
        int suiteAvailability = 2;

        System.out.println("\nRoom Details & Availability:\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailability + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability + "\n");

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability + "\n");
    }
}