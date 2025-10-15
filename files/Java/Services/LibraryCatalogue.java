package services;

import models.LibraryItem;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all the items in our library catalogue.
 * It keeps track of books, magazines, and any other LibraryItems.
 */
public class LibraryCatalogue {
    // ArrayList to store all our library items
    private ArrayList<LibraryItem> items;

    /**
     * Constructor to create a new empty catalogue
     */
    public LibraryCatalogue() {
        // Initialize an empty list
        items = new ArrayList<>();
    }

    /**
     * Add a new item to the catalogue
     */
    public void addItem(LibraryItem item) {
        // Make sure the item isn't null
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Add the item to our list
        items.add(item);
        System.out.println("Added: " + item.getTitle() + " to the catalogue.");
    }

    /**
     * Remove an item from the catalogue
     */
    public void removeItem(LibraryItem item) {
        // Make sure the item isn't null
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Try to remove the item and report the result
        if (items.remove(item)) {
            System.out.println("Removed: " + item.getTitle() + " from the catalogue.");
        } else {
            System.out.println("Item not found in catalogue.");
        }
    }

    /**
     * Display all items in the catalogue
     */
    public void displayCatalogue() {
        // Check if the catalogue is empty
        if (items.isEmpty()) {
            System.out.println("The catalogue is currently empty.");
            return;
        }

        // Print a header
        System.out.println("\n----- LIBRARY CATALOGUE -----");

        // Loop through all items and display each one
        for (int i = 0; i < items.size(); i++) {
            // Show item number (starting from 1, not 0)
            System.out.print((i + 1) + ". ");
            // Let each item display its own details
            items.get(i).displayDetails();
        }

        // Print a footer
        System.out.println("---------------------------\n");
    }

    /**
     * Get an item by its position in the catalogue
     */
    public LibraryItem findItemByIndex(int index) {
        // Check if the index is valid
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        // Return null if the index is invalid
        return null;
    }

    /**
     * Search for items by title (contains search)
     */
    public List<LibraryItem> searchByTitle(String title) {
        // Make sure the search term isn't empty
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Search title cannot be empty");
        }

        // Create a list to hold the results
        List<LibraryItem> results = new ArrayList<>();

        // Convert the search term to lowercase for case-insensitive search
        String searchTerm = title.toLowerCase().trim();

        // Check each item to see if its title contains the search term
        for (LibraryItem item : items) {
            if (item.getTitle().toLowerCase().contains(searchTerm)) {
                results.add(item);
            }
        }

        return results;
    }

    /**
     * Search for items by category
     */
    public List<LibraryItem> searchByCategory(String category) {
        // Make sure the category isn't empty
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Search category cannot be empty");
        }

        // Create a list to hold the results
        List<LibraryItem> results = new ArrayList<>();

        // Convert the category to lowercase for case-insensitive search
        String searchTerm = category.toLowerCase().trim();

        // Check each item to see if its category contains the search term
        for (LibraryItem item : items) {
            if (item.getCategory().toLowerCase().contains(searchTerm)) {
                results.add(item);
            }
        }

        return results;
    }

    /**
     * Get the number of items in the catalogue
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Get a copy of all items in the catalogue
     * We return a copy so the original list can't be modified
     */
    public ArrayList<LibraryItem> getAllItems() {
        return new ArrayList<>(items);
    }
}