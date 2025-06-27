public class Demo {
    public static void main(String[] args) {
        System.out.println("=== EMPLOYEE SCHEDULE MANAGER DEMO ===\n");
        
        // Create schedule
        Schedule schedule = new Schedule();
        
        // Add sample employees
        Employee alice = new Employee("Alice");
        Employee bob = new Employee("Bob");
        Employee charlie = new Employee("Charlie");
        Employee diana = new Employee("Diana");
        Employee eve = new Employee("Eve");
        Employee frank = new Employee("Frank");
        
        schedule.addEmployee(alice);
        schedule.addEmployee(bob);
        schedule.addEmployee(charlie);
        schedule.addEmployee(diana);
        schedule.addEmployee(eve);
        schedule.addEmployee(frank);
        
        // Add sample preferences
        addSamplePreferences(alice, bob, charlie, diana, eve, frank);
        
        // Generate and display schedule
        System.out.println("Generating schedule...\n");
        schedule.generateSchedule();
        schedule.printSchedule();
        
        System.out.println("\n=== DEMO COMPLETED ===");
        System.out.println("This demonstrates the scheduling algorithm working with sample data.");
        System.out.println("To run the interactive version, compile and run ScheduleManager.java");
    }
    
    private static void addSamplePreferences(Employee alice, Employee bob, Employee charlie, 
                                           Employee diana, Employee eve, Employee frank) {
        
        // Alice prefers morning shifts
        for (DayOfWeek day : DayOfWeek.values()) {
            alice.addPreference(day, Shift.MORNING);
        }
        
        // Bob prefers afternoon shifts
        for (DayOfWeek day : DayOfWeek.values()) {
            bob.addPreference(day, Shift.AFTERNOON);
        }
        
        // Charlie prefers evening shifts
        for (DayOfWeek day : DayOfWeek.values()) {
            charlie.addPreference(day, Shift.EVENING);
        }
        
        // Diana has mixed preferences
        diana.addPreference(DayOfWeek.MONDAY, Shift.MORNING);
        diana.addPreference(DayOfWeek.TUESDAY, Shift.AFTERNOON);
        diana.addPreference(DayOfWeek.WEDNESDAY, Shift.EVENING);
        diana.addPreference(DayOfWeek.THURSDAY, Shift.MORNING);
        diana.addPreference(DayOfWeek.FRIDAY, Shift.AFTERNOON);
        diana.addPreference(DayOfWeek.SATURDAY, Shift.EVENING);
        diana.addPreference(DayOfWeek.SUNDAY, Shift.MORNING);
        
        // Eve has priority-based preferences
        eve.addPreferenceWithPriority(DayOfWeek.MONDAY, Shift.MORNING, 0);
        eve.addPreferenceWithPriority(DayOfWeek.MONDAY, Shift.AFTERNOON, 1);
        eve.addPreferenceWithPriority(DayOfWeek.MONDAY, Shift.EVENING, 2);
        
        eve.addPreferenceWithPriority(DayOfWeek.TUESDAY, Shift.AFTERNOON, 0);
        eve.addPreferenceWithPriority(DayOfWeek.TUESDAY, Shift.EVENING, 1);
        eve.addPreferenceWithPriority(DayOfWeek.TUESDAY, Shift.MORNING, 2);
        
        eve.addPreferenceWithPriority(DayOfWeek.WEDNESDAY, Shift.EVENING, 0);
        eve.addPreferenceWithPriority(DayOfWeek.WEDNESDAY, Shift.MORNING, 1);
        eve.addPreferenceWithPriority(DayOfWeek.WEDNESDAY, Shift.AFTERNOON, 2);
        
        eve.addPreferenceWithPriority(DayOfWeek.THURSDAY, Shift.MORNING, 0);
        eve.addPreferenceWithPriority(DayOfWeek.THURSDAY, Shift.AFTERNOON, 1);
        eve.addPreferenceWithPriority(DayOfWeek.THURSDAY, Shift.EVENING, 2);
        
        eve.addPreferenceWithPriority(DayOfWeek.FRIDAY, Shift.AFTERNOON, 0);
        eve.addPreferenceWithPriority(DayOfWeek.FRIDAY, Shift.EVENING, 1);
        eve.addPreferenceWithPriority(DayOfWeek.FRIDAY, Shift.MORNING, 2);
        
        eve.addPreferenceWithPriority(DayOfWeek.SATURDAY, Shift.EVENING, 0);
        eve.addPreferenceWithPriority(DayOfWeek.SATURDAY, Shift.MORNING, 1);
        eve.addPreferenceWithPriority(DayOfWeek.SATURDAY, Shift.AFTERNOON, 2);
        
        eve.addPreferenceWithPriority(DayOfWeek.SUNDAY, Shift.MORNING, 0);
        eve.addPreferenceWithPriority(DayOfWeek.SUNDAY, Shift.AFTERNOON, 1);
        eve.addPreferenceWithPriority(DayOfWeek.SUNDAY, Shift.EVENING, 2);
        
        // Frank has limited preferences
        frank.addPreference(DayOfWeek.MONDAY, Shift.MORNING);
        frank.addPreference(DayOfWeek.WEDNESDAY, Shift.AFTERNOON);
        frank.addPreference(DayOfWeek.FRIDAY, Shift.EVENING);
        frank.addPreference(DayOfWeek.SATURDAY, Shift.MORNING);
        frank.addPreference(DayOfWeek.SUNDAY, Shift.AFTERNOON);
    }
} 