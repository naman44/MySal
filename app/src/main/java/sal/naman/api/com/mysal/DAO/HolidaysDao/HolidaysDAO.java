package sal.naman.api.com.mysal.DAO.HolidaysDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import sal.naman.api.com.mysal.DAO.DaoUtil;
import sal.naman.api.com.mysal.DAO.DatabaseAdapter;
import sal.naman.api.com.mysal.DAO.SalaryDao.SalaryInterface;

/**
 * Created by Naman-PC on 07-07-2018.
 */

public class HolidaysDAO implements HolidayInterface {

    DatabaseAdapter db;
    public HolidaysDAO(Context context){
        db = DatabaseAdapter.getInstance(context);
    }

    public void createTable(){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        Cursor cursor = dql.rawQuery("select count(*) from sqlite_master where type='table' and name='holidays_table'", null);
        int count = 0;
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        dql.execSQL(SalaryInterface.CREATE_SALARY_TABLE);
        if(count == 0){
            dql.execSQL(CREATE_TABLE_HOLIDAYS);

        }
        db.closeConnection();
    }

    long insertHolidays(ContentValues values){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = -1;
        try{
            result = dql.insert(TABLE_HOLIDAYS, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }
        db.closeConnection();
        return result;
    }

    public long getHolidaysCountByMonth(int month){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        long result = -1;
        try{
            result = DatabaseUtils.longForQuery(dql, "select count(_id) from holidays_table where month = ? and holiday_type is not 'SUNDAY'",
                    new String[]{month+""});
            //dql.rawQuery("select count(_id) from holidays_table where month = ?", new String[]{month+""});
        }catch (Exception e){
            e.printStackTrace();
        }
        db.closeConnection();
        return result;
    }

    Cursor getHolidayListByMonth(int month){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.rawQuery("select * from holidays_table where month=? and holiday_type is not 'SUNDAY' order by date",
                    new String[]{month + ""});
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    long returnSundays(int month){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        long result = 0;
        try {
            result = DatabaseUtils.longForQuery(dql, "select count(_id) from holidays_table where month =? and holiday_type=?",
                    new String[]{month + "", "SUNDAY"});
        } catch (Exception e){
            e.printStackTrace();
        }
        db.closeConnection();
        return result;
    }

    public long deleteHoliday(String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.WRITE_MODE);
        long result = 0;
        try{
            result = dql.delete(TABLE_HOLIDAYS, COLUMN_DATE + "=?", new String[]{date});
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    Cursor getHolidayByDate(String date){
        SQLiteDatabase dql = db.openConnection(DaoUtil.READ_MODE);
        Cursor cursor = null;
        try{
            cursor = dql.query(TABLE_HOLIDAYS, new String[]{COLUMN_TYPE}, COLUMN_DATE + "=?",
                    new String[]{date}, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public void closeConnection(){
        db.closeConnection();
    }
}
