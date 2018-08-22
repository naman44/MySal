package sal.naman.api.com.mysal.DAO.EmployeeDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.model.Employee;

/**
 * Created by Naman-PC on 23-06-2018.
 */

public class EmployeeImpl implements EmployeeInterface {


    public ArrayList<Employee> retrieveEmployeesFromCursor(Cursor cursor){

        ArrayList<Employee> empList = new ArrayList<>();
        if(cursor != null){
            while(cursor.moveToNext()){
                Employee emp = new Employee();
                emp.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                emp.setId(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)));
                emp.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN)));
                emp.setJoiningDate(cursor.getString(cursor.getColumnIndex(JOINING_DATE)));
                emp.setEndDate(cursor.getString(cursor.getColumnIndex(END_DATE)));
                emp.setSalary(cursor.getDouble(cursor.getColumnIndex(SALARY_COLUMN)));
                empList.add(emp);
            }
        }
        return empList;
    }

    public ArrayList<Employee> getAllEmployees(Context context){
        EmployeeDao dao = new EmployeeDao(context);
        Cursor cursor = dao.getEmployees();
        return retrieveEmployeesFromCursor(cursor);
    }

    public ArrayList<Employee> getCurrentEmployees(Context context){
        EmployeeDao dao = new EmployeeDao(context);
        Cursor cursor =  dao.getEmployees(AppUtil.formatDate(Calendar.getInstance().getTime()));
        return retrieveEmployeesFromCursor(cursor);
    }

    public ArrayList<Employee> getEmployeeForSalary(Context context, String firstDate, String lastDate){
        EmployeeDao dao = new EmployeeDao(context);
        Cursor cursor = dao.getEmployeeListForSalary(firstDate, lastDate);
        ArrayList<Employee> list = new ArrayList<>();
        if(cursor != null){
            while (cursor.moveToNext()){
                Employee emp = new Employee();
                emp.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                emp.setSalary(cursor.getDouble(cursor.getColumnIndex(SALARY_COLUMN)));
                list.add(emp);
            }
        }
        return list;
    }

    public void addEmployeeToDb(Context context, Employee emp){
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, emp.getName());
        values.put(ADDRESS_COLUMN, emp.getAddress());
        values.put(JOINING_DATE, emp.getJoiningDate());
        values.put(END_DATE, emp.getEndDate());
        values.put(SALARY_COLUMN, emp.getSalary());
        EmployeeDao dao = new EmployeeDao(context);
        long result = dao.insertEmployee(values);
        if(result > 0){
            Toast.makeText(context, "Employee saved", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Employee> getEmployeeByName(Context context, String name){
        EmployeeDao dao = new EmployeeDao(context);
        Cursor cursor = dao.getEmployeeByName(name);
        return retrieveEmployeesFromCursor(cursor);
    }

    public void updateEmployee(Context context, Employee emp){
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, emp.getName());
        values.put(ADDRESS_COLUMN, emp.getAddress());
        values.put(JOINING_DATE, emp.getJoiningDate());
        values.put(END_DATE, emp.getEndDate());
        values.put(SALARY_COLUMN, emp.getSalary());
        EmployeeDao dao = new EmployeeDao(context);
        int result = dao.updateEmployee(values);
        if(result > 0){
            Toast.makeText(context, "Employee saved", Toast.LENGTH_LONG).show();
        }
    }
}
