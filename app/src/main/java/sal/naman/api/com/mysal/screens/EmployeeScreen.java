package sal.naman.api.com.mysal.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.adapter.EmployeeListAdapter;
import sal.naman.api.com.mysal.model.Employee;

public class EmployeeScreen extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<Employee> listEmployee;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rv = findViewById(R.id.recycler_lone);
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutRv);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
        context = this;

        rv.addOnItemTouchListener(new RecyclerTouchListener(this, rv, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                EmployeeEditDialog dialog = new EmployeeEditDialog(context);
                dialog.fillValuesToDialog(listEmployee.get(position));
                dialog.show();
            }

            @Override
            public void onLongPress(View view, int position) {

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        listEmployee = new EmployeeImpl().getAllEmployees(this);
        EmployeeListAdapter adapter = new EmployeeListAdapter(this, listEmployee);
        rv.setAdapter(adapter);
    }
}
