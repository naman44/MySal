package sal.naman.api.com.mysal.DAO.HolidaysDao;

/**
 * Created by Naman-PC on 07-07-2018.
 */

public interface HolidayInterface {

    String TABLE_HOLIDAYS = "holidays_table";
    String COLUMN_ID = "_id";
    String COLUMN_MONTH = "month";
    String COLUMN_DATE = "date";
    String COLUMN_TYPE = "holiday_type";
    String COLUMN_REMARKS = "holiday_remarks";
    String[] columns = {COLUMN_ID, COLUMN_MONTH, COLUMN_DATE, COLUMN_TYPE, COLUMN_REMARKS};

    String CREATE_TABLE_HOLIDAYS = "create table " + TABLE_HOLIDAYS +
            " ( " + COLUMN_ID + " integer primary key, " +
            COLUMN_MONTH + " varchar2[20], " +
            COLUMN_DATE + " varchar2[20] not null unique, " +
            COLUMN_TYPE + " varchar2[30], " +
            COLUMN_REMARKS + " varchar2[100])";
}
