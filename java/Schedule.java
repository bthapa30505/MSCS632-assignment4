import dtos.DayOfWeek;
import dtos.Employee;
import dtos.Shift;

import java.util.*;
import java.util.stream.Collectors;

public class Schedule {
    private Map<DayOfWeek, Map<Shift, List<Employee>>> schedule;
    private List<Employee> employees;
    private static final int MIN_EMPLOYEES_PER_SHIFT = 2;
    private static final int MAX_DAYS_PER_EMPLOYEE = 5;
    private Random random;
    
    public Schedule() {
        this.schedule = new HashMap<>();
        this.employees = new ArrayList<>();
        this.random = new Random();
        
        // Initialize schedule structure
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new HashMap<>());
            for (Shift shift : Shift.values()) {
                schedule.get(day).put(shift, new ArrayList<>());
            }
        }
    }
    
    // Add an employee to the system
    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
        }
    }
    
    // Generate the complete schedule
    public void generateSchedule() {
        clearSchedule();
        
        // Phase 1: Fill all shifts with exactly 2 employees first
        fillAllShiftsWithMinimumStaff();
        
        // Phase 2: If there are remaining employees, distribute them to fill gaps
        distributeRemainingEmployees();
        
        // Phase 3: Add a 3rd person to each shift if employees haven't reached 5 days
        addThirdPersonToShifts();
    }
    
    // Clear all previous assignments
    private void clearSchedule() {
        for (Employee employee : employees) {
            employee.clearAssignments();
        }
        
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                schedule.get(day).get(shift).clear();
            }
        }
    }
    
    // Phase 1: Fill all shifts with exactly 2 employees, prioritizing preference fulfillment
    private void fillAllShiftsWithMinimumStaff() {
        // Create a list of all shifts to fill, sorted by how many employees have preferences for them
        List<ShiftSlot> allShifts = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                allShifts.add(new ShiftSlot(day, shift));
            }
        }
        
        // Sort shifts by preference popularity (least popular first to ensure coverage)
        allShifts.sort((slot1, slot2) -> {
            int pref1 = countEmployeesWithPreference(slot1.day, slot1.shift);
            int pref2 = countEmployeesWithPreference(slot2.day, slot2.shift);
            return Integer.compare(pref1, pref2);
        });
        
        // Fill each shift with exactly 2 employees
        for (ShiftSlot slot : allShifts) {
            fillShiftWithExactly2Employees(slot.day, slot.shift);
        }
    }
    
    // Fill a specific shift with exactly 2 employees
    private void fillShiftWithExactly2Employees(DayOfWeek day, Shift shift) {
        List<Employee> currentStaff = schedule.get(day).get(shift);
        
        while (currentStaff.size() < MIN_EMPLOYEES_PER_SHIFT) {
            Employee bestEmployee = findBestEmployeeForShift(day, shift);
            if (bestEmployee != null) {
                assignEmployeeToShift(bestEmployee, day, shift);
            } else {
                break; // No more available employees
            }
        }
    }
    
    // Find the best available employee for a specific shift
    private Employee findBestEmployeeForShift(DayOfWeek day, Shift shift) {
        List<Employee> availableEmployees = employees.stream()
            .filter(emp -> emp.isAvailable(day))
            .collect(Collectors.toList());
        
        if (availableEmployees.isEmpty()) {
            return null;
        }
        
        // Prioritize employees who have this shift as their preference
        List<Employee> preferredEmployees = availableEmployees.stream()
            .filter(emp -> emp.getPreferences(day).contains(shift))
            .collect(Collectors.toList());
        
        if (!preferredEmployees.isEmpty()) {
            // Among preferred employees, choose the one with fewest days worked
            return preferredEmployees.stream()
                .min((emp1, emp2) -> Integer.compare(emp1.getDaysWorked(), emp2.getDaysWorked()))
                .orElse(null);
        }
        
        // If no preferred employees, choose any available employee with fewest days worked
        return availableEmployees.stream()
            .min((emp1, emp2) -> Integer.compare(emp1.getDaysWorked(), emp2.getDaysWorked()))
            .orElse(null);
    }
    
    // Count how many employees have preference for a specific shift
    private int countEmployeesWithPreference(DayOfWeek day, Shift shift) {
        return (int) employees.stream()
            .filter(emp -> emp.getPreferences(day).contains(shift))
            .count();
    }
    
    // Phase 2: Distribute remaining employees to fill any remaining gaps
    private void distributeRemainingEmployees() {
        // First, try to fill any shifts that still don't have 2 employees
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                while (schedule.get(day).get(shift).size() < MIN_EMPLOYEES_PER_SHIFT) {
                    Employee availableEmployee = findBestEmployeeForShift(day, shift);
                    if (availableEmployee != null) {
                        assignEmployeeToShift(availableEmployee, day, shift);
                    } else {
                        break;
                    }
                }
            }
        }
    }
    
    // Phase 3: Add a third person to each shift if employees haven't reached 5 days
    private void addThirdPersonToShifts() {
        // Create list of all shifts that have exactly 2 employees
        List<ShiftSlot> shiftsWithTwoEmployees = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                if (schedule.get(day).get(shift).size() == MIN_EMPLOYEES_PER_SHIFT) {
                    shiftsWithTwoEmployees.add(new ShiftSlot(day, shift));
                }
            }
        }
        
        // Sort shifts by preference popularity (most popular first for 3rd person assignment)
        shiftsWithTwoEmployees.sort((slot1, slot2) -> {
            int pref1 = countEmployeesWithPreference(slot1.day, slot1.shift);
            int pref2 = countEmployeesWithPreference(slot2.day, slot2.shift);
            return Integer.compare(pref2, pref1); // Reverse order - most popular first
        });
        
        // Try to add a 3rd person to each shift
        for (ShiftSlot slot : shiftsWithTwoEmployees) {
            Employee thirdEmployee = findBestEmployeeForThirdSlot(slot.day, slot.shift);
            if (thirdEmployee != null && canAssignThirdPersonToShift(thirdEmployee, slot.day, slot.shift)) {
                schedule.get(slot.day).get(slot.shift).add(thirdEmployee);
                thirdEmployee.assignShift(slot.day, slot.shift);
            }
        }
    }
    
    // Find the best employee to add as a 3rd person to a shift
    private Employee findBestEmployeeForThirdSlot(DayOfWeek day, Shift shift) {
        // Find employees who can work more days and are available for this day
        List<Employee> availableEmployees = employees.stream()
            .filter(emp -> emp.isAvailable(day) && 
                          !schedule.get(day).get(shift).contains(emp) && 
                          emp.canWorkMoreDays())
            .collect(Collectors.toList());
        
        if (availableEmployees.isEmpty()) {
            return null;
        }
        
        // Prioritize employees who have this shift as their preference
        List<Employee> preferredEmployees = availableEmployees.stream()
            .filter(emp -> emp.getPreferences(day).contains(shift))
            .collect(Collectors.toList());
        
        if (!preferredEmployees.isEmpty()) {
            // Among preferred employees, choose the one with fewest days worked
            return preferredEmployees.stream()
                .min((emp1, emp2) -> Integer.compare(emp1.getDaysWorked(), emp2.getDaysWorked()))
                .orElse(null);
        }
        
        // If no preferred employees, choose any available employee with fewest days worked
        return availableEmployees.stream()
            .min((emp1, emp2) -> Integer.compare(emp1.getDaysWorked(), emp2.getDaysWorked()))
            .orElse(null);
    }
    
    // Helper class to represent a shift slot
    private static class ShiftSlot {
        final DayOfWeek day;
        final Shift shift;
        
        ShiftSlot(DayOfWeek day, Shift shift) {
            this.day = day;
            this.shift = shift;
        }
    }
    
    private boolean canAssignToShift(Employee employee, DayOfWeek day, Shift shift) {
        // Check basic availability
        if (!employee.isAvailable(day) || schedule.get(day).get(shift).contains(employee)) {
            return false;
        }
        
        // Always allow assignment if shift has less than minimum employees
        List<Employee> currentStaff = schedule.get(day).get(shift);
        return currentStaff.size() < MIN_EMPLOYEES_PER_SHIFT;
    }
    
    // Special method for assigning 3rd person (allows assignment even when shift has 2 employees)
    private boolean canAssignThirdPersonToShift(Employee employee, DayOfWeek day, Shift shift) {
        // Check basic availability
        if (!employee.isAvailable(day) || schedule.get(day).get(shift).contains(employee)) {
            return false;
        }
        
        // Allow assignment if employee can work more days and shift has exactly 2 employees
        List<Employee> currentStaff = schedule.get(day).get(shift);
        return employee.canWorkMoreDays() && currentStaff.size() == MIN_EMPLOYEES_PER_SHIFT;
    }
    
    private void assignEmployeeToShift(Employee employee, DayOfWeek day, Shift shift) {
        if (canAssignToShift(employee, day, shift)) {
            schedule.get(day).get(shift).add(employee);
            employee.assignShift(day, shift);
        }
    }
    
    // Display the schedule in a readable format
    public void printSchedule() {
        System.out.println("\n=== WEEKLY EMPLOYEE SCHEDULE ===\n");
        
        // Calculate optimal column widths
        int dayColumnWidth = 12;
        int[] shiftColumnWidths = calculateOptimalColumnWidths();
        
        // Print header
        System.out.printf("%-" + dayColumnWidth + "s", "Day");
        Shift[] shifts = Shift.values();
        for (int i = 0; i < shifts.length; i++) {
            System.out.printf("%-" + shiftColumnWidths[i] + "s", shifts[i].getDisplayName());
        }
        System.out.println();
        
        // Print separator line
        int totalWidth = dayColumnWidth;
        for (int width : shiftColumnWidths) {
            totalWidth += width;
        }
        for (int i = 0; i < totalWidth; i++) {
            System.out.print("-");
        }
        System.out.println();
        
        // Print schedule for each day
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.printf("%-" + dayColumnWidth + "s", day.getDisplayName());
            
            for (int i = 0; i < shifts.length; i++) {
                List<Employee> shiftEmployees = schedule.get(day).get(shifts[i]);
                String employeeNames = shiftEmployees.isEmpty() ? "None" : 
                    shiftEmployees.stream()
                        .map(Employee::getName)
                        .collect(Collectors.joining(", "));
                
                System.out.printf("%-" + shiftColumnWidths[i] + "s", employeeNames);
            }
            System.out.println();
        }
        
        // Print summary
        printScheduleSummary();
    }
    
    // Calculate optimal column widths based on content
    private int[] calculateOptimalColumnWidths() {
        Shift[] shifts = Shift.values();
        int[] widths = new int[shifts.length];
        
        // Start with minimum width based on shift names
        for (int i = 0; i < shifts.length; i++) {
            widths[i] = Math.max(shifts[i].getDisplayName().length() + 2, 15); // Minimum 15 chars
        }
        
        // Check all employee name combinations for each shift
        for (DayOfWeek day : DayOfWeek.values()) {
            for (int i = 0; i < shifts.length; i++) {
                List<Employee> shiftEmployees = schedule.get(day).get(shifts[i]);
                if (!shiftEmployees.isEmpty()) {
                    String employeeNames = shiftEmployees.stream()
                        .map(Employee::getName)
                        .collect(Collectors.joining(", "));
                    widths[i] = Math.max(widths[i], employeeNames.length() + 2);
                }
            }
        }
        
        // Cap maximum width to prevent extremely wide tables
        for (int i = 0; i < widths.length; i++) {
            widths[i] = Math.min(widths[i], 60); // Maximum 60 chars per column
        }
        
        return widths;
    }
    
    private void printScheduleSummary() {
        System.out.println("\n=== SCHEDULE SUMMARY ===");
        
        // Employee workload
        System.out.println("Employee Workload:");
        for (Employee employee : employees) {
            System.out.printf("  %s: %d days worked\n", employee.getName(), employee.getDaysWorked());
        }
        
        // Coverage analysis
        System.out.println("\nCoverage Analysis:");
        int totalShifts = 0;
        int coveredShifts = 0;
        int fullyStaffedShifts = 0;
        
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                totalShifts++;
                int staffCount = schedule.get(day).get(shift).size();
                if (staffCount > 0) coveredShifts++;
                if (staffCount >= MIN_EMPLOYEES_PER_SHIFT) fullyStaffedShifts++;
            }
        }
        
        System.out.printf("  Total shifts: %d\n", totalShifts);
        System.out.printf("  Covered shifts: %d (%.1f%%)\n", coveredShifts, (coveredShifts * 100.0 / totalShifts));
        System.out.printf("  Fully staffed shifts: %d (%.1f%%)\n", fullyStaffedShifts, (fullyStaffedShifts * 100.0 / totalShifts));
        
        // Check for violations
        checkConstraintViolations();
    }
    
    private void checkConstraintViolations() {
        System.out.println("\nConstraint Violations:");
        boolean hasViolations = false;
        
        // Check max days per employee
        for (Employee employee : employees) {
            if (employee.getDaysWorked() > MAX_DAYS_PER_EMPLOYEE) {
                System.out.printf("  WARNING: %s works %d days (exceeds limit of %d)\n", 
                    employee.getName(), employee.getDaysWorked(), MAX_DAYS_PER_EMPLOYEE);
                hasViolations = true;
            }
        }
        
        // Check minimum staffing
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                int staffCount = schedule.get(day).get(shift).size();
                if (staffCount > 0 && staffCount < MIN_EMPLOYEES_PER_SHIFT) {
                    System.out.printf("  WARNING: %s %s shift has only %d employee(s) (minimum %d required)\n", 
                        day.getDisplayName(), shift.getDisplayName(), staffCount, MIN_EMPLOYEES_PER_SHIFT);
                    hasViolations = true;
                }
            }
        }
        
        if (!hasViolations) {
            System.out.println("  No constraint violations found.");
        }
    }
    
    // Getters
    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }
    
    public Map<DayOfWeek, Map<Shift, List<Employee>>> getSchedule() {
        return new HashMap<>(schedule);
    }
} 