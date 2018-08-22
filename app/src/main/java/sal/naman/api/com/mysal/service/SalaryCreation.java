package sal.naman.api.com.mysal.service;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceDao;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceImpl;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceInterface;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeInterface;
import sal.naman.api.com.mysal.DAO.HolidaysDao.HolidaysDAO;
import sal.naman.api.com.mysal.DAO.SalaryDao.SalaryImpl;
import sal.naman.api.com.mysal.model.Employee;
import sal.naman.api.com.mysal.model.Holidays;
import sal.naman.api.com.mysal.model.Salary;

/**
 * Created by Naman-PC on 04-07-2018.
 */

public class SalaryCreation {

    private ArrayList<Salary> salaryList;
    private AttendanceDao attendDao;
    private Context mContext;
    private int month;

    public SalaryCreation(Context context, int month){
        this.mContext = context;
        this.month = month;
    }

    public ArrayList<Salary> getSalaryList(){
        if(salaryList == null){
            salaryList = new ArrayList<>();
        }
        initList();
        return salaryList;
    }

    /*
        Initiate Salary List
        1. see if the salary-list is equal to number of employees
        2. if not, create salary for the missing employees - all if no entry in salaryList
     */
    private void initList(){
        String[] str = AppUtil.calculateDate(month);
        EmployeeImpl impl = new EmployeeImpl();
        SalaryImpl salImpl = new SalaryImpl();
        ArrayList<Employee> empList = impl.getEmployeeForSalary(mContext, str[0], str[1]);
        ArrayList<Salary> salList = salImpl.getSalary(mContext, month);

        salaryList.addAll(salList);
        if(empList.size() > salList.size()){
            for (Employee e: empList){
                boolean empPresent = false;
                for(int i=0; i<salList.size(); i++){
                    Salary s = salList.get(i);
                    if(e.getName().equalsIgnoreCase(s.getEmployeeName())){
                        empPresent = true;
                        salList.remove(i);
                    }
                }
                if(!empPresent){
                    Salary s = new Salary();
                    s.setEmployeeName(e.getName());
                    s.setSalary(e.getSalary());
                    s.setMonth(month);
                    salaryList.add(s);
                }
            }
        }
        if (salaryList.size() > 0)
                fillInSalary(str[0], str[1], salaryList);
    }

    /*
        fill salary
        1. iterate over list
        2. if salary is 0, check if its coz of missing attendance or no salary created yet
        3. if no attendance, display same to user
        4. if attendance found, create salary
     */
    public void fillInSalary(String start, String end, ArrayList<Salary> list){
        getAttendanceDao();
        for(Salary s : list){
            int attendance = (int) attendDao.getAttandanceCount(s.getEmployeeName(), start, end, "1");
            if(attendance > 0 && s.getPaidOn() == null){
                s.setAttendance(attendance);
                s.setAbsent((int) attendDao.getAttandanceCount(s.getEmployeeName(), start, end, "0"));
                s.setAdvance(calculateAdvance(s.getEmployeeName(), start));
                s.setTimePenalty(calculateTimePenalty(s.getEmployeeName(), start, end));
                s.setRestDays(calculateRestDays(s.getAttendance(), start, end));
                calculateDisbursedSalary(s);
            }
        }
    }

