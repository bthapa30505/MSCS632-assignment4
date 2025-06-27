from enum import Enum

class DayOfWeek(Enum):
    MONDAY = "Monday"
    TUESDAY = "Tuesday"
    WEDNESDAY = "Wednesday"
    THURSDAY = "Thursday"
    FRIDAY = "Friday"
    SATURDAY = "Saturday"
    SUNDAY = "Sunday"
    
    def get_display_name(self):
        return self.value
    
    def __str__(self):
        return self.value
    
    @classmethod
    def values(cls):
        return list(cls) 