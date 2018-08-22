package sal.naman.api.com.mysal.screens;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceImpl;
import sal.naman.api.com.mysal.DAO.SalaryDao.SalaryDAO;
import sal.naman.api.com.mysal.DAO.SalaryDao.SalaryImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.adapter.RecyclerSalaryAdapter;
import sal.naman.api.com.mysal.model.Salary;
import sal.naman.api.com.mysal.service.SalaryCreation;

public class SalaryPage extends AppCompatActivity {

    TextView monthView;
    RecyclerView rv;
    CoordinatorLayout coord;
    RecyclerSalaryAdapter adapter;
    ArrayList<Salary> salaryListToDisplay;
    int month;
    ImageButton nextBtn, prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_page);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        monthView = findViewById(R.id.monthView);
        rv = findViewById(R.id.recycler_lone);
        coord = findViewById(R.id.coordinator_salary);
        nextBtn = findViewById(R.id.btn_next_salary);
        prevBtn = findViewById(R.id.btn_prev_salary);

        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutRv);

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce_interpolator);
        rv.setAnimation(anim);

        nextBtn.setOnClickListener((View v)->{
            month = month + 1;
            SalaryImpl impl = new SalaryImpl();
            salaryListToDisplay = impl.getSalary(this, month);
            if(salaryListToDisplay.size() == 0){
                SalaryCreation create = new SalaryCreation(this, month);
                salaryListToDisplay = create.getSalaryList();
            }
            adapter.updateList(salaryListToDisplay);
        });
        prevBtn.setOnClickListener((View v)->{
            month = month - 1;
            SalaryImpl impl = new SalaryImpl();
            salaryListToDisplay = impl.getSalary(this, month);
            adapter.updateList(salaryListToDisplay);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSalaryNew();
    }

    private void getSalaryNew(){
        SalaryDAO dao = new SalaryDAO(this);
        long value = dao.getMaxMonthNotPaid();
        if(value == -1){
            month = getMonthForFirstTime();
        }
        else{
            month = (int)value;
            SalaryImpl impl = new SalaryImpl();
            ArrayList<Salary> list = impl.getSalary(this, month);
            if(checkIfSalaryPaidForMonth(list)){
                month = month + 1;
            }
            else{
                salaryListToDisplay = list;
            }
        }
        if(salaryListToDisplay == null || salaryListToDisplay.isEmpty()){
            SalaryCreation creation = new SalaryCreation(this, month);
            salaryListToDisplay = creation.getSalaryList();
        }
        fillView();
    }

    private void fillView(){
        adapter = new RecyclerSalaryAdapter(this, salaryListToDisplay, month);
        rv.setAdapter(adapter);
        monthView.setText(AppUtil.getMonthName(month));
        createSnackBarWithTotal();
    }

    private boolean checkIfSalaryPaidForMonth(ArrayList<Salary> list){
        int count = 0;
        for(Salary s : list){
            if(s.getPaidOn() == null || s.getPaidOn().isEmpty()){
                count++;
            }
        }
        if(count > 0){
            return false;
        }
        else
            return true;
    }

    private int getMonthForFirstTime(){
        AttendanceImpl implA = new AttendanceImpl();
        String date = implA.getFirstAttendanceDate(this);
        Calendar cal = Calendar.getInstance();
        try{
            if(!date.isEmpty()){
                cal.setTime(AppUtil.formatDateFromString(date));
            }
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return cal.get(Calendar.MONTH);
    }

    private void createSnackBarWithTotal(){
        double sum = 0;
        for(Salary s : salaryListToDisplay){
            if(s.getDisbursedSalary() > 0 && (s.getPaidOn() == null || s.getPaidOn().isEmpty())){
                sum += s.getDisbursedSalary();
            }
        }
        Snackbar.make(coord, "Total Salary to pay", Snackbar.LENGTH_INDEFINITE).setAction(sum + "", (View v)->{}).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Update");
        item.setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item != null && item.getTitle() != null && item.getTitle().equals("Update")){
            SalaryCreation create = new SalaryCreation(this, month);
            String[] str = AppUtil.calculateDate(month);
            //create.fillInSalary(str[0], str[1], salaryListToDisplay);
            salaryListToDisplay = create.getSalaryList();
            adapter.updateList(salaryListToDisplay);
            createSnackBarWithTotal();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
