import random
from dto.day_of_week import DayOfWeek
from dto.shift import Shift


class ShiftSlot:
    def __init__(self, day, shift):
        self.day = day
        self.shift = shift

class Schedule:
    MIN_EMPLOYEES_PER_SHIFT = 2
    MAX_DAYS_PER_EMPLOYEE = 5
    
    def __init__(self):
        self.schedule = {}
        self.employees = []
        self.random = random.Random()
        
        # Initialize schedule structure
        for day in DayOfWeek.values():
            self.schedule[day] = {}
            for shift in Shift.values():
                self.schedule[day][shift] = []
    
    # Add an employee to the system
    def add_employee(self, employee):
        if employee not in self.employees:
            self.employees.append(employee)
    
    # Generate the complete schedule
    def generate_schedule(self):
        self.clear_schedule()
        
        # Phase 1: Fill all shifts with exactly 2 employees first
        self.fill_all_shifts_with_minimum_staff()
        
        # Phase 2: If there are remaining employees, distribute them to fill gaps
        self.distribute_remaining_employees()
        
        # Phase 3: Add a 3rd person to each shift if employees haven't reached 5 days
        self.add_third_person_to_shifts()
    
    # Clear all previous assignments
    def clear_schedule(self):
        for employee in self.employees:
            employee.clear_assignments()
        
        for day in DayOfWeek.values():
            for shift in Shift.values():
                self.schedule[day][shift].clear()
    
    # Phase 1: Fill all shifts with exactly 2 employees, prioritizing preference fulfillment
    def fill_all_shifts_with_minimum_staff(self):
        # Create a list of all shifts to fill, sorted by how many employees have preferences for them
        all_shifts = []
        for day in DayOfWeek.values():
            for shift in Shift.values():
                all_shifts.append(ShiftSlot(day, shift))
        
        # Sort shifts by preference popularity (least popular first to ensure coverage)
        all_shifts.sort(key=lambda slot: self.count_employees_with_preference(slot.day, slot.shift))
        
        # Fill each shift with exactly 2 employees
        for slot in all_shifts:
            self.fill_shift_with_exactly_2_employees(slot.day, slot.shift)
    
    # Fill a specific shift with exactly 2 employees
    def fill_shift_with_exactly_2_employees(self, day, shift):
        current_staff = self.schedule[day][shift]
        
        while len(current_staff) < self.MIN_EMPLOYEES_PER_SHIFT:
            best_employee = self.find_best_employee_for_shift(day, shift)
            if best_employee is not None:
                self.assign_employee_to_shift(best_employee, day, shift)
            else:
                break  # No more available employees
    
    # Find the best available employee for a specific shift
    def find_best_employee_for_shift(self, day, shift):
        available_employees = [emp for emp in self.employees if emp.is_available(day)]
        
        if not available_employees:
            return None
        
        # Prioritize employees who have this shift as their preference
        preferred_employees = [emp for emp in available_employees 
                             if shift in emp.get_preferences(day)]
        
        if preferred_employees:
            # Among preferred employees, choose the one with fewest days worked
            return min(preferred_employees, key=lambda emp: emp.get_days_worked())
        
        # If no preferred employees, choose any available employee with fewest days worked
        return min(available_employees, key=lambda emp: emp.get_days_worked())
    
    # Count how many employees have preference for a specific shift
    def count_employees_with_preference(self, day, shift):
        return sum(1 for emp in self.employees if shift in emp.get_preferences(day))
    
    # Phase 2: Distribute remaining employees to fill any remaining gaps
    def distribute_remaining_employees(self):
        # First, try to fill any shifts that still don't have 2 employees
        for day in DayOfWeek.values():
            for shift in Shift.values():
                while len(self.schedule[day][shift]) < self.MIN_EMPLOYEES_PER_SHIFT:
                    available_employee = self.find_best_employee_for_shift(day, shift)
                    if available_employee is not None:
                        self.assign_employee_to_shift(available_employee, day, shift)
                    else:
                        break
    
    # Phase 3: Add a third person to each shift if employees haven't reached 5 days
    def add_third_person_to_shifts(self):
        # Create list of all shifts that have exactly 2 employees
        shifts_with_two_employees = []
        for day in DayOfWeek.values():
            for shift in Shift.values():
                if len(self.schedule[day][shift]) == self.MIN_EMPLOYEES_PER_SHIFT:
                    shifts_with_two_employees.append(ShiftSlot(day, shift))
        
        # Sort shifts by preference popularity (most popular first for 3rd person assignment)
        shifts_with_two_employees.sort(
            key=lambda slot: self.count_employees_with_preference(slot.day, slot.shift), 
            reverse=True
        )
        
        # Try to add a 3rd person to each shift
        for slot in shifts_with_two_employees:
            third_employee = self.find_best_employee_for_third_slot(slot.day, slot.shift)
            if third_employee is not None and self.can_assign_third_person_to_shift(third_employee, slot.day, slot.shift):
                self.schedule[slot.day][slot.shift].append(third_employee)
                third_employee.assign_shift(slot.day, slot.shift)
    
    # Find the best employee to add as a 3rd person to a shift
    def find_best_employee_for_third_slot(self, day, shift):
        # Find employees who can work more days and are available for this day
        available_employees = [
            emp for emp in self.employees
            if emp.is_available(day) and 
               emp not in self.schedule[day][shift] and 
               emp.can_work_more_days()
        ]
        
        if not available_employees:
            return None
        
        # Prioritize employees who have this shift as their preference
        preferred_employees = [emp for emp in available_employees 
                             if shift in emp.get_preferences(day)]
        
        if preferred_employees:
            # Among preferred employees, choose the one with fewest days worked
            return min(preferred_employees, key=lambda emp: emp.get_days_worked())
        
        # If no preferred employees, choose any available employee with fewest days worked
        return min(available_employees, key=lambda emp: emp.get_days_worked())
    
    def can_assign_to_shift(self, employee, day, shift):
        # Check basic availability
        if not employee.is_available(day) or employee in self.schedule[day][shift]:
            return False
        
        # Always allow assignment if shift has less than minimum employees
        current_staff = self.schedule[day][shift]
        return len(current_staff) < self.MIN_EMPLOYEES_PER_SHIFT
    
    # Special method for assigning 3rd person (allows assignment even when shift has 2 employees)
    def can_assign_third_person_to_shift(self, employee, day, shift):
        # Check basic availability
        if not employee.is_available(day) or employee in self.schedule[day][shift]:
            return False
        
        # Allow assignment if employee can work more days and shift has exactly 2 employees
        current_staff = self.schedule[day][shift]
        return employee.can_work_more_days() and len(current_staff) == self.MIN_EMPLOYEES_PER_SHIFT
    
    def assign_employee_to_shift(self, employee, day, shift):
        if self.can_assign_to_shift(employee, day, shift):
            self.schedule[day][shift].append(employee)
            employee.assign_shift(day, shift)
    
    # Calculate optimal column widths based on content
    def calculate_optimal_column_widths(self):
        shifts = Shift.values()
        widths = []
        
        # Start with minimum width based on shift names
        for shift in shifts:
            widths.append(max(len(shift.get_display_name()) + 2, 15))  # Minimum 15 chars
        
        # Check all employee name combinations for each shift
        for day in DayOfWeek.values():
            for i, shift in enumerate(shifts):
                shift_employees = self.schedule[day][shift]
                if shift_employees:
                    employee_names = ", ".join(emp.get_name() for emp in shift_employees)
                    widths[i] = max(widths[i], len(employee_names) + 2)
        
        # Cap maximum width to prevent extremely wide tables
        for i in range(len(widths)):
            widths[i] = min(widths[i], 60)  # Maximum 60 chars per column
        
        return widths
    
    # Display the schedule in a readable format
    def print_schedule(self):
        print("\n=== WEEKLY EMPLOYEE SCHEDULE ===\n")
        
        # Calculate optimal column widths
        day_column_width = 12
        shift_column_widths = self.calculate_optimal_column_widths()
        
        # Print header
        print(f"{'Day':<{day_column_width}}", end="")
        shifts = Shift.values()
        for i, shift in enumerate(shifts):
            print(f"{shift.get_display_name():<{shift_column_widths[i]}}", end="")
        print()
        
        # Print separator line
        total_width = day_column_width + sum(shift_column_widths)
        print("-" * total_width)
        
        # Print schedule for each day
        for day in DayOfWeek.values():
            print(f"{day.get_display_name():<{day_column_width}}", end="")
            
            for i, shift in enumerate(shifts):
                shift_employees = self.schedule[day][shift]
                employee_names = "None" if not shift_employees else ", ".join(emp.get_name() for emp in shift_employees)
                print(f"{employee_names:<{shift_column_widths[i]}}", end="")
            print()
        
        # Print summary
        self.print_schedule_summary()
    
    def print_schedule_summary(self):
        print("\n=== SCHEDULE SUMMARY ===")
        
        # Employee workload
        print("Employee Workload:")
        for employee in self.employees:
            print(f"  {employee.get_name()}: {employee.get_days_worked()} days worked")
        
        # Coverage analysis
        print("\nCoverage Analysis:")
        total_shifts = 0
        covered_shifts = 0
        fully_staffed_shifts = 0
        
        for day in DayOfWeek.values():
            for shift in Shift.values():
                total_shifts += 1
                staff_count = len(self.schedule[day][shift])
                if staff_count > 0:
                    covered_shifts += 1
                if staff_count >= self.MIN_EMPLOYEES_PER_SHIFT:
                    fully_staffed_shifts += 1
        
        print(f"  Total shifts: {total_shifts}")
        print(f"  Covered shifts: {covered_shifts} ({covered_shifts * 100.0 / total_shifts:.1f}%)")
        print(f"  Fully staffed shifts: {fully_staffed_shifts} ({fully_staffed_shifts * 100.0 / total_shifts:.1f}%)")
        
        # Check for violations
        self.check_constraint_violations()
    
    def check_constraint_violations(self):
        print("\nConstraint Violations:")
        has_violations = False
        
        # Check max days per employee
        for employee in self.employees:
            if employee.get_days_worked() > self.MAX_DAYS_PER_EMPLOYEE:
                print(f"  WARNING: {employee.get_name()} works {employee.get_days_worked()} days (exceeds limit of {self.MAX_DAYS_PER_EMPLOYEE})")
                has_violations = True
        
        # Check minimum staffing
        for day in DayOfWeek.values():
            for shift in Shift.values():
                staff_count = len(self.schedule[day][shift])
                if staff_count > 0 and staff_count < self.MIN_EMPLOYEES_PER_SHIFT:
                    print(f"  WARNING: {day.get_display_name()} {shift.get_display_name()} shift has only {staff_count} employee(s) (minimum {self.MIN_EMPLOYEES_PER_SHIFT} required)")
                    has_violations = True
        
        if not has_violations:
            print("  No constraint violations found.")
    
    # Getters
    def get_employees(self):
        return self.employees.copy()
    
    def get_schedule(self):
        return self.schedule.copy() 