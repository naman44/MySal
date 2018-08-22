package sal.naman.api.com.mysal.DAO.EmployeeDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import sal.naman.api.com.mysal.DAO.DaoUtil;
import sal.naman.api.com.mysal.DAO.DatabaseAdapter;
import sal.naman.api.com.mysal.model.Employee;

/**
 * Created by Naman-PC on 23-06-2018.
 */

public class EmployeeDao implements EmployeeInterface{

    private Context mContext;
    private DatabaseAdapter db;

    public EmployeeDao(Context context){
        this.mContext = context;
        db = DatabaseAdapter.getInstance(context);
    }

    Cursor getEmployees(){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.query(TABLE_EMPLOYEE, columnString,
                    null, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    Cursor getEmployees(String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.query(TABLE_EMPLOYEE, columnString,
                    END_DATE + ">?", new String[]{date}, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    long insertEmployee(ContentValues values){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = -1;
        try{
            result = dql.insert(TABLE_EMPLOYEE, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    Cursor getEmployeeByName(String name){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.query(TABLE_EMPLOYEE, columnString, NAME_COLUMN,
                    new String[]{name}, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    int updateEmployee(ContentValues values){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        int result = -1;
        try{
            result = dql.update(TABLE_EMPLOYEE, values, NAME_COLUMN + " =?", new String[]{values.get(NAME_COLUMN).toString()});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    Cursor getEmployeeListForSalary(String first, String last){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select * from employee_table where joining_date < ? and (end_date > ? or end_date is null)",
                    new String[]{last, first});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public long deleteEmployee(String name){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = -1;
        try{
            result = dql.delete(TABLE_EMPLOYEE, NAME_COLUMN + "=?", new String[]{name});
        }catch (Exception et){
            et.printStackTrace();
        }
        return result;
    }
}
