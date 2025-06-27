import dtos.*;

public class ScheduleManagerTest {
    public static void main(String[] args) {
        System.out.println("=== SCHEDULE MANAGER TEST WITH PREDEFINED EMPLOYEES ===\n");
        
        // Create a new schedule
        Schedule schedule = new Schedule();
        
        // Create 10 employees with different preferences
        createTestEmployees(schedule);
        
        // Generate and display the schedule
        System.out.println("Generating schedule...\n");
        schedule.generateSchedule();
        
        // Display the results
        schedule.printSchedule();
    }
    
    private static void createTestEmployees(Schedule schedule) {
        System.out.println("Creating 10 employees with varied preferences:\n");
        
        // Group 1: Morning preference (2 employees)
        Employee bishal = new Employee("bishal");
        for (DayOfWeek day : DayOfWeek.values()) {
            bishal.addPreference(day, Shift.MORNING);
        }
        schedule.addEmployee(bishal);
        System.out.println("✓ Added bishal (Morning preference - all days)");
        
        Employee sibika = new Employee("sibika");
        for (DayOfWeek day : DayOfWeek.values()) {
            sibika.addPreference(day, Shift.MORNING);
        }
        schedule.addEmployee(sibika);
        System.out.println("✓ Added sibika (Morning preference - all days)");
        
        // Group 2: Afternoon preference (2 employees)
        Employee anish = new Employee("anish");
        for (DayOfWeek day : DayOfWeek.values()) {
            anish.addPreference(day, Shift.AFTERNOON);
        }
        schedule.addEmployee(anish);
        System.out.println("✓ Added anish (Afternoon preference - all days)");
        
        Employee ram = new Employee("ram");
        for (DayOfWeek day : DayOfWeek.values()) {
            ram.addPreference(day, Shift.AFTERNOON);
        }
        schedule.addEmployee(ram);
        System.out.println("✓ Added ram (Afternoon preference - all days)");
        
        // Group 3: Evening preference (2 employees)
        Employee hari = new Employee("hari");
        for (DayOfWeek day : DayOfWeek.values()) {
            hari.addPreference(day, Shift.EVENING);
        }
        schedule.addEmployee(hari);
        System.out.println("✓ Added hari (Evening preference - all days)");
        
        Employee sita = new Employee("sita");
        for (DayOfWeek day : DayOfWeek.values()) {
            sita.addPreference(day, Shift.EVENING);
        }
        schedule.addEmployee(sita);
        System.out.println("✓ Added sita (Evening preference - all days)");
        
        // Group 4: Weekday only preference (2 employees)
        Employee gita = new Employee("gita");
        DayOfWeek[] weekdays = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
                               DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        for (DayOfWeek day : weekdays) {
            gita.addPreference(day, Shift.MORNING);
            gita.addPreference(day, Shift.AFTERNOON);
            gita.addPreference(day, Shift.EVENING);
        }
        schedule.addEmployee(gita);
        System.out.println("✓ Added gita (Weekday only - all shifts)");
        
        Employee sagar = new Employee("sagar");
        for (DayOfWeek day : weekdays) {
            sagar.addPreference(day, Shift.MORNING);
            sagar.addPreference(day, Shift.AFTERNOON);
            sagar.addPreference(day, Shift.EVENING);
        }
        schedule.addEmployee(sagar);
        System.out.println("✓ Added sagar (Weekday only - all shifts)");
        
        // Group 5: Weekday inclined preference (2 employees) - prefer weekdays but can work weekends
        Employee manish = new Employee("manish");
        // Strong preference for weekdays (multiple shifts)
        for (DayOfWeek day : weekdays) {
            manish.addPreference(day, Shift.MORNING);
            manish.addPreference(day, Shift.AFTERNOON);
        }
        // Light preference for weekends (single shift)
        manish.addPreference(DayOfWeek.SATURDAY, Shift.MORNING);
        manish.addPreference(DayOfWeek.SUNDAY, Shift.MORNING);
        schedule.addEmployee(manish);
        System.out.println("✓ Added manish (Weekday inclined - prefers weekdays but available weekends)");
        
        Employee rabin = new Employee("rabin");
        // Strong preference for weekdays (multiple shifts)
        for (DayOfWeek day : weekdays) {
            rabin.addPreference(day, Shift.AFTERNOON);
            rabin.addPreference(day, Shift.EVENING);
        }
        // Light preference for weekends (single shift)
        rabin.addPreference(DayOfWeek.SATURDAY, Shift.EVENING);
        rabin.addPreference(DayOfWeek.SUNDAY, Shift.EVENING);
        schedule.addEmployee(rabin);
        System.out.println("✓ Added rabin (Weekday inclined - prefers weekdays but available weekends)");
        
        System.out.println("\nEmployee Summary:");
        System.out.println("- Morning specialists: bishal, sibika");
        System.out.println("- Afternoon specialists: anish, ram");
        System.out.println("- Evening specialists: hari, sita");
        System.out.println("- Weekday only: gita, sagar");
        System.out.println("- Weekday inclined: manish, rabin");
        System.out.println("\nTotal employees: " + schedule.getEmployees().size());
    }
} 