//2022F-BSE0128 (SAMEED)

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Class to simulate a ticket booking system
class TicketBookingSystem {
    private int availableTickets = 10; // Initial number of tickets available
    private final Lock lock = new ReentrantLock(); // Lock to ensure thread safety

    // Method to book tickets
    public void bookTicket(int tickets) {
        lock.lock(); // Acquire the lock
        try {
            if (availableTickets >= tickets) {
                availableTickets -= tickets;
                System.out.println(Thread.currentThread().getName() + " booked " + tickets + " tickets. Tickets left: " + availableTickets);
            } else {
                System.out.println(Thread.currentThread().getName() + " failed to book " + tickets + " tickets. Only " + availableTickets + " tickets available.");
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Method to cancel tickets
    public void cancelTicket(int tickets) {
        lock.lock(); // Acquire the lock
        try {
            availableTickets += tickets;
            System.out.println(Thread.currentThread().getName() + " cancelled " + tickets + " tickets. Tickets left: " + availableTickets);
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}

// Class to simulate ticket booking/cancellation as a thread
class TicketTransactionThread extends Thread {
    private final TicketBookingSystem system;
    private final int tickets;
    private final boolean isBooking; // Flag to determine if it's a booking or cancellation

    // Constructor to initialize the transaction details
    public TicketTransactionThread(TicketBookingSystem system, int tickets, boolean isBooking) {
        this.system = system;
        this.tickets = tickets;
        this.isBooking = isBooking;
    }

    // Method executed by the thread to process the transaction
    @Override
    public void run() {
        if (isBooking) {
            system.bookTicket(tickets); // Perform ticket booking
        } else {
            system.cancelTicket(tickets); // Perform ticket cancellation
        }
    }
}

// Main class to simulate the ticket booking system
public class TicketSystemSimulation {
    public static void main(String[] args) {
        // Initialize the ticket booking system
        TicketBookingSystem system = new TicketBookingSystem();

        // Create and start multiple threads to simulate concurrent transactions
        TicketTransactionThread thread1 = new TicketTransactionThread(system, 3, true);  // Book 3 tickets
        TicketTransactionThread thread2 = new TicketTransactionThread(system, 5, true);  // Book 5 tickets
        TicketTransactionThread thread3 = new TicketTransactionThread(system, 2, false); // Cancel 2 tickets
        TicketTransactionThread thread4 = new TicketTransactionThread(system, 6, true);  // Book 6 tickets

        // Start the threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            // Wait for all threads to finish processing
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted.");
            e.printStackTrace();
        }

        System.out.println("All transactions processed.");
    }
}
