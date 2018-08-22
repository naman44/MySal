package sal.naman.api.com.mysal.screens;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;
import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.Employee;

public class AddEmployeeScreen extends AppCompatActivity {

    TextView joinDate;
    TextInputEditText name, salary, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        joinDate = findViewById(R.id.new_joining_date);
        name = findViewById(R.id.new_emp_name);
        address = findViewById(R.id.new_emp_address);
        salary = findViewById(R.id.new_emp_salary);
        findViewById(R.id.fab).setOnClickListener((View view) ->{
            if(name.getText().toString().isEmpty()){
                name.setError("Please Enter Name");
            }
            if(salary.getText().toString().isEmpty()){
                salary.setError("Enter Salary");
            }
            else{
                Employee emp = new Employee();
                emp.setName(name.getText().toString());
                emp.setSalary(Double.parseDouble(salary.getText().toString()));
                emp.setAddress(address.getText().toString());
                emp.setJoiningDate(joinDate.getText().toString());
                emp.setEndDate("N/A");
                new EmployeeImpl().addEmployeeToDb(this, emp);
                finish();
            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();
        joinDate.setText(AppUtil.formatDate(Calendar.getInstance().getTime()));
        joinDate.setOnClickListener((View view) ->{
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (DatePicker datePicker, int i, int i1, int i2) -> {
                Calendar cal = Calendar.getInstance();
                cal.set(i, i1, i2);
                joinDate.setText(AppUtil.formatDate(cal.getTime()));
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

    }
}
