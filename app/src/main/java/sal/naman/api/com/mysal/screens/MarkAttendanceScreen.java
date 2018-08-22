package sal.naman.api.com.mysal.screens;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.ArrayList;
import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceDao;
import sal.naman.api.com.mysal.DAO.HolidaysDao.HolidaysDAO;
import sal.naman.api.com.mysal.DAO.HolidaysDao.HolidaysImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.adapter.RecyclerMarkAttendanceAdapter;
import sal.naman.api.com.mysal.model.EmployeeAttendance;
import sal.naman.api.com.mysal.model.Holidays;
import sal.naman.api.com.mysal.service.EmployeeService;

public class MarkAttendanceScreen extends AppCompatActivity {

    RecyclerView rv;
    TextView date, holidayText;
    RecyclerMarkAttendanceAdapter adapter;
    ImageButton deleteAtt;
    ArrayList<EmployeeAttendance> listAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance_screen);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date = findViewById(R.id.mark_page_date);
        rv = findViewById(R.id.recycler_lone);
        deleteAtt = findViewById(R.id.delete_attendance);
        holidayText = findViewById(R.id.mark_holiday_text);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layout);

        listAttendance = new ArrayList<>();
        adapter = new RecyclerMarkAttendanceAdapter(this, listAttendance);

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce_interpolator);
        rv.setAnimation(anim);
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar cal = Calendar.getInstance();
        date.setText(AppUtil.formatDate(cal.getTime()));
        fillAdapter();
    }

    private void fillAdapter(){
        HolidaysImpl impl = new HolidaysImpl();
        String type = impl.getHolidayType(this, date.getText().toString());

        if(type.isEmpty()){
            holidayText.setText("Mark Attendance");
            listAttendance = new EmployeeService(this).getEmployeeList(date.getText().toString());
            adapter.updateList(listAttendance);
            //adapter = new RecyclerMarkAttendanceAdapter(this, list);
            rv.setAdapter(adapter);
        }
        else{
            listAttendance.clear();
            //adapter = new RecyclerMarkAttendanceAdapter(this, list);
            adapter.updateList(listAttendance);
            holidayText.setText(type);
        }
    }

    private void setListeners(){
        Calendar cal = Calendar.getInstance();
        date.setOnClickListener((View v) ->{
            DatePickerDialog dialog = new DatePickerDialog(this, (DatePicker datePicker, int year, int month, int day) ->{
                cal.set(year, month, day);
                date.setText(AppUtil.formatDate(cal.getTime()));
                fillAdapter();
            }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        deleteAtt.setOnClickListener((View v) ->{
            deleteAttendance();
            finish();
        });
        holidayText.setOnClickListener((View v)->{
            // show dialog to fill type and remark
            if(holidayText.getText().toString().equalsIgnoreCase("Mark Holiday")){
                Holidays holiday = new Holidays();
                try{
                    cal.setTime(AppUtil.formatDateFromString(date.getText().toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                holiday.setMonth(cal.get(Calendar.MONTH));
                holiday.setDate(AppUtil.formatDate(cal.getTime()));

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Paid Amount");
                EditText text = new EditText(this);
                text.setHint("Enter Holiday Type");
                dialog.setView(text);
                dialog.setPositiveButton("Save", (DialogInterface dialogInterface, int i)-> {
                    holiday.setType(text.getText().toString());
                    HolidaysImpl impl = new HolidaysImpl();
                    impl.insertHoliday(this, holiday);
                    holidayText.setText(holiday.getType());
                    deleteAttendance();
                    dialogInterface.dismiss();
                });
                dialog.show();
            }
            else{
                HolidaysDAO dao = new HolidaysDAO(this);
                dao.deleteHoliday(date.getText().toString());
                fillAdapter();
                holidayText.setText("Mark Holiday");
            }
        });
    }

    private void deleteAttendance(){
        AttendanceDao dao = new AttendanceDao(this);
        int result = dao.deleteAttendance(date.getText().toString());
        if(result > 0){
            Toast.makeText(this, R.string.attendance_delete, Toast.LENGTH_SHORT).show();
        }
    }
}
