package sal.naman.api.com.mysal.DAO.HolidaysDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import sal.naman.api.com.mysal.model.Holidays;

/**
 * Created by Naman-PC on 07-07-2018.
 */

public class HolidaysImpl implements HolidayInterface {

    public void insertHoliday(Context context, Holidays holiday){
        HolidaysDAO dao = new HolidaysDAO(context);
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTH, holiday.getMonth());
        values.put(COLUMN_DATE, holiday.getDate());
        values.put(COLUMN_TYPE, holiday.getType());
        values.put(COLUMN_REMARKS, holiday.getRemarks());
        dao.insertHolidays(values);
    }

    public ArrayList<Holidays> getHolidayList(Context context, int month){
        ArrayList<Holidays> list = new ArrayList<>();
        HolidaysDAO dao = new HolidaysDAO(context);
        Cursor cursor = dao.getHolidayListByMonth(month);
        if(cursor != null){
            while (cursor.moveToNext()){
                Holidays holiday = new Holidays();
                holiday.setMonth(cursor.getInt(cursor.getColumnIndex(COLUMN_MONTH)));
                holiday.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                holiday.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                holiday.setRemarks(cursor.getString(cursor.getColumnIndex(COLUMN_REMARKS)));
                list.add(holiday);
            }
            cursor.close();
        }
        dao.closeConnection();
        return list;
    }

    public String getHolidayType(Context context, String date){
        String holidayType = "";
        HolidaysDAO dao = new HolidaysDAO(context);
        Cursor cursor = dao.getHolidayByDate(date);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                holidayType = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            }
        }
        return holidayType;
    }
}
