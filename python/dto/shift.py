from enum import Enum

class Shift(Enum):
    MORNING = "Morning"
    AFTERNOON = "Afternoon"
    EVENING = "Evening"
    
    def get_display_name(self):
        return self.value
    
    def __str__(self):
        return self.value
    
    @classmethod
    def values(cls):
        return list(cls) 