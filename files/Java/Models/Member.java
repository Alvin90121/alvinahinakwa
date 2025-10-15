package models;

/**
 * This class represents a library member (a person who can borrow items).
 */
public class Member {
    private String name;
    private int membershipId;
    private String contactInfo;

    /**
     * Constructor to create a new member
     */
    public Member(String name, int membershipId, String contactInfo) {
        // Make sure to not create a member with empty information
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }
        if (membershipId <= 0) {
            throw new IllegalArgumentException("Membership ID must be positive");
        }
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact info cannot be empty");
        }

        this.name = name;
        this.membershipId = membershipId;
        this.contactInfo = contactInfo;
    }

    /**
     * Get the member's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the member's ID
     */
    public int getMembershipId() {
        return membershipId;
    }

    /**
     * Get the member's contact information
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Display information about this member
     */
    public void displayMemberInfo() {
        System.out.println("Member: " + name + " | ID: " + membershipId + " | Contact: " + contactInfo);
    }

    /**
     * This method is important for using Members in collections like HashMaps
     * Two members are the same if they have the same ID
     */
    @Override
    public boolean equals(Object obj) {
        // If this is exactly the same object, they're equal
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast obj to Member and compare IDs
        Member member = (Member) obj;
        return membershipId == member.membershipId;
    }

    /**
     * Also needed for collections - must match equals method
     */
    @Override
    public int hashCode() {
        return membershipId;
    }
}