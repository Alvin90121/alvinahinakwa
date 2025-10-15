package services;

import models.Member;
import models.Staff;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all library members.
 * It handles registering, removing, and searching for members.
 */
public class MemberService {
    // ArrayList to store all our members
    private ArrayList<Member> members = new ArrayList<>();

    // I will start member IDs at 1001 and count up
    private int nextMembershipId = 1001;

    /**
     * Register a new member with automatically generated ID
     */
    public Member registerMember(String name, String contactInfo) {
        // Make sure name and contact info aren't empty
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact info cannot be empty");
        }

        // Create a new member with the next available ID
        Member newMember = new Member(name, nextMembershipId++, contactInfo);

        // Add the member to our list
        members.add(newMember);

        System.out.println("Member registered successfully: " + name + " (ID: " + newMember.getMembershipId() + ")");

        return newMember;
    }

    public void registerMember(Member member) {
        // Make sure the member isn't null
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        // If the member has the default ID (0), assign a new one
        if (member.getMembershipId() == 0) {
            throw new IllegalArgumentException("Member must have a valid ID");
        }

        // Check if a member with this ID already exists
        for (Member existingMember : members) {
            if (existingMember.getMembershipId() == member.getMembershipId()) {
                throw new IllegalArgumentException("Member with ID " + member.getMembershipId() + " already exists");
            }
        }

        // Make sure nextMembershipId stays ahead of any manually assigned IDs
        if (member.getMembershipId() >= nextMembershipId) {
            nextMembershipId = member.getMembershipId() + 1;
        }

        // Add the member to our list
        members.add(member);
        System.out.println("Member registered successfully: " + member.getName());
    }


    /**
     * Remove a member by their ID
     */
    public void removeMemberById(int membershipId) {
        // Find the member with this ID
        Member memberToRemove = null;

        for (Member member : members) {
            if (member.getMembershipId() == membershipId) {
                memberToRemove = member;
                break;
            }
        }

        // Remove the member if found
        if (memberToRemove != null) {
            members.remove(memberToRemove);
            System.out.println("Member removed: " + memberToRemove.getName());
        } else {
            System.out.println("Member with ID " + membershipId + " not found.");
        }
    }


    /**
     * Display all members, distinguishing between regular members and staff
     */
    public void listMembers() {
        // Check if there are any members
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }

        // Print a header
        System.out.println("\n----- LIBRARY MEMBERS -----");

        // Display each member
        for (Member member : members) {
            // Check if this member is a Staff member
            if (member instanceof Staff) {
                // Cast to Staff to access staff-specific methods
                Staff staffMember = (Staff) member;
                System.out.println("STAFF: " + member.getName() + " | ID: " + member.getMembershipId() +
                        " | Contact: " + member.getContactInfo() + " | Role: " + staffMember.getStaffRole());
            } else {
                // Regular member
                member.displayMemberInfo();
            }
        }

        // Print a footer
        System.out.println("---------------------------\n");
    }

    /**
     * Find a member by their ID
     */
    public Member findMemberById(int membershipId) {
        // Check each member for a matching ID
        for (Member member : members) {
            if (member.getMembershipId() == membershipId) {
                return member;
            }
        }

        // Return null if no matching member is found
        return null;
    }

    /**
     * Search for members by name
     */
    public List<Member> searchMembersByName(String name) {
        // Make sure the search term isn't empty
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be empty");
        }

        // Create a list to hold the results
        List<Member> results = new ArrayList<>();

        // Convert the search term to lowercase for case-insensitive search
        String searchTerm = name.toLowerCase().trim();

        // Check each member to see if their name contains the search term
        for (Member member : members) {
            if (member.getName().toLowerCase().contains(searchTerm)) {
                results.add(member);
            }
        }

        return results;
    }

    /**
     * Get the number of members
     */
    public int getMemberCount() {
        return members.size();
    }

    /**
     * Get a copy of all members
     * We return a copy so the original list can't be modified
     */
    public ArrayList<Member> getAllMembers() {
        return new ArrayList<>(members);
    }
}