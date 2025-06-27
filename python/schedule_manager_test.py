from dto.day_of_week import DayOfWeek
from dto.shift import Shift
from dto.employee import Employee
from schedule import Schedule

def main():
    print("=== SCHEDULE MANAGER TEST WITH PREDEFINED EMPLOYEES ===\n")
    
    # Create a new schedule
    schedule = Schedule()
    
    # Create 10 employees with different preferences
    create_test_employees(schedule)
    
    # Generate and display the schedule
    print("Generating schedule...\n")
    schedule.generate_schedule()
    
    # Display the results
    schedule.print_schedule()

def create_test_employees(schedule):
    print("Creating 10 employees with varied preferences:\n")
    
    # Group 1: Morning preference (2 employees)
    bishal = Employee("bishal")
    for day in DayOfWeek.values():
        bishal.add_preference(day, Shift.MORNING)
    schedule.add_employee(bishal)
    print("✓ Added bishal (Morning preference - all days)")
    
    sibika = Employee("sibika")
    for day in DayOfWeek.values():
        sibika.add_preference(day, Shift.MORNING)
    schedule.add_employee(sibika)
    print("✓ Added sibika (Morning preference - all days)")
    
    # Group 2: Afternoon preference (2 employees)
    anish = Employee("anish")
    for day in DayOfWeek.values():
        anish.add_preference(day, Shift.AFTERNOON)
    schedule.add_employee(anish)
    print("✓ Added anish (Afternoon preference - all days)")
    
    ram = Employee("ram")
    for day in DayOfWeek.values():
        ram.add_preference(day, Shift.AFTERNOON)
    schedule.add_employee(ram)
    print("✓ Added ram (Afternoon preference - all days)")
    
    # Group 3: Evening preference (2 employees)
    hari = Employee("hari")
    for day in DayOfWeek.values():
        hari.add_preference(day, Shift.EVENING)
    schedule.add_employee(hari)
    print("✓ Added hari (Evening preference - all days)")
    
    sita = Employee("sita")
    for day in DayOfWeek.values():
        sita.add_preference(day, Shift.EVENING)
    schedule.add_employee(sita)
    print("✓ Added sita (Evening preference - all days)")
    
    # Group 4: Weekday only preference (2 employees)
    gita = Employee("gita")
    weekdays = [DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
               DayOfWeek.THURSDAY, DayOfWeek.FRIDAY]
    for day in weekdays:
        gita.add_preference(day, Shift.MORNING)
        gita.add_preference(day, Shift.AFTERNOON)
        gita.add_preference(day, Shift.EVENING)
    schedule.add_employee(gita)
    print("✓ Added gita (Weekday only - all shifts)")
    
    sagar = Employee("sagar")
    for day in weekdays:
        sagar.add_preference(day, Shift.MORNING)
        sagar.add_preference(day, Shift.AFTERNOON)
        sagar.add_preference(day, Shift.EVENING)
    schedule.add_employee(sagar)
    print("✓ Added sagar (Weekday only - all shifts)")
    
    # Group 5: Weekday inclined preference (2 employees) - prefer weekdays but can work weekends
    manish = Employee("manish")
    # Strong preference for weekdays (multiple shifts)
    for day in weekdays:
        manish.add_preference(day, Shift.MORNING)
        manish.add_preference(day, Shift.AFTERNOON)
    # Light preference for weekends (single shift)
    manish.add_preference(DayOfWeek.SATURDAY, Shift.MORNING)
    manish.add_preference(DayOfWeek.SUNDAY, Shift.MORNING)
    schedule.add_employee(manish)
    print("✓ Added manish (Weekday inclined - prefers weekdays but available weekends)")
    
    rabin = Employee("rabin")
    # Strong preference for weekdays (multiple shifts)
    for day in weekdays:
        rabin.add_preference(day, Shift.AFTERNOON)
        rabin.add_preference(day, Shift.EVENING)
    # Light preference for weekends (single shift)
    rabin.add_preference(DayOfWeek.SATURDAY, Shift.EVENING)
    rabin.add_preference(DayOfWeek.SUNDAY, Shift.EVENING)
    schedule.add_employee(rabin)
    print("✓ Added rabin (Weekday inclined - prefers weekdays but available weekends)")
    
    print("\nEmployee Summary:")
    print("- Morning specialists: bishal, sibika")
    print("- Afternoon specialists: anish, ram")
    print("- Evening specialists: hari, sita")
    print("- Weekday only: gita, sagar")
    print("- Weekday inclined: manish, rabin")
    print(f"\nTotal employees: {len(schedule.get_employees())}")

if __name__ == "__main__":
    main() 