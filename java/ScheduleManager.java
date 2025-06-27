import java.util.*;

public class ScheduleManager {
    private Schedule schedule;
    private Scanner scanner;
    
    public ScheduleManager() {
        this.schedule = new Schedule();
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("=== EMPLOYEE SCHEDULE MANAGER ===\n");
        
        // Add employees
        addEmployees();
        
        // Add preferences
        addPreferences();
        
        // Generate and display schedule
        schedule.generateSchedule();
        schedule.printSchedule();
        
        scanner.close();
    }
    
    private void addEmployees() {
        System.out.println("Enter employee information:");
        System.out.println("(Enter 'done' when finished adding employees)\n");
        
        while (true) {
            System.out.print("Enter employee name (or 'done'): ");
            String name = scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            
            if (!name.isEmpty()) {
                Employee employee = new Employee(name);
                schedule.addEmployee(employee);
                System.out.println("Added employee: " + name);
            }
        }
        
        System.out.println("\nTotal employees added: " + schedule.getEmployees().size());
    }
    
    private void addPreferences() {
        System.out.println("\nEnter shift preferences for each employee:");
        System.out.println("Available shifts: Morning, Afternoon, Evening");
        System.out.println("Available days: Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday");
        System.out.println("(Enter 'skip' to skip an employee, 'done' to finish)\n");
        
        for (Employee employee : schedule.getEmployees()) {
            System.out.println("\n--- " + employee.getName() + " ---");
            
            boolean usePriority = askForPrioritySystem();
            
            if (usePriority) {
                addPreferencesWithPriority(employee);
            } else {
                addSimplePreferences(employee);
            }
        }
    }
    
    private boolean askForPrioritySystem() {
        while (true) {
            System.out.print("Use priority-based preferences? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (response.equals("y") || response.equals("yes")) {
                return true;
            } else if (response.equals("n") || response.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'");
            }
        }
    }
    
    private void addSimplePreferences(Employee employee) {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.print(day.getDisplayName() + " - Enter preferred shift (or 'none'): ");
            String input = scanner.nextLine().trim();
            
            if (!input.equalsIgnoreCase("none") && !input.isEmpty()) {
                Shift shift = parseShift(input);
                if (shift != null) {
                    employee.addPreference(day, shift);
                    System.out.println("Added preference: " + day + " - " + shift);
                } else {
                    System.out.println("Invalid shift. Skipping this day.");
                }
            }
        }
    }
    
    private void addPreferencesWithPriority(Employee employee) {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println("\n" + day.getDisplayName() + ":");
            
            // Get first preference
            System.out.print("1st preference (or 'none'): ");
            String firstPref = scanner.nextLine().trim();
            if (!firstPref.equalsIgnoreCase("none") && !firstPref.isEmpty()) {
                Shift firstShift = parseShift(firstPref);
                if (firstShift != null) {
                    employee.addPreferenceWithPriority(day, firstShift, 0);
                }
            }
            
            // Get second preference
            System.out.print("2nd preference (or 'none'): ");
            String secondPref = scanner.nextLine().trim();
            if (!secondPref.equalsIgnoreCase("none") && !secondPref.isEmpty()) {
                Shift secondShift = parseShift(secondPref);
                if (secondShift != null) {
                    employee.addPreferenceWithPriority(day, secondShift, 1);
                }
            }
            
            // Get third preference
            System.out.print("3rd preference (or 'none'): ");
            String thirdPref = scanner.nextLine().trim();
            if (!thirdPref.equalsIgnoreCase("none") && !thirdPref.isEmpty()) {
                Shift thirdShift = parseShift(thirdPref);
                if (thirdShift != null) {
                    employee.addPreferenceWithPriority(day, thirdShift, 2);
                }
            }
        }
    }
    
    private Shift parseShift(String input) {
        String normalized = input.trim().toLowerCase();
        
        switch (normalized) {
            case "morning":
            case "m":
                return Shift.MORNING;
            case "afternoon":
            case "a":
                return Shift.AFTERNOON;
            case "evening":
            case "e":
                return Shift.EVENING;
            default:
                return null;
        }
    }
    
    public static void main(String[] args) {
        ScheduleManager manager = new ScheduleManager();
        manager.run();
    }
} 