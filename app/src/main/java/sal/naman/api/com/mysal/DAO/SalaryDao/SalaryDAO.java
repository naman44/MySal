package sal.naman.api.com.mysal.DAO.SalaryDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import sal.naman.api.com.mysal.DAO.DaoUtil;
import sal.naman.api.com.mysal.DAO.DatabaseAdapter;

/**
 * Created by Naman-PC on 11-07-2018.
 */

public class SalaryDAO implements SalaryInterface {

    DatabaseAdapter db;
    public SalaryDAO(Context context){
        db = DatabaseAdapter.getInstance(context);
    }

    Cursor getSalaryList(int month){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select * from salary where month = ?", new String[]{month + ""});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    Cursor getSalaryPaid(){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select month from salary where paid_on is null group by month", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    Cursor getSalaryMonthIfPaid(){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select month from salary where paid_on is not null group by month order by month DESC", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    long insertSalary(ContentValues values){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = -1;
        try{
            result = dql.insert(SALARY_TABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    int updateSalary(ContentValues values, String name, int month){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        int result = 0;
        try{
            result = dql.update(SALARY_TABLE, values, NAME_COLUMN + "=? and " + MONTH_COLUMN + "=?", new String[]{name, month+""});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    Cursor getEmployeeSalary(String name, int month){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select * from salary where emp_name= ? and month = ?", new String[]{name, month+""});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public long getMaxMonthNotPaid(){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        long result = -1;
        try{
            result = DatabaseUtils.longForQuery(dql, "select min(month) from salary where paid_on is null", null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
