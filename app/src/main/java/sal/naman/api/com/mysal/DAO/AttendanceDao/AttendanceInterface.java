package sal.naman.api.com.mysal.DAO.AttendanceDao;

/**
 * Created by Naman-PC on 18-06-2018.
 */

public interface AttendanceInterface {

    String TABLE_ATTENDANCE = "attendance_table";
    String DATE_COLUMN = "date";
    String PRESENT_COLUMN = "emp_present";
    String NAME_COLUMN = "employee_name";
    String TIME_IN = "time_in";
    String TIME_OUT = "time_out";
    String ADVANCE_COLUMN = "employee_advance";
    String[] columns = {NAME_COLUMN, DATE_COLUMN, PRESENT_COLUMN, TIME_IN, TIME_OUT, ADVANCE_COLUMN};

    String CREATE_ATTENDANCE_TABLE = "create table " + TABLE_ATTENDANCE +
            "(" + DATE_COLUMN + " datetime not null, " +
            NAME_COLUMN + " varchar2[50] not null, " +
            PRESENT_COLUMN + " integer, " +
            TIME_IN + " datetime not null, " +
            TIME_OUT + " datetime not null, " +
            ADVANCE_COLUMN + " real[5], " +
            "primary key ( " + DATE_COLUMN + ","  + NAME_COLUMN  + ") " +
            ")";
}
