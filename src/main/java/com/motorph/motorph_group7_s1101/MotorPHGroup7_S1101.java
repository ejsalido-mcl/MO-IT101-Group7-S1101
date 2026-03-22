/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.motorph.motorph_group7_s1101;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MotorPHGroup7_S1101 {
    
    // Employee data arrays
    static int[] empIds;              // Employee ID numbers
    static String[] firstNames;        // First names
    static String[] lastNames;         // Last names
    static String[] birthdays;         // Birth dates
    static double[] basicSalaries;     // Monthly salary amounts
    
    // Attendance data arrays (max 6000 records)
    static final int MAX_ATTENDANCE = 6000;
    static int[] attEmpIds = new int[MAX_ATTENDANCE];        // Employee IDs from attendance
    static String[] attDates = new String[MAX_ATTENDANCE];    // Dates worked
    static String[] attLogins = new String[MAX_ATTENDANCE];   // Clock in times
    static String[] attLogouts = new String[MAX_ATTENDANCE];  // Clock out times
    static int attendanceCount = 0;  // Total attendance records loaded
    
    /**
     * Splits a CSV line into fields, handling quoted commas
     */
    static String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
    
    /**
     * Counts rows in CSV file (excluding header)
     */
    static int countRows(BufferedReader reader) throws Exception {
        int count = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) count++;
        }
        return count;
    }
    
    /**
     * Loads employee data from CSV file
     */
    static void loadEmployeeData() {
        System.out.println("Loading employee data from CSV...");
        String empFile = "Resources/MotorPH_Employee Data.csv";
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(empFile));
            reader.readLine(); // Skip header
            
            int rowCount = countRows(reader);
            reader.close();
            
            if (rowCount == 0) {
                System.out.println("ERROR: No employee data found");
                System.exit(1);
            }
            
            // Initialize arrays with exact size
            empIds = new int[rowCount];
            firstNames = new String[rowCount];
            lastNames = new String[rowCount];
            birthdays = new String[rowCount];
            basicSalaries = new double[rowCount];
            
            // Read data
            reader = new BufferedReader(new FileReader(empFile));
            reader.readLine(); // Skip header
            
            int index = 0;
            String line;
            while ((line = reader.readLine()) != null && index < rowCount) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = parseCSVLine(line);
                if (data.length < 14) continue; // Need at least 14 columns
                
                try {
                    processEmployeeRow(data, index);
                    index++;
                } catch (Exception e) {
                    // Skip invalid rows
                }
            }
            
            reader.close();
            System.out.println("Successfully loaded " + index + " employees");
            
        } catch (Exception e) {
            System.out.println("ERROR: Cannot read employee CSV file");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Processes a single employee row from CSV
     */
    static void processEmployeeRow(String[] data, int index) {
        // Remove extra spaces from all fields
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }
        
        // Parse employee ID (remove decimal point if present, e.g., "10001.0" -> "10001")
        String idStr = cleanIdString(data[0]);
        if (!idStr.isEmpty()) {
            empIds[index] = Integer.parseInt(idStr);
        } else {
            throw new IllegalArgumentException("Empty employee ID");
        }
        
        // Store basic information
        lastNames[index] = data[1];   // Last name
        firstNames[index] = data[2];  // First name
        birthdays[index] = data[3];   // Birthday
        
        // Parse salary (remove commas, e.g., "90,000" -> "90000")
        String salaryStr = data[13].replace(",", "");
        if (!salaryStr.isEmpty()) {
            basicSalaries[index] = Double.parseDouble(salaryStr);
        }
    }
    
    /**
     * Cleans ID string by removing decimal points and commas
     */
    static String cleanIdString(String idStr) {
        if (idStr.contains(".")) {
            idStr = idStr.substring(0, idStr.indexOf("."));
        }
        return idStr.replace(",", "");
    }
    
    /**
     * Loads attendance data from CSV file
     */
    static void loadAttendanceData() {
        System.out.println("Loading attendance data from CSV...");
        String attFile = "Resources/Attendance Record.csv";
        attendanceCount = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(attFile));
            reader.readLine(); // Skip header
            
            String line;
            while ((line = reader.readLine()) != null && attendanceCount < MAX_ATTENDANCE) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = parseCSVLine(line);
                if (data.length < 6) continue; // Need at least 6 columns
                
                try {
                    processAttendanceRow(data, attendanceCount);
                    attendanceCount++;
                } catch (Exception e) {
                    // Skip invalid rows
                }
            }
            
            reader.close();
            System.out.println("Successfully loaded " + attendanceCount + " attendance records");
            
        } catch (Exception e) {
            System.out.println("ERROR: Cannot read attendance CSV");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Processes a single attendance row from CSV
     */
    static void processAttendanceRow(String[] data, int index) {
        // Remove extra spaces
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].trim();
        }
        
        // Parse employee ID
        String empIdStr = cleanIdString(data[0]);
        attEmpIds[index] = Integer.parseInt(empIdStr);
        
        // Parse date (convert MM/DD/YYYY to YYYY-MM-DD)
        attDates[index] = formatDate(data[3]);
        
        // Parse times (add :00 if seconds missing)
        attLogins[index] = formatTime(data[4]);
        attLogouts[index] = formatTime(data[5]);
    }
    
    /**
     * Converts date from MM/DD/YYYY to YYYY-MM-DD format
     */
    static String formatDate(String dateStr) {
        if (dateStr.contains("/")) {
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                return parts[2] + "-" + parts[0] + "-" + parts[1];
            }
        }
        return dateStr;
    }
    
    /**
     * Adds :00 to time if seconds are missing (e.g., "8:30" -> "8:30:00")
     */
    static String formatTime(String timeStr) {
        if (!timeStr.contains(":")) {
            return timeStr + ":00";
        }
        return timeStr;
    }
    
    /**
     * Calculates hours worked with grace period and lunch deduction
     */
    static double computeHours(String loginStr, String logoutStr) {
        try {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
            LocalTime login = LocalTime.parse(loginStr.trim(), timeFormat);
            LocalTime logout = LocalTime.parse(logoutStr.trim(), timeFormat);
            
            // Define work schedule times
            LocalTime workStart = LocalTime.of(8, 0);      // 8:00 AM
            LocalTime graceEnd = LocalTime.of(8, 10);      // 8:10 AM (grace period ends)
            LocalTime lunchStart = LocalTime.of(12, 0);    // 12:00 NN
            LocalTime lunchEnd = LocalTime.of(13, 0);      // 1:00 PM
            LocalTime workEnd = LocalTime.of(17, 0);       // 5:00 PM
            
            // Cap logout at end of work day (no overtime)
            if (logout.isAfter(workEnd)) {
                logout = workEnd;
            }
            
            // Apply grace period (arrival by 8:10 counts as 8:00)
            LocalTime effectiveLogin = login;
            if (!login.isBefore(workStart) && !login.isAfter(graceEnd)) {
                effectiveLogin = workStart;
            }
            
            // Calculate minutes worked
            long minutesWorked = Duration.between(effectiveLogin, logout).toMinutes();
            if (minutesWorked < 0) return 0;
            
            // Subtract 1 hour lunch break if worked through lunch
            if (effectiveLogin.isBefore(lunchEnd) && logout.isAfter(lunchStart)) {
                minutesWorked -= 60;
            }
            
            double hours = minutesWorked / 60.0;
            return Math.min(hours, 8.0); // Max 8 hours per day
            
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Calculates SSS contribution based on salary bracket
     */
    static double computeSSS(double salary) {
        if (salary < 3250) return 135.00;
        else if (salary < 3750) return 157.50;
        else if (salary < 4250) return 180.00;
        else if (salary < 4750) return 202.50;
        else if (salary < 5250) return 225.00;
        else if (salary < 5750) return 247.50;
        else if (salary < 6250) return 270.00;
        else if (salary < 6750) return 292.50;
        else if (salary < 7250) return 315.00;
        else if (salary < 7750) return 337.50;
        else if (salary < 8250) return 360.00;
        else if (salary < 8750) return 382.50;
        else if (salary < 9250) return 405.00;
        else if (salary < 9750) return 427.50;
        else if (salary < 10250) return 450.00;
        else if (salary < 10750) return 472.50;
        else if (salary < 11250) return 495.00;
        else if (salary < 11750) return 517.50;
        else if (salary < 12250) return 540.00;
        else if (salary < 12750) return 562.50;
        else if (salary < 13250) return 585.00;
        else if (salary < 13750) return 607.50;
        else if (salary < 14250) return 630.00;
        else if (salary < 14750) return 652.50;
        else if (salary < 15250) return 675.00;
        else if (salary < 15750) return 697.50;
        else if (salary < 16250) return 720.00;
        else if (salary < 16750) return 742.50;
        else if (salary < 17250) return 765.00;
        else if (salary < 17750) return 787.50;
        else if (salary < 18250) return 810.00;
        else if (salary < 18750) return 832.50;
        else if (salary < 19250) return 855.00;
        else if (salary < 19750) return 877.50;
        else if (salary < 20250) return 900.00;
        else if (salary < 20750) return 922.50;
        else if (salary < 21250) return 945.00;
        else if (salary < 21750) return 967.50;
        else if (salary < 22250) return 990.00;
        else if (salary < 22750) return 1012.50;
        else if (salary < 23250) return 1035.00;
        else if (salary < 23750) return 1057.50;
        else if (salary < 24250) return 1080.00;
        else if (salary < 24750) return 1102.50;
        else return 1125.00; // Highest bracket
    }
    
    /**
     * Calculates PhilHealth contribution (3% of salary, employee pays half)
     */
    static double computePhilhealth(double salary) {
        double premium;
        if (salary <= 10000) {
            premium = 300; // Minimum premium
        } else if (salary >= 60000) {
            premium = 1800; // Maximum premium
        } else {
            premium = salary * 0.03; // 3% of salary
        }
        return premium / 2; // Employee pays half
    }
    
    /**
     * Calculates Pag-IBIG contribution (1% or 2% of salary, max 100)
     */
    static double computePagibig(double salary) {
        double contribution;
        if (salary <= 1500) {
            contribution = salary * 0.01; // 1% for low income
        } else {
            contribution = salary * 0.02; // 2% for higher income
        }
        if (contribution > 100) {
            contribution = 100; // Cap at 100
        }
        return contribution;
    }

    /**
     * Calculates withholding tax based on TRAIN Law brackets
     */
    static double computeMonthlyTax(double taxableIncome) {
        if (taxableIncome <= 20832) {
            return 0;
        } 
        else if (taxableIncome <= 33332) {
            return (taxableIncome - 20833) * 0.20;
        } 
        else if (taxableIncome <= 66666) {
            return 2500 + (taxableIncome - 33333) * 0.25;
        } 
        else if (taxableIncome <= 166666) {
            return 10833 + (taxableIncome - 66667) * 0.30;
        } 
        else if (taxableIncome <= 666666) {
            return 40833.33 + (taxableIncome - 166667) * 0.32;
        } 
        else {
            return 200833.33 + (taxableIncome - 666667) * 0.35;
        }
    }
    
    /**
     * Finds employee index by ID, returns -1 if not found
     */
    static int findEmployeeIndex(int id) {
        for (int i = 0; i < empIds.length; i++) {
            if (empIds[i] == id) return i;
        }
        return -1;
    }
    
    /**
     * Returns month name from month number
     */
    static String getMonthName(int month) {
        if (month == 1) return "January";
        if (month == 2) return "February";
        if (month == 3) return "March";
        if (month == 4) return "April";
        if (month == 5) return "May";
        if (month == 6) return "June";
        if (month == 7) return "July";
        if (month == 8) return "August";
        if (month == 9) return "September";
        if (month == 10) return "October";
        if (month == 11) return "November";
        if (month == 12) return "December";
        return "Unknown";
    }
    
    /**
     * Returns last day of month (simplified for June-December 2024)
     */
    static int getLastDayOfMonth(int month, int year) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30; // April, June, September, November have 30 days
        } 
        else if (month == 2) {
            return 28; // February (not used in our data)
        }
        else {
            return 31; // All other months have 31 days
        }
    }
    
    /**
     * Validates login credentials
     */
    static boolean isValidLogin(String username, String password) {
        return (username.equals("employee") || username.equals("payroll_staff")) && password.equals("12345");
    }
    
    /**
     * Displays employee profile information
     */
    static void showEmployeeDetails(int index) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                         EMPLOYEE PROFILE");
        System.out.println("=".repeat(70));
        System.out.println("Employee ID  : " + empIds[index]);
        System.out.println("Name         : " + firstNames[index] + " " + lastNames[index]);
        System.out.println("Birthday     : " + birthdays[index]);
        System.out.println("=".repeat(70));
    }
    
    /**
     * Employee menu - loops until exit
     */
    static void employeeMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("                         EMPLOYEE MENU");
            System.out.println("=".repeat(70));
            System.out.println("1. View My Profile");
            System.out.println("2. Exit");
            System.out.print("Choice: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }
            
            if (choice == 2) {
                System.out.println("\nThank you for using MotorPH Group 7 System.");
                System.exit(0);
            } else if (choice == 1) {
                promptEmployeeNumber(scanner);
            } else {
                System.out.println("Invalid option. Please choose 1 or 2.");
            }
        }
    }
    
    /**
     * Prompts for employee number with loopback on invalid input
     */
    static void promptEmployeeNumber(Scanner scanner) {
        while (true) {
            System.out.print("\nEnter your employee number: ");
            int empId;
            try {
                empId = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
            
            int index = findEmployeeIndex(empId);
            if (index == -1) {
                System.out.println("Employee number does not exist. Please try again.");
            } else {
                showEmployeeDetails(index);
                break;
            }
        }
    }
    
    /**
     * Returns hourly rate (calculated from monthly salary)
     */
    static double getHourlyRate(int empIndex) {
        return basicSalaries[empIndex] / 21.0 / 8.0;
    }
    
    /**
     * Calculates payroll for an employee in a specific month
     */
    static Object[][] calculateEmployeePayroll(int empIndex, int month, int year) {
        double hourlyRate = getHourlyRate(empIndex);
        double monthlySalary = basicSalaries[empIndex];
        
        double firstCutoffHours = 0;
        int firstCutoffDays = 0;
        double secondCutoffHours = 0;
        int secondCutoffDays = 0;
        
        // Loop through all attendance records
        for (int i = 0; i < attendanceCount; i++) {
            if (attEmpIds[i] != empIds[empIndex]) continue; // Skip other employees
            
            String date = attDates[i];
            if (date == null) continue;
            
            // Parse date (format: YYYY-MM-DD)
            String[] dateParts = date.split("-");
            if (dateParts.length < 3) continue;
            
            int recordYear = Integer.parseInt(dateParts[0]);
            int recordMonth = Integer.parseInt(dateParts[1]);
            int recordDay = Integer.parseInt(dateParts[2]);
            
            // Skip if not the requested month/year
            if (recordYear != year || recordMonth != month) continue;
            
            double hours = computeHours(attLogins[i], attLogouts[i]);
            
            // Add to appropriate cutoff
            if (recordDay <= 15) {
                firstCutoffHours += hours;
                firstCutoffDays++;
            } else {
                secondCutoffHours += hours;
                secondCutoffDays++;
            }
        }
        
        // Calculate gross pay
        double firstCutoffGross = firstCutoffHours * hourlyRate;
        double secondCutoffGross = secondCutoffHours * hourlyRate;
        double totalMonthlyGross = firstCutoffGross + secondCutoffGross;
        
        // Calculate deductions
        double sss = 0, philhealth = 0, pagibig = 0, tax = 0;
        
        if (totalMonthlyGross > 0) {
            sss = computeSSS(monthlySalary);
            philhealth = computePhilhealth(monthlySalary);
            pagibig = computePagibig(monthlySalary);
            
            double totalGovDeductions = sss + philhealth + pagibig;
            double taxableIncome = totalMonthlyGross - totalGovDeductions;
            if (taxableIncome > 0) {
                tax = computeMonthlyTax(taxableIncome);
            }
        }
        
        double totalDeductions = sss + philhealth + pagibig + tax;
        double secondCutoffNet = secondCutoffGross - totalDeductions;
        
        // Return data as 2D array (row0=first cutoff, row1=second cutoff)
        return new Object[][] {
            {firstCutoffDays, firstCutoffHours, firstCutoffGross},
            {secondCutoffDays, secondCutoffHours, secondCutoffGross, secondCutoffNet,
             sss, philhealth, pagibig, tax, totalDeductions, totalMonthlyGross}
        };
    }
    
    /**
     * Displays detailed payroll for a single employee
     */
    static void displaySingleEmployeePayroll(int empIndex) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         EMPLOYEE PAYROLL SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("Employee #      : " + empIds[empIndex]);
        System.out.println("Employee Name   : " + firstNames[empIndex] + " " + lastNames[empIndex]);
        System.out.println("Birthday        : " + birthdays[empIndex]);
        System.out.println("Hourly Rate     : Php " + getHourlyRate(empIndex));
        
        boolean hasData = false;
        
        // Loop through months June to December 2024
        for (int month = 6; month <= 12; month++) {
            int year = 2024;
            Object[][] payrollData = calculateEmployeePayroll(empIndex, month, year);
            
            // Extract first cutoff data (row 0)
            int firstCutoffDays = (int) payrollData[0][0];
            double firstCutoffHours = (double) payrollData[0][1];
            double firstCutoffGross = (double) payrollData[0][2];
            
            // Extract second cutoff data (row 1)
            int secondCutoffDays = (int) payrollData[1][0];
            double secondCutoffHours = (double) payrollData[1][1];
            double secondCutoffGross = (double) payrollData[1][2];
            double secondCutoffNet = (double) payrollData[1][3];
            double sss = (double) payrollData[1][4];
            double philhealth = (double) payrollData[1][5];
            double pagibig = (double) payrollData[1][6];
            double tax = (double) payrollData[1][7];
            double totalDeductions = (double) payrollData[1][8];
            
            int lastDay = getLastDayOfMonth(month, year);
            String monthName = getMonthName(month);
            
            // Skip months with no attendance data
            if (firstCutoffDays == 0 && secondCutoffDays == 0) continue;
            
            hasData = true;
            
            System.out.println("\n" + "-".repeat(80));
            System.out.println("MONTH: " + monthName + " " + year);
            System.out.println("-".repeat(80));
            
            // Display first cutoff (days 1-15)
            if (firstCutoffDays > 0) {
                System.out.println("Cutoff Date     : " + monthName + " 1 to 15");
                System.out.println("Total Hours Worked : " + firstCutoffHours);
                System.out.println("Gross Salary    : Php " + firstCutoffGross);
                System.out.println("Net Salary      : Php " + firstCutoffGross);
            } else {
                System.out.println("Cutoff Date     : " + monthName + " 1 to 15");
                System.out.println("No days recorded for this period.");
            }
            
            System.out.println();
            
            // Display second cutoff (days 16-end)
            if (secondCutoffDays > 0) {
                System.out.println("Cutoff Date     : " + monthName + " 16 to " + lastDay);
                System.out.println("Total Hours Worked : " + secondCutoffHours);
                System.out.println("Gross Salary    : Php " + secondCutoffGross);
                System.out.println("Each Deduction:");
                System.out.println("    SSS         : Php " + sss);
                System.out.println("    PhilHealth  : Php " + philhealth);
                System.out.println("    Pag-IBIG    : Php " + pagibig);
                System.out.println("    Tax         : Php " + tax);
                System.out.println("Total Deductions: Php " + totalDeductions);
                System.out.println("Net Salary      : Php " + secondCutoffNet);
            } else {
                System.out.println("Cutoff Date     : " + monthName + " 16 to " + lastDay);
                System.out.println("No days recorded for this period.");
            }
        }
        
        if (!hasData) {
            System.out.println("\nNo attendance data found for this employee.");
        }
        System.out.println("=".repeat(80));
    }
    
    /**
     * Displays payroll summary for all employees (compact view)
     */
    static void displayAllEmployeesPayroll() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                    PAYROLL SUMMARY - ALL EMPLOYEES");
        System.out.println("=".repeat(100));
        
        for (int i = 0; i < empIds.length; i++) {
            if (empIds[i] == 0) continue; // Skip empty slots
            
            System.out.println("\n" + "-".repeat(100));
            System.out.println("EMPLOYEE: " + firstNames[i] + " " + lastNames[i] + " (ID: " + empIds[i] + ")");
            System.out.println("Birthday: " + birthdays[i]);
            System.out.println("-".repeat(100));
            
            boolean hasData = false;
            
            for (int month = 6; month <= 12; month++) {
                int year = 2024;
                Object[][] payrollData = calculateEmployeePayroll(i, month, year);
                
                int firstCutoffDays = (int) payrollData[0][0];
                double firstCutoffHours = (double) payrollData[0][1];
                double firstCutoffGross = (double) payrollData[0][2];
                
                int secondCutoffDays = (int) payrollData[1][0];
                double secondCutoffHours = (double) payrollData[1][1];
                double secondCutoffGross = (double) payrollData[1][2];
                double secondCutoffNet = (double) payrollData[1][3];
                double sss = (double) payrollData[1][4];
                double philhealth = (double) payrollData[1][5];
                double pagibig = (double) payrollData[1][6];
                double tax = (double) payrollData[1][7];
                double totalDeductions = (double) payrollData[1][8];
                
                int lastDay = getLastDayOfMonth(month, year);
                String monthName = getMonthName(month);
                
                if (firstCutoffDays == 0 && secondCutoffDays == 0) continue;
                
                hasData = true;
                System.out.println("\n  MONTH: " + monthName + " " + year);
                
                // First cutoff summary
                if (firstCutoffDays > 0) {
                    System.out.println("    First Cutoff (1-15): Hours=" + firstCutoffHours +
                                     ", Gross=Php " + firstCutoffGross +
                                     ", Net=Php " + firstCutoffGross);
                }
                
                // Second cutoff summary with deductions
                if (secondCutoffDays > 0) {
                    System.out.println("    Second Cutoff (16-" + lastDay + "): Hours=" + secondCutoffHours +
                                     ", Gross=Php " + secondCutoffGross);
                    System.out.println("        Deductions: SSS=Php " + sss +
                                     ", PhilHealth=Php " + philhealth +
                                     ", Pag-IBIG=Php " + pagibig +
                                     ", Tax=Php " + tax);
                    System.out.println("        Total Deductions=Php " + totalDeductions +
                                     ", Net=Php " + secondCutoffNet);
                }
            }
            
            if (!hasData) {
                System.out.println("  No attendance data found for this employee.");
            }
        }
        System.out.println("\n" + "=".repeat(100));
    }
    
    /**
     * Payroll staff menu
     */
    static void payrollStaffMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("                       PAYROLL STAFF MENU");
            System.out.println("=".repeat(70));
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit");
            System.out.print("Choice: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }
            
            if (choice == 2) {
                System.out.println("\nThank you for using MotorPH Group 7 System.");
                System.exit(0);
            } else if (choice == 1) {
                processPayrollMenu(scanner);
            } else {
                System.out.println("Invalid option. Please choose 1 or 2.");
            }
        }
    }
    
    /**
     * Payroll processing sub-menu
     */
    static void processPayrollMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("                       PROCESS PAYROLL");
            System.out.println("=".repeat(70));
            System.out.println("1. One employee");
            System.out.println("2. All employees");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }
            
            if (choice == 3) {
                System.out.println("\nThank you for using MotorPH Group 7 System.");
                System.exit(0);  // Exit the entire program
            } else if (choice == 1) {
                processOneEmployee(scanner);
            } else if (choice == 2) {
                displayAllEmployeesPayroll();
            } else {
                System.out.println("Invalid option. Please choose 1-3.");
            }
        }
    }
    
    /**
     * Process payroll for a single employee with loopback
     */
    static void processOneEmployee(Scanner scanner) {
        while (true) {
            System.out.print("\nEnter employee number: ");
            int empId;
            try {
                empId = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
            
            int index = findEmployeeIndex(empId);
            if (index == -1) {
                System.out.println("Employee number does not exist. Please try again.");
            } else {
                displaySingleEmployeePayroll(index);
                break;
            }
        }
    }
    
    /**
     * Main method - program entry point
     */
    public static void main(String[] args) {
        // Display welcome banner
        System.out.println("=".repeat(70));
        System.out.println("           MOTORPH GROUP 7 S1101 PAYROLL SYSTEM");
        System.out.println("=".repeat(70));
        
        // Load data from CSV files
        loadEmployeeData();
        loadAttendanceData();
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                   MOTORPH GROUP 7 S1101 SYSTEM");
        System.out.println("=".repeat(70));
        
        // Login
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (!isValidLogin(username, password)) {
            System.out.println("\nInvalid username or password.");
            System.out.println("Access denied.");
            scanner.close();
            return;
        }
        
        System.out.println("\nWelcome, " + username + "!");
        
        // Show appropriate menu
        if (username.equals("employee")) {
            employeeMenu(scanner);
        } else {
            payrollStaffMenu(scanner);
        }
        
        scanner.close();
    }
}