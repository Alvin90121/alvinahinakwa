package models;

import java.util.Date;

/**
 * This class represents a book in our library.
 * It extends LibraryItem so it inherits all the common properties like
 * title, author, and publication date.
 */
public class Book extends LibraryItem {
    // Books have an ISBN (International Standard Book Number)
    private String ISBN;

    /**
     * Constructor for creating a new book
     */
    public Book(String title, String author, String ISBN, Date publicationDate, String category) {
        // Call the parent class constructor first to set up common properties
        super(title, author, publicationDate, category);

        // Check that ISBN isn't empty
        if (ISBN == null || ISBN.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }

        this.ISBN = ISBN;
    }

    /**
     * Get the ISBN of this book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * This overrides the abstract method from LibraryItem
     * It prints details specific to books
     */
    @Override
    public void displayDetails() {
        System.out.println("Book: " + title + " by " + author);
        System.out.println("  ISBN: " + ISBN);
        System.out.println("  Category: " + category);
        System.out.println("  Published: " + getPublicationDateFormatted());
        System.out.println("  Status: " + (isAvailable ? "Available" : "Checked Out"));
    }
}
