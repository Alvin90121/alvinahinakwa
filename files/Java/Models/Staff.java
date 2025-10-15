package models;

/**
 * This class represents a staff member (like a librarian).
 * It extends Member because staff are also members of the library,
 * but with additional information (their role).
 */
public class Staff extends Member {
    // Staff have a role (like "Librarian" or "Assistant")
    private String staffRole;

    /**
     * Constructor to create a new staff member
     */
    public Staff(String name, int membershipId, String contactInfo, String staffRole) {
        // First set up the member information using the parent constructor
        super(name, membershipId, contactInfo);

        // Then add staff-specific information
        if (staffRole == null || staffRole.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff role cannot be empty");
        }
        this.staffRole = staffRole;
    }

    /**
     * Get the staff member's role
     */
    public String getStaffRole() {
        return staffRole;
    }

    /**
     * Display information about this staff member
     * This includes both member info and staff-specific info
     */
    public void displayStaffInfo() {
        // Call the parent method to show member information
        super.displayMemberInfo();
        // Then add staff-specific information
        System.out.println("Role: " + staffRole);
    }
}