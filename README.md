README - MotorPH Payroll System
Team Details
Name
Salido, Evan Jake	 - Attendance data loading, hours calculation logic, grace period implementation, CSV parsing, menu systems, Payroll calculation methods, Display Formatting
Guillo, Samantha	- Calculation of SSS, PhilHealth & Pag-IBIG
Noble, Treyzer - 	Project Planning
Pancito, Nica	- Documentation, comments, helps on display formatting
Espelita, Rina - Documentation, Readme File, comments

Program Details 
System Overview
The MotorPH Payroll System is a Java-based application that automates the calculation of employee salaries and government-mandated deductions. It reads employee information and daily attendance records from CSV files, processes the data according to Philippine labor regulations, and generates comprehensive payroll summaries.

How the System Works
1. Data Loading Phase
Employee Data: The system reads MotorPH_Employee Data.csv from the Resources folder, extracting:

Employee ID, name, and birthday

Basic monthly salary

The system automatically calculates hourly rates (monthly salary ÷ 21 working days ÷ 8 hours)

Attendance Data: The system reads Attendance Record.csv, processing:

Daily login and logout times for each employee

Dates are converted to YYYY-MM-DD format for consistency

Times are formatted to ensure proper calculation

2. Hours Worked Calculation
For each attendance record, the system calculates hours worked using these rules:

Standard Work Day: 8:00 AM to 5:00 PM (8 hours)

Grace Period: Employees who log in by 8:10 AM are counted as starting at 8:00 AM

Lunch Break: 1 hour unpaid break (12:00 PM to 1:00 PM) is automatically deducted

Overtime: Hours beyond 5:00 PM are not counted (capped at 8 hours per day)

Validation: Invalid or negative time entries return 0 hours

3. Payroll Computation
The system calculates payroll in two cutoffs per month (1st-15th and 16th-end of month):

Gross Pay Calculation:

Hours worked × Hourly rate = Gross pay per cutoff

Government Deductions:

SSS (Social Security System): Based on monthly salary brackets (₱135 to ₱1,125)

PhilHealth: 3% of monthly salary (minimum ₱300, maximum ₱1,800 total premium) with employee paying half

Pag-IBIG: 1% for salaries ≤ ₱1,500, 2% for higher salaries (maximum ₱100)

Withholding Tax: Based on TRAIN Law graduated tax table:

Up to ₱20,832: 0%

₱20,833 - ₱33,332: 20% over ₱20,833

₱33,333 - ₱66,666: ₱2,500 + 25% over ₱33,333

₱66,667 - ₱166,666: ₱10,833 + 30% over ₱66,667

₱166,667 - ₱666,666: ₱40,833.33 + 32% over ₱166,667

Over ₱666,667: ₱200,833.33 + 35% over ₱666,667

Net Pay Calculation:

Second cutoff gross pay - Total deductions = Net pay (deductions are applied to the second cutoff)

4. User Interface & Access Control
Login System:

Simple authentication with two user types

Credentials: username/password (employee/12345 or payroll_staff/12345)

Employee Menu:

View Personal Profile: Enter employee number to see ID, name, and birthday

Exit: Close the application

Payroll Staff Menu:

Process Payroll: Access payroll processing options

Exit: Close the application

Payroll Processing Options:

One Employee: Enter employee number to see detailed monthly payroll

All Employees: View compact payroll summary for all employees

Exit: Return to previous menu

5. Output Generation
Single Employee Payroll Display:

Employee information (ID, name, birthday, hourly rate)

Monthly breakdown (June-December 2024)

Per cutoff: hours worked, gross salary

Detailed deductions (SSS, PhilHealth, Pag-IBIG, tax)

Total deductions and net salary

All Employees Payroll Display:

Compact view showing each employee's data

Monthly summaries with hours and gross pay

Deduction breakdowns and net pay

Technical Specifications
Language: Java (version 8+)

Data Storage: CSV files

Key Java Concepts Used:

File I/O (BufferedReader, FileReader)

Collections (ArrayList, List)

Date/Time API (LocalTime, Duration)

Exception handling

Arrays and string manipulation

Static methods and variables

Limitations
Processes only June-December 2024

Maximum 6000 attendance records

No overtime pay calculation

Simple authentication (not secure for production)

All deductions are applied to second cutoff only

Project Plan Link
[Insert Google Drive, Trello, or other project management link here]

