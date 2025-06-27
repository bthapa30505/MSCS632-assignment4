import java.util.*;

public class Employee {
    private String name;
    private Map<DayOfWeek, List<Shift>> preferences; // Priority order: first element is highest priority
    private Map<DayOfWeek, Shift> assignedShifts;
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
    
    public void addPreference(DayOfWeek day, Shift shift) {
        preferences.get(day).add(shift);
    }
    
    public void addPreferenceWithPriority(DayOfWeek day, Shift shift, int priority) {
        List<Shift> dayPreferences = preferences.get(day);
        while (dayPreferences.size() <= priority) {
            dayPreferences.add(null);
        }
        dayPreferences.set(priority, shift);
    }
    
    public List<Shift> getPreferences(DayOfWeek day) {
        return new ArrayList<>(preferences.get(day));
    }
    
    public void assignShift(DayOfWeek day, Shift shift) {
        if (!isAssigned(day)) {
            assignedShifts.put(day, shift);
            daysWorked++;
        }
    }
    
    public boolean isAssigned(DayOfWeek day) {
        return assignedShifts.containsKey(day);
    }
    
    public Shift getAssignedShift(DayOfWeek day) {
        return assignedShifts.get(day);
    }
    
    public int getDaysWorked() {
        return daysWorked;
    }
    
    public boolean canWorkMoreDays() {
        return daysWorked < 5;
    }
    
    public boolean isAvailable(DayOfWeek day) {
        return !isAssigned(day) && canWorkMoreDays();
    }
    
    public void clearAssignments() {
        assignedShifts.clear();
        daysWorked = 0;
    }
    
    @Override
    public String toString() {
        return name;
    }
} 