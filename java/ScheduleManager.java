import dtos.Employee;
import dtos.Shift;
import dtos.DayOfWeek;

import java.util.*;

public class ScheduleManager {
    private Schedule schedule;
    private Scanner scanner;
    
    public ScheduleManager() {
        this.schedule = new Schedule();
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("=== EMPLOYEE SCHEDULE MANAGEMENT SYSTEM ===\n");
        System.out.println("Welcome to the Employee Scheduling Application!");
        System.out.println("This system helps manage employee shifts across a 7-day work week.");
        System.out.println("Features:");
        System.out.println("- Employees can work a maximum of 5 days per week");
        System.out.println("- No employee works more than one shift per day");
        System.out.println("- Minimum 2 employees required per shift");
        System.out.println("- Automatic conflict resolution and staffing optimization\n");
        
        try {
            // Step 1: Add employees
            addEmployees();
            
            // Step 2: Collect employee preferences
            collectPreferences();
            
            // Step 3: Generate and display schedule
            generateSchedule();
            
            // Step 4: Offer options for schedule management
            manageSchedule();
            
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    private void addEmployees() {
        System.out.println("=== STEP 1: ADD EMPLOYEES ===");
        System.out.println("Enter employee names (type 'done' when finished):\n");
        
        while (true) {
            System.out.print("Enter employee name (or 'done'): ");
            String name = scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            
            if (name.isEmpty()) {
                System.out.println("Please enter a valid name.");
                continue;
            }
            
            // Check for duplicate names
            boolean exists = schedule.getEmployees().stream()
                .anyMatch(emp -> emp.getName().equalsIgnoreCase(name));
            
            if (exists) {
                System.out.println("Employee with that name already exists. Please enter a different name.");
                continue;
            }
            
            Employee employee = new Employee(name);
            schedule.addEmployee(employee);
            System.out.println("✓ Added employee: " + name);
        }
        
        if (schedule.getEmployees().isEmpty()) {
            System.out.println("No employees added. Adding sample employees for demonstration...");
            addSampleEmployees();
        }
        
        System.out.println("\nTotal employees: " + schedule.getEmployees().size());
        System.out.println("Employees: " + schedule.getEmployees().stream()
            .map(Employee::getName)
            .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b));
    }
    
    private void addSampleEmployees() {
        String[] sampleNames = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"};
        for (String name : sampleNames) {
            schedule.addEmployee(new Employee(name));
        }
    }
    
    private void collectPreferences() {
        System.out.println("\n=== STEP 2: COLLECT SHIFT PREFERENCES ===");
        System.out.println("For each employee, specify their preferred shifts for each day.");
        System.out.println("Available shifts: Morning, Afternoon, Evening");
        System.out.println("You can enter: 'morning', 'afternoon', 'evening', 'm', 'a', 'e', '1', '2', '3', or 'skip'\n");
        
        for (Employee employee : schedule.getEmployees()) {
            System.out.println("--- Setting preferences for " + employee.getName() + " ---");
            
            // Ask if user wants to set preferences manually or use quick setup
            System.out.print("Use quick setup for " + employee.getName() + "? (y/n): ");
            String quickSetup = scanner.nextLine().trim().toLowerCase();
            
            if (quickSetup.equals("y") || quickSetup.equals("yes")) {
                useQuickSetup(employee);
            } else {
                collectManualPreferences(employee);
            }
            
            System.out.println();
        }
    }
    
    private void useQuickSetup(Employee employee) {
        System.out.println("Quick Setup Options:");
        System.out.println("1. Morning shifts (all 7 days)");
        System.out.println("2. Afternoon shifts (all 7 days)");
        System.out.println("3. Evening shifts (all 7 days)");
        System.out.println("4. Weekdays only (Monday-Friday, mixed shifts)");
        System.out.println("5. Weekend focus (weekends + some weekdays)");
        System.out.println("6. Custom preferences");
        
        System.out.print("Choose option (1-6): ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                for (DayOfWeek day : DayOfWeek.values()) {
                    employee.addPreference(day, Shift.MORNING);
                }
                System.out.println("✓ Set morning shifts for all days");
                break;
            case "2":
                for (DayOfWeek day : DayOfWeek.values()) {
                    employee.addPreference(day, Shift.AFTERNOON);
                }
                System.out.println("✓ Set afternoon shifts for all days");
                break;
            case "3":
                for (DayOfWeek day : DayOfWeek.values()) {
                    employee.addPreference(day, Shift.EVENING);
                }
                System.out.println("✓ Set evening shifts for all days");
                break;
            case "4":
                DayOfWeek[] weekdays = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
                                       DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
                Shift[] shifts = {Shift.MORNING, Shift.AFTERNOON, Shift.EVENING};
                for (int i = 0; i < weekdays.length; i++) {
                    employee.addPreference(weekdays[i], shifts[i % 3]);
                }
                System.out.println("✓ Set weekday preferences with mixed shifts");
                break;
            case "5":
                employee.addPreference(DayOfWeek.SATURDAY, Shift.MORNING);
                employee.addPreference(DayOfWeek.SATURDAY, Shift.AFTERNOON);
                employee.addPreference(DayOfWeek.SUNDAY, Shift.MORNING);
                employee.addPreference(DayOfWeek.SUNDAY, Shift.AFTERNOON);
                employee.addPreference(DayOfWeek.FRIDAY, Shift.EVENING);
                employee.addPreference(DayOfWeek.MONDAY, Shift.MORNING);
                System.out.println("✓ Set weekend-focused preferences");
                break;
            case "6":
                collectManualPreferences(employee);
                break;
            default:
                System.out.println("Invalid choice. Using manual setup...");
                collectManualPreferences(employee);
                break;
        }
    }
    
