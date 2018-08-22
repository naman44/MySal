package sal.naman.api.com.mysal.DAO.AttendanceDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import sal.naman.api.com.mysal.model.EmployeeAttendance;

/**
 * Created by Naman-PC on 26-06-2018.
 */

public class AttendanceImpl implements AttendanceInterface {

    ContentValues updateValuesFromObj(EmployeeAttendance attend){
        ContentValues values = new ContentValues();
        values.put(DATE_COLUMN, attend.getDate());
        values.put(PRESENT_COLUMN, attend.isPresent() ? 1 : 0);
        values.put(NAME_COLUMN, attend.getName());
        values.put(TIME_IN, attend.getTimeIn());
        values.put(TIME_OUT, attend.getTimeOut());
        values.put(ADVANCE_COLUMN, attend.getAdvance());
        return values;
    }

    public void  insertAttendance(Context context, ArrayList<EmployeeAttendance> list){
        AttendanceDao dao = new AttendanceDao(context);
        ArrayList<ContentValues> listValues = new ArrayList<>(list.size());
        for(EmployeeAttendance attend : list){
            listValues.add(updateValuesFromObj(attend));
        }
        dao.insertAttendance(listValues);
    }

    public ArrayList<EmployeeAttendance> getAttendance(Context context, String date){
        AttendanceDao dao = new AttendanceDao(context);
        Cursor cursor = dao.getEmployeeAttendanceByDate(date);
        ArrayList<EmployeeAttendance> attendance = new ArrayList<>();
        if(cursor != null){
            while(cursor.moveToNext()){
                EmployeeAttendance attend = new EmployeeAttendance();
                attend.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                attend.setTimeOut(cursor.getString(cursor.getColumnIndex(TIME_OUT)));
                attend.setPresent((cursor.getInt(cursor.getColumnIndex(PRESENT_COLUMN))) == 1);
                attend.setTimeIn(cursor.getString(cursor.getColumnIndex(TIME_IN)));
                attend.setAdvance(cursor.getDouble(cursor.getColumnIndex(ADVANCE_COLUMN)));
                attend.setDate(cursor.getString(cursor.getColumnIndex(DATE_COLUMN)));
                attendance.add(attend);
            }
        }
        return attendance;
    }

    // incomplete method
    public void updateAttendance(Context context, ArrayList<EmployeeAttendance> list){
        AttendanceDao dao = new AttendanceDao(context);
        ArrayList<ContentValues> listValues = new ArrayList<>(list.size());

    }

    public long saveIndividualAttendance(Context context, EmployeeAttendance attendance){
        AttendanceDao dao = new AttendanceDao(context);
        return dao.updateIndividualAttendance(updateValuesFromObj(attendance));
    }

    public long insertIndividualAttendance(Context context, EmployeeAttendance attendance){
        AttendanceDao dao = new AttendanceDao(context);
        return dao.insertIndividualAttendance(updateValuesFromObj(attendance));
    }

    public boolean getAttendanceForDay(Context context, String name, String date){
        AttendanceDao dao = new AttendanceDao(context);
        Cursor cursor = dao.getAttendanceForDay(name,date);
        boolean present = false;
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            int attend = cursor.getInt(cursor.getColumnIndex(PRESENT_COLUMN));
            present = attend == 1;
        }
        return present;
    }

    public ArrayList<EmployeeAttendance> getAttendanceForMonth(Context context, String name, String start, String end){
        ArrayList<EmployeeAttendance> list = new ArrayList<>();
        AttendanceDao dao = new AttendanceDao(context);
        Cursor cursor = dao.getEmployeeAttendanceForMonth(name, start, end);
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                EmployeeAttendance att = new EmployeeAttendance();
                att.setName(name);
                att.setDate(cursor.getString(cursor.getColumnIndex(DATE_COLUMN)));
                att.setTimeIn(cursor.getString(cursor.getColumnIndex(TIME_IN)));
                att.setTimeOut(cursor.getString(cursor.getColumnIndex(TIME_OUT)));
                att.setAdvance(cursor.getDouble(cursor.getColumnIndex(ADVANCE_COLUMN)));
                att.setPresent(cursor.getInt(cursor.getColumnIndex(PRESENT_COLUMN)) == 1);
                list.add(att);
            }
        }
        return list;
    }

    public String getFirstAttendanceDate(Context context){
        AttendanceDao dao = new AttendanceDao(context);
        Cursor cursor = dao.getFirstAttendanceDate();
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(DATE_COLUMN));
        }
        return "";
    }
}
