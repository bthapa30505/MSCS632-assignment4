from enum import Enum
from typing import Dict, List, Optional

class DayOfWeek(Enum):
    MONDAY = "Monday"
    TUESDAY = "Tuesday"
    WEDNESDAY = "Wednesday"
    THURSDAY = "Thursday"
    FRIDAY = "Friday"
    SATURDAY = "Saturday"
    SUNDAY = "Sunday"

class Shift(Enum):
    MORNING = "Morning"
    AFTERNOON = "Afternoon"
    EVENING = "Evening"

class Employee:
    def __init__(self, name: str):
        self.name = name
        self.preferences: Dict[DayOfWeek, List[Optional[Shift]]] = {
            day: [] for day in DayOfWeek
        }
        self.assigned_shifts: Dict[DayOfWeek, Shift] = {}
        self.days_worked = 0
    
    def add_preference(self, day: DayOfWeek, shift: Shift) -> None:
        """Add a shift preference for a specific day."""
        self.preferences[day].append(shift)
    
    def add_preference_with_priority(self, day: DayOfWeek, shift: Shift, priority: int) -> None:
        """Add a shift preference with specific priority for a day."""
        day_preferences = self.preferences[day]
        while len(day_preferences) <= priority:
            day_preferences.append(None)
        day_preferences[priority] = shift
    
    def get_preferences(self, day: DayOfWeek) -> List[Optional[Shift]]:
        """Get all preferences for a specific day."""
        return self.preferences[day].copy()
    
    def assign_shift(self, day: DayOfWeek, shift: Shift) -> None:
        """Assign an employee to a shift on a specific day."""
        if not self.is_assigned(day):
            self.assigned_shifts[day] = shift
            self.days_worked += 1
    
    def is_assigned(self, day: DayOfWeek) -> bool:
        """Check if employee is assigned to any shift on a specific day."""
        return day in self.assigned_shifts
    
    def get_assigned_shift(self, day: DayOfWeek) -> Optional[Shift]:
        """Get the assigned shift for a specific day."""
        return self.assigned_shifts.get(day)
    
    def can_work_more_days(self) -> bool:
        """Check if employee can work more days (max 5 days per week)."""
        return self.days_worked < 5
    
    def is_available(self, day: DayOfWeek) -> bool:
        """Check if employee is available to work on a specific day."""
        return not self.is_assigned(day) and self.can_work_more_days()
    
    def clear_assignments(self) -> None:
        """Clear all shift assignments and reset days worked."""
        self.assigned_shifts.clear()
        self.days_worked = 0
    
    def __str__(self) -> str:
        return self.name
    
    def __repr__(self) -> str:
        return f"Employee('{self.name}')" 