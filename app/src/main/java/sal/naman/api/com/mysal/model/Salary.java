package sal.naman.api.com.mysal.model;

/**
 * Created by Naman-PC on 18-06-2018.
 */

public class Salary {

    private String employeeName;
    private int month;
    private double salary;
    private int attendance;
    private int absent;
    private int restDays;
    private double timePenalty;
    private double advance;
    private double disbursedSalary;
    private double paidSalary;
    private String paidOn;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getRestDays() {
        return restDays;
    }

    public void setRestDays(int restDays) {
        this.restDays = restDays;
    }

    public double getPaidSalary() {
        return paidSalary;
    }

    public void setPaidSalary(double paidSalary) {
        this.paidSalary = paidSalary;
    }

    public String getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(String paidOn) {
        this.paidOn = paidOn;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public double getTimePenalty() {
        return timePenalty;
    }

    public void setTimePenalty(double timePenalty) {
        this.timePenalty = timePenalty;
    }

    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }

    public double getDisbursedSalary() {
        return disbursedSalary;
    }

    public void setDisbursedSalary(double disbursedSalary) {
        this.disbursedSalary = disbursedSalary;
    }

    public boolean compareSalaryObject(Salary old, Salary newO){
        if(old.absent == newO.absent &&
           old.attendance == newO.attendance &&
           old.advance == newO.advance &&
           old.disbursedSalary == newO.disbursedSalary &&
           old.month == newO.month &&
           old.salary == newO.salary &&
           old.paidOn.equalsIgnoreCase(newO.paidOn) &&
           old.paidSalary == newO.paidSalary &&
           old.restDays == newO.restDays &&
           old.timePenalty == newO.timePenalty &&
           old.employeeName.equalsIgnoreCase(newO.employeeName)){
            return true;
        }
        return false;
    }
}
