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

class Service {

    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

class AddOnServiceManager {

    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(Reservation reservation, Service service) {
        reservationServices.putIfAbsent(reservation.getReservationId(), new ArrayList<>());
        reservationServices.get(reservation.getReservationId()).add(service);
    }

    public void displayServices(Reservation reservation) {
        List<Service> services = reservationServices.get(reservation.getReservationId());
        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for reservation " + reservation.getReservationId() + "\n");
            return;
        }
        System.out.println("Add-on Services for reservation " + reservation.getReservationId() + ":");
        double totalCost = 0;
        for (Service s : services) {
            System.out.println(" - " + s.getServiceName() + " | Price: " + s.getPrice());
            totalCost += s.getPrice();
        }
        System.out.println("Total Add-on Cost: " + totalCost + "\n");
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 7.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================\n");

        Reservation r1 = new Reservation("Alice", "Single Room", "R001");
        Reservation r2 = new Reservation("Bob", "Double Room", "R002");

        Service breakfast = new Service("Breakfast", 200);
        Service spa = new Service("Spa Access", 500);
        Service airportPickup = new Service("Airport Pickup", 300);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        serviceManager.addService(r1, breakfast);
        serviceManager.addService(r1, spa);
        serviceManager.addService(r2, airportPickup);

        r1.display();
        serviceManager.displayServices(r1);

        r2.display();
        serviceManager.displayServices(r2);
    }
}