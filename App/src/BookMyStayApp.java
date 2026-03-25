import java.util.*;
import java.util.concurrent.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getReservationId() { return reservationId; }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

class ThreadSafeRoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public ThreadSafeRoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        if (!inventory.containsKey(roomType)) return false;
        int available = inventory.get(roomType);
        if (available <= 0) return false;
        inventory.put(roomType, available - 1);
        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " -> Available: " + e.getValue());
        }
    }
}

class BookingProcessor implements Runnable {
    private final Queue<Reservation> bookingQueue;
    private final ThreadSafeRoomInventory inventory;

    public BookingProcessor(Queue<Reservation> bookingQueue, ThreadSafeRoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                r = bookingQueue.poll();
            }
            boolean success = inventory.allocateRoom(r.getRoomType());
            String status = success ? "CONFIRMED" : "FAILED (No Availability)";
            System.out.println("Thread " + Thread.currentThread().getName() +
                    " processed reservation " + r.getReservationId() + ": " + status);
        }
    }
}

public class BookMyStayApp
{

    public static void main(String[] args) {

        String appName = "Book My Stay - Hotel Booking System";
        String version = "Version 11.0";

        System.out.println("======================================");
        System.out.println(" " + appName);
        System.out.println(" " + version);
        System.out.println("======================================\n");

        ThreadSafeRoomInventory inventory = new ThreadSafeRoomInventory();

        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single Room", "R001"));
        bookingQueue.add(new Reservation("Bob", "Double Room", "R002"));
        bookingQueue.add(new Reservation("Charlie", "Single Room", "R003"));
        bookingQueue.add(new Reservation("David", "Suite Room", "R004"));
        bookingQueue.add(new Reservation("Eve", "Single Room", "R005"));

        // Create multiple threads to simulate concurrent booking
        int numberOfThreads = 3;
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new BookingProcessor(bookingQueue, inventory), "BookingThread-" + (i + 1));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        inventory.displayInventory();
        System.out.println("\nAll booking requests processed.");
    }
}