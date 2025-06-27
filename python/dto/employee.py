from day_of_week import DayOfWeek
from shift import Shift

class Employee:
    def __init__(self, name):
        self.name = name
        self.preferences = {}  # Stores preferred shifts for each day
        self.assigned_shifts = {}  # Stores actual assigned shifts
        self.days_worked = 0
        
        # Initialize preferences for all days
        for day in DayOfWeek.values():
            self.preferences[day] = []
    
    def get_name(self):
        return self.name
    
    # Add a preferred shift for a specific day
    def add_preference(self, day, shift):
        self.preferences[day].append(shift)
    
    # Get preferred shifts for a specific day
    def get_preferences(self, day):
        return self.preferences[day].copy()
    
    # Check if employee has any preferences for a day
    def has_preferences(self, day):
        return len(self.preferences[day]) > 0
    
    # Assign a shift to the employee for a specific day
    def assign_shift(self, day, shift):
        if not self.is_assigned(day):
            self.assigned_shifts[day] = shift
            self.days_worked += 1
    
    # Check if employee is already assigned on a specific day
    def is_assigned(self, day):
        return day in self.assigned_shifts
    
    # Get assigned shift for a specific day
    def get_assigned_shift(self, day):
        return self.assigned_shifts.get(day)
    
    # Get number of days worked
    def get_days_worked(self):
        return self.days_worked
    
    # Check if employee can work more days (max 5 days per week)
    def can_work_more_days(self):
        return self.days_worked < 5
    
    # Check if employee is available for a specific day
    def is_available(self, day):
        return not self.is_assigned(day) and self.can_work_more_days()
    
    # Clear all assignments (useful for regenerating schedule)
    def clear_assignments(self):
        self.assigned_shifts.clear()
        self.days_worked = 0
    
    # Get all assigned days
    def get_assigned_days(self):
        return set(self.assigned_shifts.keys())
    
    def __str__(self):
        return self.name
    
    def __eq__(self, other):
        if not isinstance(other, Employee):
            return False
        return self.name == other.name
    
    def __hash__(self):
        return hash(self.name) 