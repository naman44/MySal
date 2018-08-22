package sal.naman.api.com.mysal.DAO.SalaryDao;

/**
 * Created by Naman-PC on 11-07-2018.
 */

public interface SalaryInterface {

    String SALARY_TABLE = "salary";
    String MONTH_COLUMN = "month";
    String NAME_COLUMN = "emp_name";
    String SALARY_COLUMN = "salary";
    String ATTENDANCE_COLUMN = "attendance";
    String ABSENT_COLUMN = "absent";
    String REST_COLUMN = "rest_days";
    String TIME_PENALTY_COLUMN = "time_penalty";
    String ADVANCE_COLUMN = "advance";
    String DISBURSED_SALARY_COLUMN = "disbursed_salary";
    String SALARY_PAID_COLUMN = "salary_paid";
    String PAID_ON_COLUMN = "paid_on";
    String REMARKS_COLUMN = "remarks";

    public String CREATE_SALARY_TABLE = "create table salary" +
            "( " + MONTH_COLUMN + " integer not null, " +
            NAME_COLUMN + " varchar2[20] not null, " +
            SALARY_COLUMN + " real[10], " +
            ATTENDANCE_COLUMN + " integer, " +
            ABSENT_COLUMN + " integer, " +
            REST_COLUMN + " integer, " +
            TIME_PENALTY_COLUMN + " real[5], " +
            ADVANCE_COLUMN + " real[5], " +
            DISBURSED_SALARY_COLUMN + " real[10], " +
            SALARY_PAID_COLUMN + " real[10], " +
            PAID_ON_COLUMN + " datetime, " +
            REMARKS_COLUMN + " varchar2[100], " +
            "primary key (" + MONTH_COLUMN + ", " + NAME_COLUMN + ") )";
}
