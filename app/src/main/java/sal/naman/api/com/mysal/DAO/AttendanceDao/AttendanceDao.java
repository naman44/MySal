package sal.naman.api.com.mysal.DAO.AttendanceDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sal.naman.api.com.mysal.DAO.DaoUtil;
import sal.naman.api.com.mysal.DAO.DatabaseAdapter;

/**
 * Created by Naman-PC on 18-06-2018.
 */

public class AttendanceDao implements AttendanceInterface{

    DatabaseAdapter db;
    public AttendanceDao(Context context){
        db = DatabaseAdapter.getInstance(context);
    }

    void insertAttendance(ArrayList<ContentValues> list){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        dql.beginTransaction();
        try{
            for(ContentValues values : list){
                dql.insert(TABLE_ATTENDANCE, null, values);
            }

            dql.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dql.endTransaction();
        }
    }

    Cursor getEmployeeAttendanceByDate(String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.query(TABLE_ATTENDANCE, columns, DATE_COLUMN + "=?", new String[]{date}, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public int isAttendanceMarkedForDay(String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select * from attendance_table where date = ? group by date", new String[]{date});
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cursor != null){
            return cursor.getCount();
        }
        return 0;
    }

    void updateAttendance(ArrayList<ContentValues> list){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        dql.beginTransaction();
        try{
            for(ContentValues values : list){
                dql.insert(TABLE_ATTENDANCE, null, values);
            }

            dql.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dql.endTransaction();
        }
    }

    long updateIndividualAttendance(ContentValues values){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = -1;
        try{
            result = dql.update(TABLE_ATTENDANCE, values, DATE_COLUMN + "=?" + " and " + NAME_COLUMN + "=?",
                    new String[]{values.getAsString(DATE_COLUMN), values.getAsString(NAME_COLUMN)});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    long insertIndividualAttendance(ContentValues values){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = -1;
        try{
            result = dql.insert(TABLE_ATTENDANCE, null, values);
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    Cursor getFirstAttendanceDate(){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.query(TABLE_ATTENDANCE, new String[]{DATE_COLUMN}, null, null,
                    null, null, DATE_COLUMN, "1");
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getAttendanceForSalary(String start_date, String end_date, String present){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor= null;
        try{
            cursor = dql.rawQuery("select a.employee_name, count(a.emp_present), e.employee_salary from attendance_table a join employee_table e" +
                    " on a.employee_name = e.employee_name where a.emp_present =? and a.date BETWEEN ? and ? group by a.employee_name", new String[]{present,start_date,end_date});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public long getAttandanceCount(String name, String start, String end, String present){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        long result = 0;
        try{
            result = DatabaseUtils.longForQuery(dql, "select count(emp_present) from attendance_table where employee_name = ?" +
                    " and emp_present = ? and date between ? and ? group by employee_name", new String[]{name, present, start, end});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int deleteAttendance(String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        int result = -1;
        try{
            result = dql.delete(TABLE_ATTENDANCE, DATE_COLUMN + " =?", new String[]{date});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public Cursor getAdvance(String name, String startDate, String endDate){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select sum(employee_advance) from attendance_table " +
                    "where employee_name = ? and date between ? and ?", new String[]{name, startDate, endDate});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getEmployeeTime(String name, String startDate, String endDate){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select time_in, time_out from attendance_table where employee_name = ? and emp_present = 1 and date between ? and ?",
                    new String[]{name, startDate, endDate});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    Cursor getAttendanceForDay(String name, String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select emp_present from attendance_table where employee_name=? and date=?", new String[]{name, date});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    Cursor getEmployeeAttendanceForMonth(String name, String start, String end){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select * from attendance_table where employee_name=? and date between ? and ?",
                    new String[]{name, start,end});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

}
