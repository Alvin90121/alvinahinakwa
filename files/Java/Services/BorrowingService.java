package services;

import models.LibraryItem;
import models.Member;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * The BorrowingService manages all borrowing and returning activities in the library.
 * This class keeps track of:
 * - Which members have borrowed which items
 * - When items are due back
 * - Late fees for overdue items
 */
public class BorrowingService {
    // This stores all the borrowed items and their due dates
    // For each member, I have mapped the items they've borrowed and when they're due
    private HashMap<Member, Map<LibraryItem, Date>> borrowedItems;

    // These constants define our borrowing rules
    private static final int LOAN_PERIOD_DAYS = 14;     // Items are loaned for 14 days
    public static final double DAILY_OVERDUE_FEE = 0.50;  // 50 cents per day late fee

    /**
     * Constructor - creates a new empty BorrowingService
     */
    public BorrowingService() {
        // Initialize the data structure to track borrowed items
        borrowedItems = new HashMap<>();
    }

    /**
     * Let a member borrow a library item
     */
    public void borrowItem(Member member, LibraryItem item) {
        // First check that we have valid inputs
        if (member == null) {
            System.out.println("Error: Member cannot be null");
            return;
        }

        if (item == null) {
            System.out.println("Error: Item cannot be null");
            return;
        }

        // Check if the item is available to borrow
        if (item.isAvailable()) {
            // First time this member is borrowing something? Create their record
            if (!borrowedItems.containsKey(member)) {
                borrowedItems.put(member, new HashMap<>());
            }

            // Calculate the due date (current date + 14 days)
            Date currentDate = new Date();  // Today's date

            // Add 14 days to the current date to get the due date
            // 1000 (ms) * 60 (sec) * 60 (min) * 24 (hours) * LOAN_PERIOD_DAYS
            long dueTime = currentDate.getTime() + (1000 * 60 * 60 * 24 * LOAN_PERIOD_DAYS);
            Date dueDate = new Date(dueTime);

            // Add the item to the member's borrowed items with its due date
            borrowedItems.get(member).put(item, dueDate);

            // Mark the item as checked out in the catalogue
            item.checkOut();

            // Print confirmation message
            System.out.println(member.getName() + " has borrowed: " + item.getTitle());
            System.out.println("Due date: " + dueDate);
        } else {
            // The item is not available (already checked out)
            System.out.println("Sorry, '" + item.getTitle() + "' is not available for borrowing.");
        }

        // Late fee notification
        System.out.println("\n----- LATE FEE POLICY -----");
        System.out.println("Please return this item by the due date.");
        System.out.println("Late fee: £" + String.format("%.2f", DAILY_OVERDUE_FEE) + " per day");
        System.out.println("Example late fees:");
        System.out.println("- 1 day late: £" + String.format("%.2f", DAILY_OVERDUE_FEE * 1));
        System.out.println("- 1 week late: £" + String.format("%.2f", DAILY_OVERDUE_FEE * 7));
        System.out.println("- 2 weeks late: £" + String.format("%.2f", DAILY_OVERDUE_FEE * 14));
        System.out.println("--------------------------");
    }

    /**
     * Process a member returning a library item
     */
    public void returnItem(Member member, LibraryItem item) {
        // First check for valid inputs
        if (member == null) {
            System.out.println("Error: Member cannot be null");
            return;
        }

        if (item == null) {
            System.out.println("Error: Item cannot be null");
            return;
        }

        // Check if this member has borrowed this item
        if (hasBorrowedItem(member, item)) {
            // Get the due date for this item
            Date dueDate = borrowedItems.get(member).get(item);

            // Get today's date (the return date)
            Date returnDate = new Date();

            // Remove the item from the member's borrowed list
            borrowedItems.get(member).remove(item);

            // If the member has no more borrowed items, remove them from our tracking
            if (borrowedItems.get(member).isEmpty()) {
                borrowedItems.remove(member);
            }

            // Mark the item as returned (available again)
            item.returnItem();

            // Check if the item is returned late
            if (returnDate.after(dueDate)) {
                // Calculate days late
                long diffTime = returnDate.getTime() - dueDate.getTime();
                long diffDays = diffTime / (1000 * 60 * 60 * 24);  // Convert ms to days

                // Calculate the late fee
                double fee = diffDays * DAILY_OVERDUE_FEE;

                // Print late return message with fee
                System.out.println("Item returned late by " + diffDays + " days.");
                System.out.println("Late fee: $" + String.format("%.2f", fee));
            } else {
                // Item returned on time
                System.out.println("Item returned on time. Thank you!");
            }
        } else {
            System.out.println("This member has not borrowed this item or has already returned it.");
        }
    }

    /**
     * Display all items borrowed by a member
     */
    public void displayBorrowedItems(Member member) {
        // Check for valid input
        if (member == null) {
            System.out.println("Error: Member cannot be null");
            return;
        }

        // Check if this member has any borrowed items
        if (!borrowedItems.containsKey(member) || borrowedItems.get(member).isEmpty()) {
            System.out.println(member.getName() + " has no borrowed items.");
            return;
        }

        // Print a header for the display
        System.out.println("\n----- " + member.getName() + "'s Borrowed Items -----");

        // Get all the items and due dates for this member
        Map<LibraryItem, Date> items = borrowedItems.get(member);

        // Get today's date (to check if items are overdue)
        Date currentDate = new Date();

        // Loop through and display each borrowed item
        for (Map.Entry<LibraryItem, Date> entry : items.entrySet()) {
            LibraryItem item = entry.getKey();
            Date dueDate = entry.getValue();

            // Print basic item information
            System.out.println("- " + item.getTitle());
            System.out.println("  Due date: " + dueDate);

            // Check if the item is overdue
            if (currentDate.after(dueDate)) {
                // Calculate days late
                long diffTime = currentDate.getTime() - dueDate.getTime();
                long daysLate = diffTime / (1000 * 60 * 60 * 24);  // Convert ms to days

                // Calculate current fee
                double fee = daysLate * DAILY_OVERDUE_FEE;

                // Print overdue status and fee
                System.out.println("  STATUS: OVERDUE by " + daysLate + " days");
                System.out.println("  Current fee: $" + String.format("%.2f", fee));
            } else {
                // Calculate days left until due
                long diffTime = dueDate.getTime() - currentDate.getTime();
                long daysLeft = diffTime / (1000 * 60 * 60 * 24);  // Convert ms to days

                // Prints status
                System.out.println("  STATUS: On time (" + daysLeft + " days remaining)");
            }

            System.out.println();
        }
    }

    /**
     * Get a list of all items borrowed by a member
     */
    public Map<LibraryItem, Date> getBorrowedItems(Member member) {
        // If the member has borrowed items, return them
        if (borrowedItems.containsKey(member)) {
            return borrowedItems.get(member);
        }

        // Return an empty map if they haven't borrowed anything
        return new HashMap<>();
    }

    /**
     * Check if a member has borrowed a specific item
     */
    public boolean hasBorrowedItem(Member member, LibraryItem item) {
        // I need to check two things:
        // 1. Does the member exist in our borrowedItems map?
        // 2. Has the member borrowed this specific item?
        return borrowedItems.containsKey(member) &&
                borrowedItems.get(member).containsKey(item);
    }
}
