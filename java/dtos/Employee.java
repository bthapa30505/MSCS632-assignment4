package dtos;

import java.util.*;

public class Employee {
    private String name;
    private Map<DayOfWeek, List<Shift>> preferences; // Stores preferred shifts for each day
    private Map<DayOfWeek, Shift> assignedShifts;    // Stores actual assigned shifts
    private int daysWorked;
    
    public Employee(String name) {
        this.name = name;
        this.preferences = new HashMap<>();
        this.assignedShifts = new HashMap<>();
        this.daysWorked = 0;
        
        // Initialize preferences for all days
        for (DayOfWeek day : DayOfWeek.values()) {
            preferences.put(day, new ArrayList<>());
        }
    }
    
    public String getName() {
        return name;
    }
    
    // Add a preferred shift for a specific day
    public void addPreference(DayOfWeek day, Shift shift) {
        preferences.get(day).add(shift);
    }
    
    // Get preferred shifts for a specific day
    public List<Shift> getPreferences(DayOfWeek day) {
        return new ArrayList<>(preferences.get(day));
    }
    
    // Check if employee has any preferences for a day
    public boolean hasPreferences(DayOfWeek day) {
        return !preferences.get(day).isEmpty();
    }
    
    // Assign a shift to the employee for a specific day
    public void assignShift(DayOfWeek day, Shift shift) {
        if (!isAssigned(day)) {
            assignedShifts.put(day, shift);
            daysWorked++;
        }
    }
    
    // Check if employee is already assigned on a specific day
    public boolean isAssigned(DayOfWeek day) {
        return assignedShifts.containsKey(day);
    }
    
    // Get assigned shift for a specific day
    public Shift getAssignedShift(DayOfWeek day) {
        return assignedShifts.get(day);
    }
    
    // Get number of days worked
    public int getDaysWorked() {
        return daysWorked;
    }
    
    // Check if employee can work more days (max 5 days per week)
    public boolean canWorkMoreDays() {
        return daysWorked < 5;
    }
    
    // Check if employee is available for a specific day
    public boolean isAvailable(DayOfWeek day) {
        return !isAssigned(day) && canWorkMoreDays();
    }
    
    // Clear all assignments (useful for regenerating schedule)
    public void clearAssignments() {
        assignedShifts.clear();
        daysWorked = 0;
    }
    
    // Get all assigned days
    public Set<DayOfWeek> getAssignedDays() {
        return new HashSet<>(assignedShifts.keySet());
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(name, employee.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
} 