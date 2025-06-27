# Employee Schedule Manager - Java Implementation

This Java application manages employee schedules for a company that operates 7 days a week with three shifts: Morning, Afternoon, and Evening.

## Features

1. **Employee Management**: Add employees with their names
2. **Shift Preferences**: Set preferred shifts for each day of the week
3. **Priority-based Preferences**: Option to rank shift preferences (1st, 2nd, 3rd choice)
4. **Automatic Scheduling**: Intelligent algorithm that:
   - Ensures no employee works more than one shift per day
   - Limits employees to maximum 5 days per week
   - Maintains minimum 2 employees per shift per day
   - Resolves conflicts when preferred shifts are full
5. **Schedule Output**: Displays the final weekly schedule in a readable format

## How to Compile and Run

### Prerequisites
- Java 8 or higher installed on your system

### Compilation
Open a terminal/command prompt in the `java` directory and run:

```bash
javac *.java
```

### Running the Application
After compilation, run the application:

```bash
java ScheduleManager
```

## Usage Example

1. **Add Employees**: Enter employee names one by one, type 'done' when finished
2. **Set Preferences**: For each employee, choose between:
   - Simple preferences: Enter one preferred shift per day
   - Priority-based preferences: Rank shifts (1st, 2nd, 3rd choice) for each day
3. **View Schedule**: The application will automatically generate and display the optimal schedule

### Sample Input:
```
Enter employee name (or 'done'): Alice
Enter employee name (or 'done'): Bob
Enter employee name (or 'done'): Charlie
Enter employee name (or 'done'): done

Use priority-based preferences? (y/n): n

Monday - Enter preferred shift (or 'none'): morning
Tuesday - Enter preferred shift (or 'none'): afternoon
...
```

## File Structure

- `Employee.java`: Represents an employee with preferences and assignments
- `DayOfWeek.java`: Enum for days of the week
- `Shift.java`: Enum for shift types (Morning, Afternoon, Evening)
- `Schedule.java`: Core scheduling logic and algorithm
- `ScheduleManager.java`: Main application class with user interface
- `README.md`: This documentation file

## Algorithm Overview

The scheduling algorithm works in three phases:

1. **Preferred Assignment**: Assigns employees to their preferred shifts when possible
2. **Minimum Coverage**: Ensures each shift has at least 2 employees
3. **Conflict Resolution**: Handles cases where preferences can't be met and fills remaining gaps

The algorithm respects all constraints:
- No employee works more than one shift per day
- Maximum 5 days per week per employee
- Minimum 2 employees per shift per day
- Prioritizes employee preferences when possible 