    private void getAttendanceDao(){
        if(attendDao == null){
            attendDao = new AttendanceDao(mContext);
        }
    }
    /*
        calculate employee advance
        Date - from 1st of month to the current date
     */
    private double calculateAdvance(String name, String firstDate){
        SalaryImpl impl = new SalaryImpl();
        Salary sal = impl.getEmployeeSalary(mContext, name, month-1);
        Cursor cursor = null;
        if(sal != null && (sal.getPaidOn()!=null && !sal.getPaidOn().isEmpty())){
            cursor = attendDao.getAdvance(name, sal.getPaidOn(), AppUtil.formatDate(Calendar.getInstance().getTime()));
        }
        else{
            cursor = attendDao.getAdvance(name, firstDate, AppUtil.formatDate(Calendar.getInstance().getTime()));
        }
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }
        return 0;
    }

    private double calculateTimePenalty(String name, String startDate, String endDate){
        Cursor cursor = attendDao.getEmployeeTime(name, startDate, endDate);
        double penalty = 0;
        if(cursor != null){
            while (cursor.moveToNext()){
                String time_in = cursor.getString(cursor.getColumnIndex(AttendanceInterface.TIME_IN));
                String time_out = cursor.getString(cursor.getColumnIndex(AttendanceInterface.TIME_OUT));
                if(!time_in.equals("-") || !time_out.equals("-")){
                    double time = penaltyTime(time_in, time_out);
                    penalty += time;
                }
            }
        }
        return penalty;
    }

    /*
        calculate rest days
        1. Check for holidays in a month
        2. calculate sundays in month
        3. if 5 sundays in month, allocate extra sunday if employee present in all days but 1.
     */
    private int calculateRestDays(int attendance, String firstDay, String lastDay){
        int restDays;
        HolidaysDAO dao = new HolidaysDAO(mContext);
        int holidaysCount = (int) dao.getHolidaysCountByMonth(month);
        dao.closeConnection();
        ArrayList<Holidays> list = countSundays(firstDay,lastDay);
        restDays = getRestCountSimple(attendance + holidaysCount);
        if(list.size() == 5){
            int proxy = attendance + holidaysCount + restDays;
            if(month != 1){
                if(proxy >= 29){
                    restDays++;
                }
            }
            else{
                if(proxy >=27){
                    restDays++;
                }
            }
        }
        return restDays;
    }

    private int getRestCountSimple(int attendance){
        return attendance/6;
    }

    /*
        method for getting one day up or down in String format
        TO BE USED : if we check for employee sat/sun presence for rest day calculation
        NOT IN USE for now
     */
    private String getDateUpDown(String date, int difference){
        String newDate = date;
        try{
            Date dateP = AppUtil.formatDateFromString(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateP);
            cal.add(Calendar.DAY_OF_MONTH, difference);
            newDate = AppUtil.formatDate(cal.getTime());
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return newDate;
    }

    /*
        count sundays in a month by iterating over the month
     */
    private ArrayList<Holidays> countSundays(String first,String last){
        ArrayList<Holidays> holidayList = new ArrayList<>();
        try{
            Date firstDay = AppUtil.formatDateFromString(first);
            Date lastDay = AppUtil.formatDateFromString(last);
            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            calStart.setTime(firstDay);
            calEnd.setTime(lastDay);
            while(calStart.before(calEnd)){
                if(calStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    Holidays holiday = new Holidays();
                    holiday.setMonth(month);
                    holiday.setDate(AppUtil.formatDate(calStart.getTime()));
                    holiday.setType("SUNDAY");
                    holiday.setRemarks("");
                    holidayList.add(holiday);
                }
                calStart.add(Calendar.DAY_OF_MONTH, 1);
            }
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return holidayList;
    }

    /*
        calculate salary and save to database object by object
        salary = (attendance + rest)* salary - (penalty*hourly rate + advance)
     */
    private void calculateDisbursedSalary(Salary s){
            SalaryImpl impl = new SalaryImpl();
            double diff = getCarryOverAmount(mContext, s.getEmployeeName(), month - 1);
            double dailyRate = s.getSalary()/30;
            double hourlyRate = dailyRate/8;
            int totalDays = s.getAttendance() + s.getRestDays();
            if(s.getAbsent() == 0){
                if(totalDays == 31){
                    totalDays = 30;
                }
                if(month == 1 && totalDays == 29){
                    totalDays = 30;
                }
            }
            double salary = (totalDays)*dailyRate - (s.getTimePenalty()*hourlyRate + s.getAdvance()) + diff;
            s.setDisbursedSalary(Math.round(salary));
            int result = impl.updateSalary(mContext, s);
            if (result == 0){
                long res = impl.insertSalary(mContext, s);
                if(res > 0){
                    Toast.makeText(mContext, "Salary Inserted", Toast.LENGTH_SHORT);
                }
            }else
                Toast.makeText(mContext, "Salary Updated", Toast.LENGTH_SHORT);
    }

    public static double getCarryOverAmount(Context context, String name, int month){
        SalaryImpl impl = new SalaryImpl();
        Salary sal = impl.getEmployeeSalary(context, name, month);
        double diff = 0;
        if(sal != null){
            diff = sal.getDisbursedSalary() - sal.getPaidSalary();
        }
        return diff;
    }

    public void updateSalaryOnChange(Salary s, String value){
        if(value.isEmpty()){
            calculateDisbursedSalary(s);
        }
        else{
            getAttendanceDao();
            String[] str = AppUtil.calculateDate(month);
            s.setAttendance((int)attendDao.getAttandanceCount(s.getEmployeeName(), str[0], str[1], "1"));
            s.setAbsent((int)attendDao.getAttandanceCount(s.getEmployeeName(), str[0], str[1], "0"));
            s.setRestDays(calculateRestDays(s.getAttendance(), str[0], str[1]));
            calculateDisbursedSalary(s);
        }
    }

    /*
        Method to calculate penalty time
                employee is not considered late till 9.15AM or if leaves at 5.15PM.
                after that 0.5 hrs is deducted for every 30mins delay cycle
     */
    private double penaltyTime(String timeIn, String timeOut){
        double penalty = 0;
        try{
            Date in = AppUtil.formatTimeFromString(timeIn);
            Date out = AppUtil.formatTimeFromString(timeOut);
            long value = out.getTime() - in.getTime();
            double timeSpent = (double) value/3600000;
            double diff = 8.25 - timeSpent;
            int differential = (int) Math.floor(diff/0.5) + 1;
            penalty = differential*0.5;
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return penalty;
    }

    /*
        Extra method to calculate attendance for a complete list
        NOT IN USE
     */
    private void getAttendanceForEmployee(String first, String last, String present){
        Cursor cursor = attendDao.getAttendanceForSalary(first, last, present);
        if(cursor != null){
            while (cursor.moveToNext()){
                boolean itemFound = false;
                String name = cursor.getString(cursor.getColumnIndex(AttendanceInterface.NAME_COLUMN));
                String attendance = cursor.getString(1);
                double salary = cursor.getDouble(cursor.getColumnIndex(EmployeeInterface.SALARY_COLUMN));
                for(Salary s : salaryList){
                    if(s.getEmployeeName().equalsIgnoreCase(name)){
                        if(present.equals("0")){
                            s.setAbsent(Integer.parseInt(attendance));
                        }
                        else{
                            s.setAttendance(Integer.parseInt(attendance));
                        }
                        itemFound = true;
                    }
                }
                if(!itemFound){
                    Salary sal = new Salary();
                    sal.setEmployeeName(name);
                    if(present.equals("0")){
                        sal.setAbsent(Integer.parseInt(attendance));
                    }
                    else{
                        sal.setAttendance(Integer.parseInt(attendance));
                    }
                    sal.setAttendance(Integer.parseInt(attendance));
                    sal.setSalary(salary);
                    salaryList.add(sal);
                }
            }
        }
    }

    private int getRestCount(ArrayList<Holidays> list, int attendance, Salary s){
        int restDays = 0;
        for(Holidays h : list){
            String sat = getDateUpDown(h.getDate(), -1);
            String sun = getDateUpDown(h.getDate(), 1);
            AttendanceImpl impl = new AttendanceImpl();
                if(impl.getAttendanceForDay(mContext, s.getEmployeeName(), sat) &&
                        impl.getAttendanceForDay(mContext, s.getEmployeeName(), sun)){
                    restDays++;
                }
        }
        int attendRest = attendance/6;
        if(restDays > attendRest){
            restDays = attendRest;
        }
        return restDays;
    }
}
