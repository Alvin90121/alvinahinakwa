package models;

import java.util.Date;

/**
 * This class represents a magazine in our library.
 * Like Book, it extends LibraryItem to inherit common properties.
 */
public class Magazine extends LibraryItem {
    // Magazines have issue numbers to identify different editions
    private int issueNumber;

    /**
     * Constructor for creating a new magazine
     */
    public Magazine(String title, String author, int issueNumber, Date publicationDate, String category) {
        // Call parent constructor to handle common properties
        super(title, author, publicationDate, category);

        // Check that the issue number makes sense
        if (issueNumber <= 0) {
            throw new IllegalArgumentException("Issue number must be a positive number");
        }

        this.issueNumber = issueNumber;
    }

    /**
     * Get the issue number of this magazine
     */
    public int getIssueNumber() {
        return issueNumber;
    }

    /**
     * Display magazine-specific details
     * This overrides the abstract method from LibraryItem
     */
    @Override
    public void displayDetails() {
        System.out.println("Magazine: " + title + " | Issue: " + issueNumber);
        System.out.println("  Publisher: " + author);
        System.out.println("  Category: " + category);
        System.out.println("  Published: " + getPublicationDateFormatted());
        System.out.println("  Status: " + (isAvailable ? "Available" : "Checked Out"));
    }
}