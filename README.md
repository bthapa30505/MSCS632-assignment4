# MSCS632-assignment4

This repository contains the java and python implementation of the program with following requirements:
1. Input and Storage:
The application should collect employee names and their preferred shifts (morning, afternoon, or evening) for each day of the week. Store this information in an appropriate data structure.
2. Scheduling Logic:
Assign shifts for employees. Ensure that:
No employee works more than one shift per day.
An employee can work a maximum of 5 days per week.
The company must have at least 2 employees per shift per day. If fewer than 2 employees are available for a shift, randomly assign additional employees who have not worked 5 days yet.
3. Shift Conflicts:
Implement logic to detect and resolve conflicts where an employee’s preferred shift is full for a given day. If a conflict occurs, assign the employee to another available shift on the same or next day.
4. Output:
After scheduling the shifts, the application should output the final schedule for the week in a readable format, indicating which employee is assigned to each shift on each day.

# Python code is present under python folder and Java code implementation is present under java folder.
