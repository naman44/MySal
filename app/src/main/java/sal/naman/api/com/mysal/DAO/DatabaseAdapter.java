package sal.naman.api.com.mysal.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceInterface;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeInterface;
import sal.naman.api.com.mysal.DAO.HolidaysDao.HolidayInterface;
import sal.naman.api.com.mysal.DAO.SalaryDao.SalaryInterface;

/**
 * Created by Naman-PC on 01-09-2017.
 */

public class DatabaseAdapter {


    private SQLiteDatabase db;
    private MyHelper helper;
    private Context context;

    private static DatabaseAdapter instance = null;

    private DatabaseAdapter(Context context){
        helper = new MyHelper(context, DaoUtil.DB_NAME, null, 1);
        this.context = context;
    }

    //get db instance
    public static DatabaseAdapter getInstance(Context context){

        if(instance == null){
            synchronized (DatabaseAdapter.class){
                instance = new DatabaseAdapter(context);
            }
        }
        return instance;
    }

    // to open db connection
    public SQLiteDatabase openConnection(int mode)
    {
        if(mode==DaoUtil.WRITE_MODE){
            db=helper.getWritableDatabase();
        }
        else if(mode==DaoUtil.READ_MODE){
            db= helper.getReadableDatabase();
        }
        return db;
    }

    // close db connection
    public void closeConnection(){
        db.close();
        helper.close();
    }

    public void deleteAllData(){
        SQLiteDatabase db = openConnection(DaoUtil.WRITE_MODE);
        try{
            db.delete(AttendanceInterface.TABLE_ATTENDANCE, null, null);
            db.delete(SalaryInterface.SALARY_TABLE, null, null);
            db.delete(HolidayInterface.TABLE_HOLIDAYS, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createBasicTables(SQLiteDatabase db){

        try{
            db.execSQL(AttendanceInterface.CREATE_ATTENDANCE_TABLE);
            db.execSQL(EmployeeInterface.CREATE_EMPLOYEE_TABLE);
            db.execSQL(HolidayInterface.CREATE_TABLE_HOLIDAYS);
            db.execSQL(SalaryInterface.CREATE_SALARY_TABLE);
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, DaoUtil.DB_EXCEPTION_CREATE_BASIC_TABLE, Toast.LENGTH_LONG).show();
        }
    }

    /*
	 * DB helper class - inner class
	 */
    public class MyHelper extends SQLiteOpenHelper{

        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
                super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createBasicTables(db);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }
    }
}
