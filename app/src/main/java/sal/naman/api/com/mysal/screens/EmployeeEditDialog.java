package sal.naman.api.com.mysal.screens;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.Employee;

/**
 * Created by Naman-PC on 03-07-2018.
 * Dialog class to edit employee on home screen
 */

public class EmployeeEditDialog extends Dialog implements View.OnClickListener{

    private EditText name, address, salary;
    private TextView joiningDate, endDate;
    private Context mContext;

    public EmployeeEditDialog(Context context){
        super(context);
        this.mContext = context;
        initDialog();
    }

    @Override
    public void show() {
        super.show();

    }

    private void initDialog(){
        setContentView(R.layout.content_add_employee_screen);
        if(getWindow() != null)
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title = findViewById(R.id.add_edot_employee_text);
        title.setText(R.string.title_edit_emp);
        name = findViewById(R.id.new_emp_name);
        address = findViewById(R.id.new_emp_address);
        salary = findViewById(R.id.new_emp_salary);
        joiningDate = findViewById(R.id.new_joining_date);
        endDate = findViewById(R.id.emp_end_date);
        Button save = findViewById(R.id.save_dialog_data);
        save.setVisibility(View.VISIBLE);

        joiningDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        save.setOnClickListener((View v) -> {
            Employee emp = new Employee();
            emp.setName(name.getText().toString());
            emp.setAddress(address.getText().toString());
            emp.setSalary(AppUtil.getAmountSansSymbol(mContext, salary.getText().toString()));
            emp.setJoiningDate(joiningDate.getText().toString());
            emp.setEndDate(endDate.getText().toString());
            EmployeeImpl impl = new EmployeeImpl();
            impl.updateEmployee(getContext(), emp);
            dismiss();
        });
    }

    @Override
    public void onClick(View view) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), (datePicker, year, month, date) -> {
            Calendar calLocal = Calendar.getInstance();
            calLocal.set(year, month, date);
            ((TextView)view).setText(AppUtil.formatDate(calLocal.getTime()));
        }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void fillValuesToDialog(Employee emp){
        name.setText(emp.getName());
        address.setText(emp.getAddress());
        salary.setText(AppUtil.getAmountWithSymbol(mContext, emp.getSalary()));
        joiningDate.setText(emp.getJoiningDate());
        endDate.setText(emp.getEndDate());
        if(endDate.getText().toString().isEmpty()){
            endDate.setText("N/A");
        }
    }

}
