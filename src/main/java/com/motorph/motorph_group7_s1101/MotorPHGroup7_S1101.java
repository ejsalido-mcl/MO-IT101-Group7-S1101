/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.motorph.motorph_group7_s1101;
/**
 *
 * @author jake
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MotorPHGroup7_S1101 {
    
    static int[] empIds;
    static String[] firstNames;
    static String[] lastNames;
    static String[] birthdays;
    static String[] addresses;
    static String[] phoneNumbers;
    static String[] sssNumbers;
    static String[] philhealthNumbers;
    static String[] tinNumbers;
    static String[] pagibigNumbers;
    static String[] empStatus;
    static String[] positions;
    static String[] supervisors;
    static double[] basicSalaries;
    static double[] hourlyRates;
    
    static final int MAX_ATTENDANCE = 6000;
    
    static int[] attEmpIds = new int[MAX_ATTENDANCE];
    static String[] attDates = new String[MAX_ATTENDANCE];
    static String[] attLogins = new String[MAX_ATTENDANCE];
    static String[] attLogouts = new String[MAX_ATTENDANCE];
    
    static int attendanceCount = 0;
    
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
    //Load Employee Method
    static void loadEmployeeData() {
        System.out.println("Loading employee data from CSV...");
        
        String empFile = "Resources/MotorPH_Employee Data.csv";
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(empFile));
            reader.readLine();
            
            int rowCount = 0;
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rowCount++;
                }
            }
            
            reader.close();
            
            if (rowCount == 0) {
                System.out.println("ERROR: No employee data found in CSV file");
                System.exit(1);
                return;
            }
            
            empIds = new int[rowCount];
            firstNames = new String[rowCount];
            lastNames = new String[rowCount];
            birthdays = new String[rowCount];
            addresses = new String[rowCount];
            phoneNumbers = new String[rowCount];
            sssNumbers = new String[rowCount];
            philhealthNumbers = new String[rowCount];
            tinNumbers = new String[rowCount];
            pagibigNumbers = new String[rowCount];
            empStatus = new String[rowCount];
            positions = new String[rowCount];
            supervisors = new String[rowCount];
            basicSalaries = new double[rowCount];
            hourlyRates = new double[rowCount];
            
            reader = new BufferedReader(new FileReader(empFile));
            reader.readLine();
            
            int index = 0;
            
            while ((line = reader.readLine()) != null && index < rowCount) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = parseCSVLine(line);
                
                if (data.length < 14) continue;
                
                try {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }
                    
                    String idStr = data[0];
                    if (idStr.contains(".")) {
                        idStr = idStr.substring(0, idStr.indexOf("."));
                    }
                    idStr = idStr.replace(",", "");
                    
                    if (!idStr.isEmpty()) {
                        empIds[index] = Integer.parseInt(idStr);
                    } else {
                        continue;
                    }
                    
                    lastNames[index] = data[1];
                    firstNames[index] = data[2];
                    birthdays[index] = data[3];
                    addresses[index] = data[4];
                    phoneNumbers[index] = data[5];
                    sssNumbers[index] = data[6];
                    philhealthNumbers[index] = data[7];
                    tinNumbers[index] = data[8];
                    pagibigNumbers[index] = data[9];
                    empStatus[index] = data[10];
                    positions[index] = data[11];
                    
                    String supervisor = data[12];
                    supervisors[index] = supervisor.equals("N/A") ? "None" : supervisor;
                    
                    String salaryStr = data[13].replace(",", "");
                    if (!salaryStr.isEmpty()) {
                        basicSalaries[index] = Double.parseDouble(salaryStr);
                    }
                    
                    if (basicSalaries[index] > 0) {
                        hourlyRates[index] = basicSalaries[index] / 21 / 8;
                    }
                    
                    index++;
                    
                } catch (Exception e) {
                }
            }
            
            reader.close();
            
            System.out.println("Successfully loaded employee data");
            
        } catch (Exception e) {
            System.out.println("ERROR: Cannot find or read employee CSV file");
            System.out.println("Make sure 'MotorPH_Employee Data.csv' is in the Resources folder");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    static void loadAttendanceData() {
        System.out.println("Loading attendance data from CSV...");
        
        String attFile = "Resources/Attendance Record.csv";
        
        attendanceCount = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(attFile));
            reader.readLine();
            
            String line;
            
            while ((line = reader.readLine()) != null && attendanceCount < MAX_ATTENDANCE) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = parseCSVLine(line);
                
                if (data.length < 6) continue;
                
                try {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }
                    
                    String empIdStr = data[0];
                    if (empIdStr.contains(".")) {
                        empIdStr = empIdStr.substring(0, empIdStr.indexOf("."));
                    }
                    attEmpIds[attendanceCount] = Integer.parseInt(empIdStr);
                    
                    String dateStr = data[3];
                    
                    if (dateStr.contains("/")) {
                        String[] dateParts = dateStr.split("/");
                        if (dateParts.length == 3) {
                            attDates[attendanceCount] = dateParts[2] + "-" + dateParts[0] + "-" + dateParts[1];
                        } else {
                            attDates[attendanceCount] = dateStr;
                        }
                    } else {
                        attDates[attendanceCount] = dateStr;
                    }
                    
                    String loginStr = data[4];
                    
                    if (!loginStr.contains(":")) {
                        loginStr = loginStr + ":00";
                    }
                    attLogins[attendanceCount] = loginStr;
                    
                    String logoutStr = data[5];
                    
                    if (!logoutStr.contains(":")) {
                        logoutStr = logoutStr + ":00";
                    }
                    attLogouts[attendanceCount] = logoutStr;
                    
                    attendanceCount++;
                    
                } catch (Exception e) {
                }
            }
            
            reader.close();
            
            System.out.println("Successfully loaded attendance record data");
            
        } catch (Exception e) {
            System.out.println("ERROR reading attendance CSV: " + e.getMessage());
            System.out.println("Make sure 'Attendance Record.csv' is in the Resources folder");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    static double computeHours(String loginStr, String logoutStr) {
        try {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
            
            LocalTime login = LocalTime.parse(loginStr.trim(), timeFormat);
            LocalTime logout = LocalTime.parse(logoutStr.trim(), timeFormat);
            
            LocalTime workStart = LocalTime.of(8, 0);
            LocalTime graceEnd = LocalTime.of(8, 10);
            LocalTime lunchStart = LocalTime.of(12, 0);
            LocalTime lunchEnd = LocalTime.of(13, 0);
            LocalTime workEnd = LocalTime.of(17, 0);
            
            if (logout.isAfter(workEnd)) {
                logout = workEnd;
            }
            
            LocalTime effectiveLogin = login;
            
            if (!login.isBefore(workStart) && !login.isAfter(graceEnd)) {
                effectiveLogin = workStart;
            }
            
            long minutesWorked = Duration.between(effectiveLogin, logout).toMinutes();
            
            if (minutesWorked < 0) {
                return 0;
            }
            
            if (effectiveLogin.isBefore(lunchEnd) && logout.isAfter(lunchStart)) {
                minutesWorked -= 60;
            }
            
            double hours = minutesWorked / 60.0;
            
            return Math.min(hours, 8.0);
            
        } catch (Exception e) {
            return 0;
        }
    }
    
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
        else return 1125.00;
    }
    
    static double computePhilhealth(double salary) {
        double premium;
        
        if (salary <= 10000) {
            premium = 300;
        } else if (salary >= 60000) {
            premium = 1800;
        } else {
            premium = salary * 0.03;
        }
        
        return premium / 2;
    }
    
    static double computePagibig(double salary) {
        double contribution;
        
        if (salary <= 1500) {
            contribution = salary * 0.01;
        } else {
            contribution = salary * 0.02;
        }
        
        if (contribution > 100) {
            contribution = 100;
        }
        
        return contribution;
    }

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
    
    static int findEmployeeIndex(int id) {
        for (int i = 0; i < empIds.length; i++) {
            if (empIds[i] == id) {
                return i;
            }
        }
        return -1;
    }
    
    static boolean isLate(String login) {
        if (login == null || login.isEmpty()) return false;
        
        try {
            String[] timeParts = login.split(":");
            
            if (timeParts.length < 2) return false;
            
            int loginHour = Integer.parseInt(timeParts[0]);
            int loginMinute = Integer.parseInt(timeParts[1]);
            
            int loginMinutes = loginHour * 60 + loginMinute;
            int graceEnd = 8 * 60 + 10;
            
            return loginMinutes > graceEnd;
            
        } catch (Exception e) {
            return false;
        }
    }
    
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
    
    static int getLastDayOfMonth(int month, int year) {
        if (month == 2) {
            if (year % 4 == 0) {
                if (year % 100 == 0) {
                    if (year % 400 == 0) {
                        return 29;
                    } else {
                        return 28;
                    }
                } else {
                    return 29;
                }
            } else {
                return 28;
            }
        } 
        else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } 
        else {
            return 31;
        }
    }
    
    static boolean isValidLogin(String username, String password) {
        return (username.equals("employee") || username.equals("payroll_staff")) && password.equals("12345");
    }
    
    static void showEmployeeDetails(int index) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                         EMPLOYEE PROFILE");
        System.out.println("=".repeat(70));
        
        System.out.println("Employee ID  : " + empIds[index]);
        System.out.println("Name         : " + 
            (firstNames[index] != null ? firstNames[index] : "") + " " + 
            (lastNames[index] != null ? lastNames[index] : ""));
        System.out.println("Birthday     : " + (birthdays[index] != null ? birthdays[index] : ""));
        System.out.println("Address      : " + (addresses[index] != null ? addresses[index] : ""));
        System.out.println("Phone        : " + (phoneNumbers[index] != null ? phoneNumbers[index] : ""));
        
        System.out.println("-".repeat(70));
        
        System.out.println("SSS #        : " + (sssNumbers[index] != null ? sssNumbers[index] : ""));
        System.out.println("Philhealth # : " + (philhealthNumbers[index] != null ? philhealthNumbers[index] : ""));
        System.out.println("TIN #        : " + (tinNumbers[index] != null ? tinNumbers[index] : ""));
        System.out.println("Pag-IBIG #   : " + (pagibigNumbers[index] != null ? pagibigNumbers[index] : ""));
        
        System.out.println("-".repeat(70));
        
        System.out.println("Position     : " + (positions[index] != null ? positions[index] : ""));
        System.out.println("Status       : " + (empStatus[index] != null ? empStatus[index] : ""));
        System.out.println("Supervisor   : " + (supervisors[index] != null ? supervisors[index] : ""));
        
        System.out.println("=".repeat(70));
    }
    
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
                System.out.print("\nEnter your employee number: ");
                int empId;
                try {
                    empId = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Please enter a number.");
                    scanner.nextLine();
                    continue;
                }
                
                int index = findEmployeeIndex(empId);
                if (index == -1) {
                    System.out.println("\nEmployee number does not exist.");
                } else {
                    showEmployeeDetails(index);
                }
            } else {
                System.out.println("Invalid option. Please choose 1 or 2.");
            }
        }
    }
    
    static Object[][] calculateEmployeePayroll(int empIndex, int month, int year) {
        double hourlyRate = hourlyRates[empIndex];
        double monthlySalary = basicSalaries[empIndex];
        
        double firstCutoffHours = 0;
        int firstCutoffDays = 0;
        
        double secondCutoffHours = 0;
        int secondCutoffDays = 0;
        
        for (int i = 0; i < attendanceCount; i++) {
            if (attEmpIds[i] != empIds[empIndex]) continue;
            
            String date = attDates[i];
            if (date == null) continue;
            
            String[] dateParts = date.split("-");
            if (dateParts.length < 3) continue;
            
            int recordYear = Integer.parseInt(dateParts[0]);
            int recordMonth = Integer.parseInt(dateParts[1]);
            int recordDay = Integer.parseInt(dateParts[2]);
            
            if (recordYear != year || recordMonth != month) continue;
            
            double hours = computeHours(attLogins[i], attLogouts[i]);
            
            if (recordDay <= 15) {
                firstCutoffHours += hours;
                firstCutoffDays++;
            } else {
                secondCutoffHours += hours;
                secondCutoffDays++;
            }
        }
        
        double firstCutoffGross = firstCutoffHours * hourlyRate;
        double secondCutoffGross = secondCutoffHours * hourlyRate;
        
        double totalMonthlyGross = firstCutoffGross + secondCutoffGross;
        
        double sss = 0, philhealth = 0, pagibig = 0, tax = 0;
        
        if (totalMonthlyGross > 0) {
            sss = computeSSS(monthlySalary);
            philhealth = computePhilhealth(monthlySalary);
            pagibig = computePagibig(monthlySalary);
            
            double totalGovDeductions = sss + philhealth + pagibig;
            double taxableIncome = totalMonthlyGross - totalGovDeductions;
            
            if (taxableIncome > 0) {
                tax = computeMonthlyTax(taxableIncome);
            } else {
                tax = 0;
            }
        }
        
        double totalDeductions = sss + philhealth + pagibig + tax;
        
        double secondCutoffNet = secondCutoffGross - totalDeductions;
        
        return new Object[][] {
            {firstCutoffDays, firstCutoffHours, firstCutoffGross},
            {secondCutoffDays, secondCutoffHours, secondCutoffGross, secondCutoffNet,
             sss, philhealth, pagibig, tax, totalDeductions, totalMonthlyGross}
        };
    }
    
    static void displaySingleEmployeePayroll(int empIndex) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         EMPLOYEE PAYROLL SUMMARY");
        System.out.println("=".repeat(80));
        
        System.out.println("Employee #      : " + empIds[empIndex]);
        System.out.println("Employee Name   : " + firstNames[empIndex] + " " + lastNames[empIndex]);
        System.out.println("Birthday        : " + birthdays[empIndex]);
        System.out.println("Hourly Rate     : Php " + String.format("%,.2f", hourlyRates[empIndex]));
        
        boolean hasData = false;
        
        for (int month = 6; month <= 12; month++) {
            int year = 2024;
            
            Object[][] payrollData = calculateEmployeePayroll(empIndex, month, year);
            
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
            
            if (firstCutoffDays == 0 && secondCutoffDays == 0) {
                continue;
            }
            
            hasData = true;
            
            System.out.println("\n" + "-".repeat(80));
            System.out.println("MONTH: " + monthName + " " + year);
            System.out.println("-".repeat(80));
            
            if (firstCutoffDays > 0) {
                System.out.println("Cutoff Date     : " + monthName + " 1 to 15");
                System.out.println("Total Hours Worked : " + String.format("%.2f", firstCutoffHours));
                System.out.println("Gross Salary    : Php " + String.format("%,.2f", firstCutoffGross));
                System.out.println("Net Salary      : Php " + String.format("%,.2f", firstCutoffGross));
            } else {
                System.out.println("Cutoff Date     : " + monthName + " 1 to 15");
                System.out.println("No days recorded for this period.");
            }
            
            System.out.println();
            
            if (secondCutoffDays > 0) {
                System.out.println("Cutoff Date     : " + monthName + " 16 to " + lastDay);
                System.out.println("Total Hours Worked : " + String.format("%.2f", secondCutoffHours));
                System.out.println("Gross Salary    : Php " + String.format("%,.2f", secondCutoffGross));
                System.out.println("Each Deduction:");
                System.out.println("    SSS         : Php " + String.format("%,.2f", sss));
                System.out.println("    PhilHealth  : Php " + String.format("%,.2f", philhealth));
                System.out.println("    Pag-IBIG    : Php " + String.format("%,.2f", pagibig));
                System.out.println("    Tax         : Php " + String.format("%,.2f", tax));
                System.out.println("Total Deductions: Php " + String.format("%,.2f", totalDeductions));
                System.out.println("Net Salary      : Php " + String.format("%,.2f", secondCutoffNet));
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
    
    static void displayAllEmployeesPayroll() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                    PAYROLL SUMMARY - ALL EMPLOYEES");
        System.out.println("=".repeat(100));
        
        for (int i = 0; i < empIds.length; i++) {
            if (empIds[i] == 0) continue;
            
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
                
                if (firstCutoffDays == 0 && secondCutoffDays == 0) {
                    continue;
                }
                
                hasData = true;
                
                System.out.println("\n  MONTH: " + monthName + " " + year);
                
                if (firstCutoffDays > 0) {
                    System.out.println("    First Cutoff (1-15): Hours=" + String.format("%.2f", firstCutoffHours) +
                                     ", Gross=Php " + String.format("%,.2f", firstCutoffGross) +
                                     ", Net=Php " + String.format("%,.2f", firstCutoffGross));
                }
                
                if (secondCutoffDays > 0) {
                    System.out.println("    Second Cutoff (16-" + lastDay + "): Hours=" + String.format("%.2f", secondCutoffHours) +
                                     ", Gross=Php " + String.format("%,.2f", secondCutoffGross));
                    System.out.println("        Deductions: SSS=Php " + String.format("%,.2f", sss) +
                                     ", PhilHealth=Php " + String.format("%,.2f", philhealth) +
                                     ", Pag-IBIG=Php " + String.format("%,.2f", pagibig) +
                                     ", Tax=Php " + String.format("%,.2f", tax));
                    System.out.println("        Total Deductions=Php " + String.format("%,.2f", totalDeductions) +
                                     ", Net=Php " + String.format("%,.2f", secondCutoffNet));
                }
            }
            
            if (!hasData) {
                System.out.println("  No attendance data found for this employee.");
            }
        }
        
        System.out.println("\n" + "=".repeat(100));
    }
    
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
                return;
            } else if (choice == 1) {
                processOneEmployee(scanner);
            } else if (choice == 2) {
                displayAllEmployeesPayroll();
            } else {
                System.out.println("Invalid option. Please choose 1-3.");
            }
        }
    }
    
    static void processOneEmployee(Scanner scanner) {
        System.out.print("\nEnter employee number: ");
        int empId;
        try {
            empId = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Please enter a number.");
            scanner.nextLine();
            return;
        }
        
        int index = findEmployeeIndex(empId);
        if (index == -1) {
            System.out.println("\nEmployee number does not exist.");
            return;
        }
        
        displaySingleEmployeePayroll(index);
    }
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("           MOTORPH GROUP 7 S1101 PAYROLL SYSTEM");
        System.out.println("=".repeat(70));
        
        loadEmployeeData();
        loadAttendanceData();
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                   MOTORPH GROUP 7 S1101 SYSTEM");
        System.out.println("=".repeat(70));
        
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
        
        if (username.equals("employee")) {
            employeeMenu(scanner);
        } else {
            payrollStaffMenu(scanner);
        }
        
        scanner.close();
    }
}