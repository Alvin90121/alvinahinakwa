package ui;

import java.util.Scanner;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import models.*;
import services.*;

/**
 * This class handles the user interface for our Library Management System.
 * It creates menus and processes user input to call the appropriate services.
 */
public class LibraryUI {

    private Scanner scanner = new Scanner(System.in);

    // These are the service objects that do the actual work
    private LibraryCatalogue catalogue = new LibraryCatalogue();
    private MemberService memberService = new MemberService();
    private BorrowingService borrowingService = new BorrowingService();

    // A date formatter to make entering dates easier
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Start the UI running - this is the main method that gets called from Main
     */
    public void start() {
        // Add some sample data to test the system
        initializeLibraryData();

        // This is the main program loop - it keeps running until the user chooses to exit
        while (true) {
            // Display the main menu options
            displayMainMenu();

            // Get the user's choice (a number from 1 to 7)
            int choice = getValidIntInput("Enter your choice: ", 1, 7);

            try {
                // Process the user's choice
                switch (choice) {
                    case 1: // Catalogue Management
                        catalogueManagementMenu();
                        break;
                    case 2: // Member Management
                        memberManagementMenu();
                        break;
                    case 3: // Borrow Item
                        borrowItemMenu();
                        break;
                    case 4: // Return Item
                        returnItemMenu();
                        break;
                    case 5: // View Borrowed Items
                        viewBorrowedItemsMenu();
                        break;
                    case 6: // Search
                        searchMenu();
                        break;
                    case 7: // Exit
                        System.out.println("Thank you for using the Library Management System. Goodbye!");
                        return; // Exit the program
                }
            } catch (Exception e) {
                // If something goes wrong, show the error and continue
                System.out.println("Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Display the main menu options
     */
    private void displayMainMenu() {
        System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Catalogue Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrow Item");
        System.out.println("4. Return Item");
        System.out.println("5. View Borrowed Items");
        System.out.println("6. Search");
        System.out.println("7. Exit");
        System.out.println("=====================================");
    }

    // ===== CATALOGUE MANAGEMENT =====

    /**
     * Display and process the catalogue management menu
     */
    private void catalogueManagementMenu() {
        // This is another menu loop - it keeps running until the user goes back
        while (true) {
            System.out.println("\n===== CATALOGUE MANAGEMENT =====");
            System.out.println("1. Add Book");
            System.out.println("2. Add Magazine");
            System.out.println("3. Display Catalogue");
            System.out.println("4. Delete Item");
            System.out.println("5. Back to Main Menu");
            System.out.println("=============================");

            int choice = getValidIntInput("Enter your choice: ", 1, 5);
            scanner.nextLine(); // Clear the input buffer

            // If they choose 5, go back to the main menu
            if (choice == 5) return;

            try {
                switch (choice) {
                    case 1: // Add Book
                        addBook();
                        break;
                    case 2: // Add Magazine
                        addMagazine();
                        break;
                    case 3: // Display Catalogue
                        catalogue.displayCatalogue();
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        break;
                    case 4: // Delete Item
                        deleteItem();
                        break;
                }
            } catch (Exception e) {
                // If something goes wrong, show the error and continue
                System.out.println("Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Get information from the user and add a new book to the catalogue
     */
    private void addBook() {
        System.out.println("\n----- ADD BOOK -----");

        // Get book information from the user
        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter author: ");
        String author = scanner.nextLine().trim();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        System.out.print("Enter publication date (MM/DD/YYYY): ");
        Date publicationDate = parseDate(scanner.nextLine().trim());

        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        // Create a new Book object with the information
        Book book = new Book(title, author, isbn, publicationDate, category);

        // Add the book to the catalogue
        catalogue.addItem(book);

        System.out.println("Book added successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Get information from the user and add a new magazine to the catalogue
     */
    private void addMagazine() {
        System.out.println("\n----- ADD MAGAZINE -----");

        // Get magazine information from the user
        System.out.print("Enter magazine title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine().trim();

        System.out.print("Enter issue number: ");
        int issueNumber = getValidIntInput("", 1, Integer.MAX_VALUE);
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter publication date (MM/DD/YYYY): ");
        Date publicationDate = parseDate(scanner.nextLine().trim());

        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        // Create a new Magazine object with the information
        Magazine magazine = new Magazine(title, publisher, issueNumber, publicationDate, category);

        // Add the magazine to the catalogue
        catalogue.addItem(magazine);

        System.out.println("Magazine added successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Delete an item from the catalogue
     */
    private void deleteItem() {
        System.out.println("\n----- DELETE ITEM -----");

        // Check if there are any items to delete
        if (catalogue.getItemCount() == 0) {
            System.out.println("The catalogue is empty. There are no items to delete.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Display the catalogue so the user can see what items are available
        catalogue.displayCatalogue();

        // Get the item number to delete
        System.out.print("Enter the item number to delete (or 0 to cancel): ");
        int itemIndex = getValidIntInput("", 0, catalogue.getItemCount());
        scanner.nextLine(); // Clear buffer

        // Check if the user wants to cancel
        if (itemIndex == 0) {
            System.out.println("Deletion canceled.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Convert from 1-based (display) to 0-based (internal)
        itemIndex--;

        // Get the item to delete
        LibraryItem item = catalogue.findItemByIndex(itemIndex);

        // Check if the item can be deleted (not checked out)
        if (!item.isAvailable()) {
            System.out.println("Cannot delete this item because it is currently checked out.");
            System.out.println("The item must be returned before it can be deleted.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Confirm deletion
        System.out.println("\nYou are about to delete the following item:");
        item.displayDetails();
        System.out.print("Are you sure you want to delete this item? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            // Delete the item
            catalogue.removeItem(item);
            System.out.println("Item deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // ===== MEMBER MANAGEMENT =====

    /**
     * Display and process the member management menu
     */
    private void memberManagementMenu() {
        while (true) {
            System.out.println("\n===== MEMBER MANAGEMENT =====");
            System.out.println("1. Register New Member");
            System.out.println("2. Register Staff Member");
            System.out.println("3. View All Members");
            System.out.println("4. Remove Member");
            System.out.println("5. Back to Main Menu");
            System.out.println("=============================");

            int choice = getValidIntInput("Enter your choice: ", 1, 5);
            scanner.nextLine();

            if (choice == 5) return;

            try {
                switch (choice) {
                    case 1: // Register Member
                        registerMember();
                        break;
                    case 2: // Register Staff
                        registerStaff();
                        break;
                    case 3: // View Members
                        memberService.listMembers();
                        System.out.println("Press Enter to continue...");
                        scanner.nextLine();
                        break;
                    case 4: // Remove Member
                        removeMember();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Get information from the user and register a new member
     */
    private void registerMember() {
        System.out.println("\n----- REGISTER MEMBER -----");

        // Get member information from the user
        System.out.print("Enter member name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter contact information (email/phone): ");
        String contactInfo = scanner.nextLine().trim();

        // Register the member using the member service
        memberService.registerMember(name, contactInfo);

        System.out.println("Member registered successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Get information from the user and register a new staff member
     */
    private void registerStaff() {
        System.out.println("\n----- REGISTER STAFF -----");

        // Get staff information from the user
        System.out.print("Enter staff name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter contact information (email/phone): ");
        String contactInfo = scanner.nextLine().trim();

        System.out.print("Enter staff role: ");
        String role = scanner.nextLine().trim();

        int membershipId = 1001 + memberService.getMemberCount();

        // Create a new staff member with the same ID
        Staff staff = new Staff(name, membershipId, contactInfo, role);

        // Register the staff using the member service
        memberService.registerMember(staff);

        System.out.println("Staff member registered successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Get a member ID from the user and remove that member
     */
    private void removeMember() {
        System.out.println("\n----- REMOVE MEMBER -----");

        // Get the member ID from the user
        System.out.print("Enter member ID: ");
        int memberId = getValidIntInput("", 1, Integer.MAX_VALUE);
        scanner.nextLine(); // Clear buffer

        // Remove the member using the member service
        memberService.removeMemberById(memberId);

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // ===== BORROWING AND RETURNING =====

    /**
     * Process the borrowing of an item
     */
    private void borrowItemMenu() {
        // Check if there are any items to borrow
        if (catalogue.getItemCount() == 0) {
            System.out.println("The catalogue is empty. Please add items first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Check if there are any members who can borrow
        if (memberService.getMemberCount() == 0) {
            System.out.println("No members registered. Please register members first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("\n----- BORROW ITEM -----");

        // First, select which member is borrowing
        Member member = selectMember();
        if (member == null) return;

        // Then, show the catalogue and let them select an item
        catalogue.displayCatalogue();
        System.out.print("Enter the item number to borrow: ");
        int itemIndex = getValidIntInput("", 1, catalogue.getItemCount()) - 1;
        scanner.nextLine(); // Clear buffer

        // Get the selected item
        LibraryItem item = catalogue.findItemByIndex(itemIndex);

        try {
            // Process the borrowing
            borrowingService.borrowItem(member, item);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Process the return of an item
     */
    private void returnItemMenu() {
        // Check if there are any members who might have borrowed something
        if (memberService.getMemberCount() == 0) {
            System.out.println("No members registered. Cannot process returns.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("\n----- RETURN ITEM -----");

        // First, select which member is returning an item
        Member member = selectMember();
        if (member == null) return;

        // Check if this member has borrowed any items
        if (borrowingService.getBorrowedItems(member).isEmpty()) {
            System.out.println("This member has no items to return.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Display all items borrowed by this member
        System.out.println("\nItems borrowed by " + member.getName() + ":");
        int index = 1;
        List<LibraryItem> borrowedItems = new ArrayList<>(borrowingService.getBorrowedItems(member).keySet());

        for (LibraryItem item : borrowedItems) {
            System.out.print(index++ + ". ");
            item.displayDetails();
        }

        // Let the user select which item to return
        System.out.print("Enter the number of the item to return: ");
        int itemIndex = getValidIntInput("", 1, borrowedItems.size()) - 1;
        scanner.nextLine(); // Clear buffer

        // Get the selected item
        LibraryItem itemToReturn = borrowedItems.get(itemIndex);

        try {
            // Process the return
            borrowingService.returnItem(member, itemToReturn);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * View all items borrowed by a member
     */
    private void viewBorrowedItemsMenu() {
        if (memberService.getMemberCount() == 0) {
            System.out.println("No members registered. Cannot view borrowed items.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("\n----- VIEW BORROWED ITEMS -----");

        // Select which member's items to view
        Member member = selectMember();
        if (member == null) return;

        // Display all borrowed items for this member
        borrowingService.displayBorrowedItems(member);

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // ===== SEARCH FUNCTIONALITY =====

    /**
     * Display and process the search menu
     */
    private void searchMenu() {
        while (true) {
            System.out.println("\n===== SEARCH MENU =====");
            System.out.println("1. Search Items by Title");
            System.out.println("2. Search Items by Category");
            System.out.println("3. Search Members by Name");
            System.out.println("4. Back to Main Menu");
            System.out.println("=======================");

            int choice = getValidIntInput("Enter your choice: ", 1, 4);
            scanner.nextLine();

            if (choice == 4) return;

            try {
                switch (choice) {
                    case 1: // Search by Title
                        searchItemsByTitle();
                        break;
                    case 2: // Search by Category
                        searchItemsByCategory();
                        break;
                    case 3: // Search Members
                        searchMembersByName();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Search for items by title
     */
    private void searchItemsByTitle() {
        System.out.println("\n----- SEARCH ITEMS BY TITLE -----");

        // Loop until a valid input is found or user chooses to go back
        while (true) {
            System.out.print("Enter title to search (or type 'back' to return): ");
            String title = scanner.nextLine().trim();

            // Check for exit command
            if (title.equalsIgnoreCase("back")) {
                return;
            }

            // Check for empty title
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Try again.");
                continue;
            }

            try {
                List<LibraryItem> results = catalogue.searchByTitle(title);

                if (results.isEmpty()) {
                    System.out.println("No items found with title containing: " + title);
                } else {
                    System.out.println("\nFound " + results.size() + " items matching '" + title + "':");
                    for (int i = 0; i < results.size(); i++) {
                        System.out.print((i + 1) + ". ");
                        results.get(i).displayDetails();
                    }
                }

                // Search is complete, exit the loop
                break;
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                break;
            }
        }

        // Wait for user to continue
        System.out.println("\nPress Enter to return to search menu...");
        scanner.nextLine();
    }

    /**
     * Search for items by category
     */
    private void searchItemsByCategory() {
        System.out.println("\n----- SEARCH ITEMS BY CATEGORY -----");

        // Loop until a valid input is found or user chooses to go back
        while (true) {
            System.out.print("Enter category to search (or type 'back' to return): ");
            String category = scanner.nextLine().trim();

            // Check for exit command
            if (category.equalsIgnoreCase("back")) {
                return;
            }

            // Check for empty category
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty. Try again.");
                continue;
            }

            try {
                List<LibraryItem> results = catalogue.searchByCategory(category);

                if (results.isEmpty()) {
                    System.out.println("No items found in category: " + category);
                } else {
                    System.out.println("\nFound " + results.size() + " items in category '" + category + "':");
                    for (int i = 0; i < results.size(); i++) {
                        System.out.print((i + 1) + ". ");
                        results.get(i).displayDetails();
                    }
                }

                // Search is complete, exit the loop
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                break;
            }
        }

        // Wait for user to continue
        System.out.println("\nPress Enter to return to search menu...");
        scanner.nextLine();
    }

    /**
     * Search for members by name
     */
    private void searchMembersByName() {
        System.out.println("\n----- SEARCH MEMBERS BY NAME -----");

        // Same as the title and category, loops until a valid input is found or user chooses to go back
        while (true) {
            System.out.print("Enter name (or type 'back' to return): ");
            String name = scanner.nextLine().trim();

            // Check for exit command
            if (name.equalsIgnoreCase("back")) {
                return;
            }

            // Check for empty name
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Try again.");
                continue;
            }

            try {
                List<Member> results = memberService.searchMembersByName(name);

                if (results.isEmpty()) {
                    System.out.println("No members found matching: " + name);
                } else {
                    System.out.println("\nFound " + results.size() + " members matching '" + name + "':");
                    for (Member member : results) {
                        member.displayMemberInfo();
                    }
                }

                // Search is complete, exit the loop
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                break;
            }
        }

        // Wait for user to continue
        System.out.println("\nPress Enter to return to search menu...");
        scanner.nextLine();
    }

    // ===== HELPER METHODS =====

    /**
     * Let the user select a member from the list
     */
    private Member selectMember() {
        // Display all members
        memberService.listMembers();

        // Check if we have any members
        if (memberService.getMemberCount() == 0) {
            System.out.println("No members registered.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return null;
        }

        // Get the member ID from the user
        System.out.print("Enter member ID: ");
        int memberId = getValidIntInput("", 1, Integer.MAX_VALUE);
        scanner.nextLine(); // Clear buffer

        // Find the member with this ID
        Member member = memberService.findMemberById(memberId);

        // Check if we found a member
        if (member == null) {
            System.out.println("Member with ID " + memberId + " not found.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return null;
        }

        return member;
    }

    /**
     * Parse a date string in MM/DD/YYYY format
     */
    private Date parseDate(String dateStr) {
        try {
            // Try to parse the date string
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            // If parsing fails, throw an exception with a helpful message
            throw new IllegalArgumentException("Invalid date format. Please use MM/DD/YYYY format.");
        }
    }

    /**
     * Get a valid integer input within a specified range
     */
    private int getValidIntInput(String prompt, int min, int max) {
        int input = 0;
        boolean validInput = false;

        // Keep trying until given a valid input
        while (!validInput) {
            System.out.print(prompt);
            try {
                // Try to read an integer
                input = scanner.nextInt();

                // Check if it's in the valid range
                if (input >= min && input <= max) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (Exception e) {
                // If reading fails (e.g., they entered text instead of a number)
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear the invalid input
            }
        }

        return input;
    }

    /**
     * Initialize some sample data for testing
     */
    private void initializeLibraryData() {
        try {
            // Add sample books
            catalogue.addItem(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565",
                    parseDate("04/10/1925"), "Fiction"));
            catalogue.addItem(new Book("To Kill a Mockingbird", "Harper Lee", "978-0061120084",
                    parseDate("07/11/1960"), "Fiction"));
            catalogue.addItem(new Book("1984", "George Orwell", "978-0451524935",
                    parseDate("06/08/1949"), "Science Fiction"));

            // Add sample magazines
            catalogue.addItem(new Magazine("National Geographic", "National Geographic Society", 256,
                    parseDate("03/15/2023"), "Science"));
            catalogue.addItem(new Magazine("Time", "Time USA, LLC", 42,
                    parseDate("02/28/2023"), "News"));

            // Add sample members
            memberService.registerMember("John Doe", "john.doe@email.com");
            memberService.registerMember("Jane Smith", "jane.smith@email.com");

            // Add sample staff
            Staff librarian = new Staff("Alice Johnson", 1003, "alice.j@library.org", "Librarian");
            memberService.registerMember(librarian);

            System.out.println("Sample data has been loaded successfully!");

        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }
}
