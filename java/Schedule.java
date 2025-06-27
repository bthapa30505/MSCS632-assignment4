import java.util.*;

public class Schedule {
    private Map<DayOfWeek, Map<Shift, List<Employee>>> schedule;
    private List<Employee> employees;
    private static final int MIN_EMPLOYEES_PER_SHIFT = 2;
    private static final int MAX_DAYS_PER_EMPLOYEE = 5;
    
    public Schedule() {
        this.schedule = new HashMap<>();
        this.employees = new ArrayList<>();
        
        // Initialize schedule structure
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new HashMap<>());
            for (Shift shift : Shift.values()) {
                schedule.get(day).put(shift, new ArrayList<>());
            }
        }
    }
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
    
    public void generateSchedule() {
        // Clear all previous assignments
        for (Employee employee : employees) {
            employee.clearAssignments();
        }
        
        // Clear schedule
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                schedule.get(day).get(shift).clear();
            }
        }
        
        // First pass: assign employees to their preferred shifts
        assignPreferredShifts();
        
        // Second pass: ensure minimum employees per shift
        ensureMinimumEmployeesPerShift();
        
        // Third pass: resolve conflicts and fill gaps
        resolveConflictsAndFillGaps();
    }
    
    private void assignPreferredShifts() {
        for (Employee employee : employees) {
            for (DayOfWeek day : DayOfWeek.values()) {
                if (!employee.isAvailable(day)) continue;
                
                List<Shift> preferences = employee.getPreferences(day);
                boolean assigned = false;
                
                // Try to assign based on preferences (priority order)
                for (Shift preferredShift : preferences) {
                    if (preferredShift != null && canAssignToShift(day, preferredShift, employee)) {
                        assignEmployeeToShift(day, preferredShift, employee);
                        assigned = true;
                        break;
                    }
                }
                
                // If no preference-based assignment, try any available shift
                if (!assigned) {
                    for (Shift shift : Shift.values()) {
                        if (canAssignToShift(day, shift, employee)) {
                            assignEmployeeToShift(day, shift, employee);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private void ensureMinimumEmployeesPerShift() {
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                List<Employee> shiftEmployees = schedule.get(day).get(shift);
                
                while (shiftEmployees.size() < MIN_EMPLOYEES_PER_SHIFT) {
                    Employee availableEmployee = findAvailableEmployee(day, shift);
                    if (availableEmployee != null) {
                        assignEmployeeToShift(day, shift, availableEmployee);
                    } else {
                        // If no available employee, we'll handle this in the next pass
                        break;
                    }
                }
            }
        }
    }
    
    private void resolveConflictsAndFillGaps() {
        // Handle employees who couldn't be assigned to their preferences
        for (Employee employee : employees) {
            if (employee.getDaysWorked() == 0) {
                // Employee has no assignments, try to assign them anywhere
                for (DayOfWeek day : DayOfWeek.values()) {
                    if (!employee.isAvailable(day)) continue;
                    
                    for (Shift shift : Shift.values()) {
                        if (canAssignToShift(day, shift, employee)) {
                            assignEmployeeToShift(day, shift, employee);
                            break;
                        }
                    }
                    if (employee.getDaysWorked() > 0) break;
                }
            }
        }
        
        // Final check: ensure minimum employees per shift
        for (DayOfWeek day : DayOfWeek.values()) {
            for (Shift shift : Shift.values()) {
                List<Employee> shiftEmployees = schedule.get(day).get(shift);
                
                while (shiftEmployees.size() < MIN_EMPLOYEES_PER_SHIFT) {
                    Employee availableEmployee = findAvailableEmployee(day, shift);
                    if (availableEmployee != null) {
                        assignEmployeeToShift(day, shift, availableEmployee);
                    } else {
                        // If still can't find anyone, we'll need to reassign someone
                        Employee employeeToReassign = findEmployeeToReassign(day, shift);
                        if (employeeToReassign != null) {
                            // Remove from current assignment and assign to this shift
                            DayOfWeek currentDay = findEmployeeCurrentDay(employeeToReassign);
                            if (currentDay != null) {
                                Shift currentShift = employeeToReassign.getAssignedShift(currentDay);
                                schedule.get(currentDay).get(currentShift).remove(employeeToReassign);
                                employeeToReassign.clearAssignments();
                                assignEmployeeToShift(day, shift, employeeToReassign);
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private boolean canAssignToShift(DayOfWeek day, Shift shift, Employee employee) {
        return employee.isAvailable(day) && 
               !schedule.get(day).get(shift).contains(employee);
    }
    
    private void assignEmployeeToShift(DayOfWeek day, Shift shift, Employee employee) {
        schedule.get(day).get(shift).add(employee);
        employee.assignShift(day, shift);
    }
    
    private Employee findAvailableEmployee(DayOfWeek day, Shift shift) {
        for (Employee employee : employees) {
            if (canAssignToShift(day, shift, employee)) {
                return employee;
            }
        }
        return null;
    }
    
    private Employee findEmployeeToReassign(DayOfWeek day, Shift shift) {
        // Find an employee who is assigned to a shift that has more than minimum employees
        for (Employee employee : employees) {
            if (employee.isAssigned(day)) {
                Shift currentShift = employee.getAssignedShift(day);
                if (schedule.get(day).get(currentShift).size() > MIN_EMPLOYEES_PER_SHIFT) {
                    return employee;
                }
            }
        }
        return null;
    }
    
    private DayOfWeek findEmployeeCurrentDay(Employee employee) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (employee.isAssigned(day)) {
                return day;
            }
        }
        return null;
    }
    
    public void printSchedule() {
        System.out.println("\n=== WEEKLY EMPLOYEE SCHEDULE ===\n");
        
        // Print header
        System.out.printf("%-12s", "Day");
        for (Shift shift : Shift.values()) {
            System.out.printf("%-20s", shift.getDisplayName());
        }
        System.out.println();
        
        // Print separator
        System.out.println("-".repeat(72));
        
        // Print schedule for each day
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.printf("%-12s", day.getDisplayName());
            
            for (Shift shift : Shift.values()) {
                List<Employee> shiftEmployees = schedule.get(day).get(shift);
                String employeeNames = shiftEmployees.isEmpty() ? "None" : 
                    String.join(", ", shiftEmployees.stream().map(Employee::getName).toList());
                System.out.printf("%-20s", employeeNames);
            }
            System.out.println();
        }
        
        // Print summary
        System.out.println("\n=== SCHEDULE SUMMARY ===");
        for (Employee employee : employees) {
            System.out.printf("%s: %d days worked\n", employee.getName(), employee.getDaysWorked());
        }
    }
    
    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }
    
    public Map<DayOfWeek, Map<Shift, List<Employee>>> getSchedule() {
        return new HashMap<>(schedule);
    }
} 