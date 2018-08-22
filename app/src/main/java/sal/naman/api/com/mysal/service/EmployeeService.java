package sal.naman.api.com.mysal.service;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceImpl;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.model.Employee;
import sal.naman.api.com.mysal.model.EmployeeAttendance;
import sal.naman.api.com.mysal.model.Salary;

/**
 * Created by Naman-PC on 28-06-2018.
 */

public class EmployeeService {

    private Context mContext;
    public EmployeeService(Context mContext){
        this.mContext = mContext;
    }

    public ArrayList<EmployeeAttendance> getEmployeeList(String date){
        return createList(date);

    }

    public ArrayList<EmployeeAttendance> createList(String date) {
        ArrayList<EmployeeAttendance> listOld = new AttendanceImpl().getAttendance(mContext, date);
        ArrayList<Employee> empList = new EmployeeImpl().getAllEmployees(mContext);
            for(Employee e : empList){
                boolean itemPresent = false;
                for(EmployeeAttendance a : listOld){
                    if(a.getName().equalsIgnoreCase(e.getName())){
                        itemPresent = true;
                    }
                }
                if(date.compareTo(e.getJoiningDate()) < 0 || (e.getEndDate() != null && date.compareTo(e.getEndDate()) > 0)){
                    itemPresent = true;
                }

                if(!itemPresent){
                        EmployeeAttendance attendance = new EmployeeAttendance();
                        attendance.setName(e.getName());
                        attendance.setTimeIn(AppUtil.startingTime);
                        attendance.setTimeOut(AppUtil.endTime);
                        attendance.setAdvance(0);
                        attendance.setPresent(true);
                        attendance.setDate(date);
                        long result = new AttendanceImpl().insertIndividualAttendance(mContext, attendance);
                        if(result < 0){
                            Toast.makeText(mContext, "Error in Entry : " + e.getName() + " on " + attendance.getDate(),
                                    Toast.LENGTH_LONG).show();
                        }
                        listOld.add(attendance);
                }
            }
            return listOld;
        }

    public void markBulkAttendance(Date startDate, Date endDate){
//        Date start = new Date();
//        Date end = new Date();
//        try{
//            start = AppUtil.formatDateFromString(startDate);
//            end = AppUtil.formatDateFromString(endDate);
//        }catch (ParseException pe){
//            pe.printStackTrace();
//        }
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);

        while(calStart.before(calEnd)){
            if(calStart.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                createList(AppUtil.formatDate(calStart.getTime()));
                calStart.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }

    public ArrayList<Salary> calculateSalary(int month, Context context){
        ArrayList<Salary> list = new ArrayList<>();
        return list;
    }

}
