package sal.naman.api.com.mysal.DAO.EmployeeDao;

/**
 * Created by Naman-PC on 18-06-2018.
 */

public interface EmployeeInterface {

    String TABLE_EMPLOYEE = "employee_table";
    String ID_COLUMN = "_id";
    String NAME_COLUMN = "employee_name";
    String ADDRESS_COLUMN = "employee_address";
    String JOINING_DATE = "joining_date";
    String END_DATE = "end_date";
    String SALARY_COLUMN = "employee_salary";
    String[] columnString = {ID_COLUMN, NAME_COLUMN, ADDRESS_COLUMN, JOINING_DATE, END_DATE, SALARY_COLUMN};

    String CREATE_EMPLOYEE_TABLE = "create table " + TABLE_EMPLOYEE +
            "(" + ID_COLUMN + " integer NOT NULL primary key, " +
            NAME_COLUMN + " varchar2[50] not null, " +
            ADDRESS_COLUMN + " varchar2[100], " +
            JOINING_DATE + " datetime, " +
            END_DATE + " datetime, " +
            SALARY_COLUMN + " real[10])";
}
