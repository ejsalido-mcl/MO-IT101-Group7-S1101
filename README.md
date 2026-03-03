# README - MotorPH Payroll System

## Overview
A Java program that calculates employee payroll based on attendance records. It reads employee data and attendance logs from CSV files, computes hours worked, deducts government contributions, and generates payroll summaries.

## Features
- Load employee information and attendance records from CSV files
- Calculate hours worked (with grace period and lunch break)
- Compute government contributions (SSS, PhilHealth, Pag-IBIG) and withholding tax
- View employee profile
- Generate payroll for one or all employees
- Two user types: Employee and Payroll Staff

## Login Credentials
| User Type | Username | Password |
|-----------|----------|----------|
| Employee | `employee` | `12345` |
| Payroll Staff | `payroll_staff` | `12345` |

## CSV File Format

### Employee Data CSV
Must have these columns:
- Column 0: Employee #
- Column 1: Last Name
- Column 2: First Name
- Column 3: Birthday
- Column 13: Basic Salary

### Attendance Record CSV
Must have these columns:
- Column 0: Employee #
- Column 3: Date (MM/DD/YYYY format)
- Column 4: Log In (HH:MM format)
- Column 5: Log Out (HH:MM format)

## Menu Options

### Employee Menu
1. **View My Profile** - Enter employee number to see details
2. **Exit** - Close program

### Payroll Staff Menu
1. **Process Payroll** - Enter payroll menu
2. **Exit** - Close program

### Process Payroll Menu
1. **One employee** - Enter employee number to see detailed payroll
2. **All employees** - See summary for all employees
3. **Exit** - Back to main menu

## How Payroll is Calculated

### Hours Worked
- Work day: 8:00 AM to 5:00 PM
- Grace period: 10 minutes (login by 8:10 counted as 8:00)
- Lunch break: 1 hour unpaid (12:00-1:00 PM)
- Maximum 8 hours per day (no overtime)

### Deductions
- **SSS:** Based on salary bracket table
- **PhilHealth:** 3% of salary (employee pays half)
- **Pag-IBIG:** 1% or 2% of salary (max ₱100)
- **Tax:** Based on TRAIN Law brackets

## Key Java Concepts Used
- File I/O (BufferedReader, FileReader)
- ArrayList, List
- LocalTime, Duration, DateTimeFormatter
- Exception Handling (try-catch)
- Arrays and multidimensional arrays
- String manipulation and parsing
- Static methods and variables
- Control structures (loops, conditionals)

## Notes
- Only processes June-December 2024
---

**Group 7 S1101**
