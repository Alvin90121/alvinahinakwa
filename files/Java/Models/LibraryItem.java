package models;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * This is the parent class for all items in our library.
 */
public abstract class LibraryItem {
    // These are protected so child classes can access them directly
    protected String title;
    protected String author;
    protected Date publicationDate;
    protected String category;
    protected boolean isAvailable;

    // A shared formatter for dates so they look nice when displayed
    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Constructor to create a new library item
     */
    public LibraryItem(String title, String author, Date publicationDate, String category) {
        // Check for empty inputs - don't want to store empty values!
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (publicationDate == null) {
            throw new IllegalArgumentException("Publication date cannot be null");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        // Set all the values
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.category = category;
        this.isAvailable = true;  // New items are always available at first
    }

    /**
     * Mark this item as checked out
     */
    public void checkOut() {
        this.isAvailable = false;
    }

    /**
     * Mark this item as returned to the library
     */
    public void returnItem() {
        this.isAvailable = true;
    }

    /**
     * Check if this item is available for checkout
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    // Getter methods

    /**
     * Get the title of this item
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the author of this item
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get the publication date of this item
     */
    public Date getPublicationDate() {
        return publicationDate;
    }

    /**
     * Get the publication date as a formatted string
     */
    public String getPublicationDateFormatted() {
        return DATE_FORMAT.format(publicationDate);
    }

    /**
     * Get the category of this item
     */
    public String getCategory() {
        return category;
    }

    // Abstract method that child classes must implement
    /**
     * Display details of this item
     * Since different items have different details, each child class
     * will provide its own version of this method
     */
    public abstract void displayDetails();
}