    private void collectManualPreferences(Employee employee) {
        System.out.println("Enter preferred shifts for each day (or 'skip' to skip a day):");
        
        for (DayOfWeek day : DayOfWeek.values()) {
            while (true) {
                System.out.printf("%s - preferred shift (morning/afternoon/evening/skip): ", 
                    day.getDisplayName());
                String input = scanner.nextLine().trim().toLowerCase();
                
                if (input.equals("skip") || input.isEmpty()) {
                    break;
                }
                
                Shift shift = parseShift(input);
                if (shift != null) {
                    employee.addPreference(day, shift);
                    System.out.println("  ✓ Added " + shift.getDisplayName() + " shift for " + day.getDisplayName());
                    break;
                } else {
                    System.out.println("  Invalid input. Please enter: morning, afternoon, evening, m, a, e, 1, 2, 3, or skip");
                }
            }
        }
    }
    
    private Shift parseShift(String input) {
        switch (input.toLowerCase()) {
            case "morning": case "m": case "1":
                return Shift.MORNING;
            case "afternoon": case "a": case "2":
                return Shift.AFTERNOON;
            case "evening": case "e": case "3":
                return Shift.EVENING;
            default:
                return null;
        }
    }
    
    private void generateSchedule() {
        System.out.println("\n=== STEP 3: GENERATING SCHEDULE ===");
        System.out.println("Processing employee preferences and generating optimal schedule...");
        
        schedule.generateSchedule();
        
        System.out.println("✓ Schedule generated successfully!\n");
        schedule.printSchedule();
    }
    
    private void manageSchedule() {
        System.out.println("\n=== SCHEDULE MANAGEMENT OPTIONS ===");
        
        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. View current schedule");
            System.out.println("2. Regenerate schedule");
            System.out.println("3. Add new employee");
            System.out.println("4. View employee details");
            System.out.println("5. Export schedule summary");
            System.out.println("6. Exit");
            
            System.out.print("Choose option (1-6): ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    schedule.printSchedule();
                    break;
                case "2":
                    regenerateSchedule();
                    break;
                case "3":
                    addNewEmployee();
                    break;
                case "4":
                    viewEmployeeDetails();
                    break;
                case "5":
                    exportScheduleSummary();
                    break;
                case "6":
                    System.out.println("Thank you for using the Employee Schedule Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
                    break;
            }
        }
    }
    
