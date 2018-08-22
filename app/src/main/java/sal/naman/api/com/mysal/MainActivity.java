package sal.naman.api.com.mysal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceDao;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.adapter.HomeViewEmployeeAdapter;
import sal.naman.api.com.mysal.model.Employee;
import sal.naman.api.com.mysal.screens.AddEmployeeScreen;
import sal.naman.api.com.mysal.screens.EmployeeEditDialog;
import sal.naman.api.com.mysal.screens.EmployeeScreen;
import sal.naman.api.com.mysal.screens.MarkAttendanceScreen;
import sal.naman.api.com.mysal.screens.SalaryPage;
import sal.naman.api.com.mysal.service.EmployeeService;

public class MainActivity extends AppCompatActivity{

    Button mark, markToday, salaryPage, purgeDbBtn;
    ListView listView;
    ArrayList<Employee> listEmployee;
    TextView attendanceMarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mark = findViewById(R.id.new_attendance);
        attendanceMarked = findViewById(R.id.salary_marked);
        purgeDbBtn = findViewById(R.id.purge_db_btn);
        markToday = findViewById(R.id.mark_attend_auto);
        listView = findViewById(R.id.employee_list_home);
        salaryPage = findViewById(R.id.salary_page);
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listEmployee = new EmployeeImpl().getCurrentEmployees(this);
        HomeViewEmployeeAdapter adapter = new HomeViewEmployeeAdapter(this, R.layout.content_list_home, listEmployee);
        listView.setAdapter(adapter);
        AttendanceDao dao = new AttendanceDao(this);
        int x = dao.isAttendanceMarkedForDay(AppUtil.formatDate(Calendar.getInstance().getTime()));
        if(x > 0){
            attendanceMarked.setText(R.string.attendance_today_marked);
            attendanceMarked.setTextColor(Color.GREEN);
        }
    }

    private void initListeners(){
        mark.setOnClickListener((View view) -> startActivity(new Intent(getApplicationContext(), MarkAttendanceScreen.class)));
        findViewById(R.id.fab).setOnClickListener((View view) -> startActivity(new Intent(this, AddEmployeeScreen.class)));
        markToday.setOnClickListener((View v) ->{
            new EmployeeService(this).createList(AppUtil.formatDate(Calendar.getInstance().getTime()));
            Toast.makeText(this, "Attendance marked", Toast.LENGTH_SHORT).show();
        });
        salaryPage.setOnClickListener((View v) -> startActivity(new Intent(this, SalaryPage.class)));
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            EmployeeEditDialog dialog = new EmployeeEditDialog(this);
            dialog.fillValuesToDialog(listEmployee.get(position));
            dialog.show();
        });
        /*
        purgeDbBtn.setOnClickListener((View v)->{
            //TODO: purge db of all data with backup
            final String inFileName = getDatabasePath(DaoUtil.DB_NAME).getPath();
            try{
                File dbFile = new File(inFileName);
                FileInputStream fis = new FileInputStream(dbFile);
                String outFileName = getDatabasePath(DaoUtil.DB_NAME).getPath() + "old";
                OutputStream output = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                fis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            DatabaseAdapter db = DatabaseAdapter.getInstance(this);
            db.deleteAllData();
            //TODO : add more features before allowing purge
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_employees){
            Intent intent = new Intent(this, EmployeeScreen.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
