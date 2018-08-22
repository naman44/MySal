package sal.naman.api.com.mysal.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.adapter.AttendanceDetailAdapter;
import sal.naman.api.com.mysal.model.EmployeeAttendance;

public class AttendanceDetailScreen extends AppCompatActivity {

    RecyclerView rv;
    String name;
    int month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.recycler_lone);
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutRv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String monthStr = intent.getStringExtra("month");
        ArrayList<EmployeeAttendance> list = new ArrayList<>();
        if(name!= null && (monthStr!= null && !monthStr.isEmpty())){
            month = Integer.parseInt(monthStr);
            String[] str = AppUtil.calculateDate(month);
            AttendanceImpl impl = new AttendanceImpl();
            list = impl.getAttendanceForMonth(this, name, str[0], str[1]);
        }
        AttendanceDetailAdapter adapter = new AttendanceDetailAdapter(this, list);
        rv.setAdapter(adapter);
    }
}
