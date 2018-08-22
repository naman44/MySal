package sal.naman.api.com.mysal.DAO.SalaryDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import sal.naman.api.com.mysal.model.Salary;

/**
 * Created by Naman-PC on 12-07-2018.
 */

public class SalaryImpl implements SalaryInterface {

    public ArrayList<Salary> getSalary(Context context, int month){
        SalaryDAO dao = new SalaryDAO(context);
        Cursor cursor = dao.getSalaryList(month);
        return retrieveSalaryFromCursor(cursor);
    }

    public long insertSalary(Context context, Salary s){
        ContentValues values = updateValues(s);
        SalaryDAO dao = new SalaryDAO(context);
        return dao.insertSalary(values);
    }

    private ContentValues updateValues(Salary s){
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, s.getEmployeeName());
        values.put(MONTH_COLUMN, s.getMonth());
        values.put(SALARY_COLUMN, s.getSalary());
        values.put(ATTENDANCE_COLUMN, s.getAttendance());
        values.put(ABSENT_COLUMN, s.getAbsent());
        values.put(REST_COLUMN, s.getRestDays());
        values.put(ADVANCE_COLUMN, s.getAdvance());
        values.put(TIME_PENALTY_COLUMN, s.getTimePenalty());
        values.put(DISBURSED_SALARY_COLUMN, s.getDisbursedSalary());
        values.put(SALARY_PAID_COLUMN, s.getPaidSalary());
        values.put(PAID_ON_COLUMN, s.getPaidOn());
        values.put(REMARKS_COLUMN, s.getRemarks());
        return values;
    }

    private ArrayList<Salary> retrieveSalaryFromCursor(Cursor cursor){
        ArrayList<Salary> list = new ArrayList<>();
        if(cursor != null){
            while (cursor.moveToNext()){
                list.add(getSalaryObjFromCursor(cursor));
            }
        }
        return list;
    }

    private Salary getSalaryObjFromCursor(Cursor cursor){
        Salary s = new Salary();
        s.setEmployeeName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
        s.setMonth(cursor.getInt(cursor.getColumnIndex(MONTH_COLUMN)));
        s.setSalary(cursor.getDouble(cursor.getColumnIndex(SALARY_COLUMN)));
        s.setAttendance(cursor.getInt(cursor.getColumnIndex(ATTENDANCE_COLUMN)));
        s.setAbsent(cursor.getInt(cursor.getColumnIndex(ABSENT_COLUMN)));
        s.setRestDays(cursor.getInt(cursor.getColumnIndex(REST_COLUMN)));
        s.setAdvance(cursor.getDouble(cursor.getColumnIndex(ADVANCE_COLUMN)));
        s.setTimePenalty(cursor.getDouble(cursor.getColumnIndex(TIME_PENALTY_COLUMN)));
        s.setDisbursedSalary(cursor.getDouble(cursor.getColumnIndex(DISBURSED_SALARY_COLUMN)));
        s.setPaidSalary(cursor.getDouble(cursor.getColumnIndex(SALARY_PAID_COLUMN)));
        s.setPaidOn(cursor.getString(cursor.getColumnIndex(PAID_ON_COLUMN)));
        s.setRemarks(cursor.getString(cursor.getColumnIndex(REMARKS_COLUMN)));
        return s;
    }

    public int[] getSalaryMonths(Context context){
        int[] intArr = {};
        SalaryDAO dao = new SalaryDAO(context);
        Cursor cursor = dao.getSalaryPaid();
        if(cursor != null && cursor.getCount() > 0){
            intArr = new int[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()){
                if(i > cursor.getCount()){
                    break;
                }
                intArr[i] = cursor.getInt(cursor.getColumnIndex(MONTH_COLUMN));
                i++;
            }
        }
        return intArr;
    }

    public String getSalaryMonthsIfPaid(Context context){
        SalaryDAO dao = new SalaryDAO(context);
        Cursor cursor = dao.getSalaryMonthIfPaid();
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            int month = cursor.getInt(cursor.getColumnIndex(MONTH_COLUMN));
            return month+"";
        }
        return "";
    }

    public Salary getEmployeeSalary(Context context, String name, int month){
        SalaryDAO dao = new SalaryDAO(context);
        Cursor cursor = dao.getEmployeeSalary(name, month);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return getSalaryObjFromCursor(cursor);
        }
        return null;
    }

    public int updateSalary(Context context, Salary s){
        ContentValues values = updateValues(s);
        SalaryDAO dao = new SalaryDAO(context);
        return dao.updateSalary(values, (String)values.get(NAME_COLUMN), (int)values.get(MONTH_COLUMN));
    }

    public int updateSalary(Context context, String name, double amount, int month){
        ContentValues values = new ContentValues();
        values.put(SALARY_PAID_COLUMN, amount);
        SalaryDAO dao = new SalaryDAO(context);
        return dao.updateSalary(values, name, month);
    }

    public int updateSalary(Context context, String name, String date, int month){
        ContentValues values = new ContentValues();
        values.put(PAID_ON_COLUMN, date);
        SalaryDAO dao = new SalaryDAO(context);
        return dao.updateSalary(values, name, month);
    }

    public Salary getSalaryDetailsForAdvance(Context context, String name, int month){
        SalaryDAO dao = new SalaryDAO(context);
        Cursor cursor = dao.getEmployeeSalary(name, month);
        Salary sal = null;
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            sal =  getSalaryObjFromCursor(cursor);
        }
        return sal;
    }
}
