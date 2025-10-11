package Main;

import ui.LibraryUI;

/**
 * This is the main class that starts our Library Management System.
 * It creates the user interface and starts it running.
 */
public class Main {
    public static void main(String[] args) {
        // Print a welcome message
        System.out.println("Starting Library Management System...");

        // Create the user interface
        LibraryUI libraryUI = new LibraryUI();

        // Start the interface running
        libraryUI.start();
    }
}