    private void regenerateSchedule() {
        System.out.println("\nRegenerating schedule...");
        schedule.generateSchedule();
        System.out.println("✓ Schedule regenerated!");
        schedule.printSchedule();
    }
    
    private void addNewEmployee() {
        System.out.print("\nEnter new employee name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }
        
        // Check for duplicates
        boolean exists = schedule.getEmployees().stream()
            .anyMatch(emp -> emp.getName().equalsIgnoreCase(name));
        
        if (exists) {
            System.out.println("Employee with that name already exists.");
            return;
        }
        
        Employee newEmployee = new Employee(name);
        schedule.addEmployee(newEmployee);
        
        System.out.println("✓ Added employee: " + name);
        System.out.println("Setting preferences for " + name + "...");
        
        collectManualPreferences(newEmployee);
        
        System.out.println("Employee added. Regenerating schedule...");
        schedule.generateSchedule();
        schedule.printSchedule();
    }
    
    private void viewEmployeeDetails() {
        System.out.println("\n=== EMPLOYEE DETAILS ===");
        
        for (Employee emp : schedule.getEmployees()) {
            System.out.println("\n" + emp.getName() + ":");
            System.out.println("  Days worked: " + emp.getDaysWorked() + "/5");
            
            if (emp.getDaysWorked() > 0) {
                System.out.println("  Assigned shifts:");
                for (DayOfWeek day : DayOfWeek.values()) {
                    if (emp.isAssigned(day)) {
                        System.out.println("    " + day.getDisplayName() + ": " + 
                            emp.getAssignedShift(day).getDisplayName());
                    }
                }
            }
            
            System.out.println("  Preferences:");
            boolean hasPreferences = false;
            for (DayOfWeek day : DayOfWeek.values()) {
                List<Shift> prefs = emp.getPreferences(day);
                if (!prefs.isEmpty()) {
                    hasPreferences = true;
                    System.out.print("    " + day.getDisplayName() + ": ");
                    for (int i = 0; i < prefs.size(); i++) {
                        if (i > 0) System.out.print(", ");
                        System.out.print(prefs.get(i).getDisplayName());
                    }
                    System.out.println();
                }
            }
            if (!hasPreferences) {
                System.out.println("    No preferences set");
            }
        }
    }
    
    private void exportScheduleSummary() {
        System.out.println("\n=== SCHEDULE EXPORT SUMMARY ===");
        System.out.println("Company: [Your Company Name]");
        System.out.println("Week of: [Current Week]");
        System.out.println("Generated on: " + new Date());
        System.out.println();
        
        // Brief summary
        int totalEmployees = schedule.getEmployees().size();
        int totalAssignedDays = schedule.getEmployees().stream()
            .mapToInt(Employee::getDaysWorked)
            .sum();
        
        System.out.println("Summary Statistics:");
        System.out.println("- Total employees: " + totalEmployees);
        System.out.println("- Total assigned days: " + totalAssignedDays);
        System.out.println("- Average days per employee: " + 
            (totalEmployees > 0 ? String.format("%.1f", (double)totalAssignedDays / totalEmployees) : "0"));
        
        // Quick schedule overview
        System.out.println("\nSchedule Overview:");
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.printf("%-10s: ", day.getDisplayName());
            for (Shift shift : Shift.values()) {
                int count = schedule.getSchedule().get(day).get(shift).size();
                System.out.printf("%s(%d) ", shift.getDisplayName().substring(0, 3), count);
            }
            System.out.println();
        }
        
        System.out.println("\n✓ Summary exported to console. In a real application, this would be saved to a file.");
    }
    
    public static void main(String[] args) {
        ScheduleManager manager = new ScheduleManager();
        manager.run();
    }
} 