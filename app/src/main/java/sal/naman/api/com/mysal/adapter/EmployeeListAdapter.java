package sal.naman.api.com.mysal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeDao;
import sal.naman.api.com.mysal.DAO.EmployeeDao.EmployeeImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.Employee;
import sal.naman.api.com.mysal.service.DiffUtilsCallBackEmployee;

/**
 * Created by Naman-PC on 17-08-2018.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {

    private Context mContext;
    private ArrayList<Employee> mList;

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{

        private TextView name, salary, joinDate, endDate;
        public EmployeeViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.content_home_emp_name);
            salary = itemView.findViewById(R.id.content_home_emp_salary);
            joinDate = itemView.findViewById(R.id.content_home_emp_joining_date);
            endDate = itemView.findViewById(R.id.content_home_emp_end_date);
            itemView.setOnCreateContextMenuListener((ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) -> {
                    contextMenu.setHeaderTitle(name.getText());
                    contextMenu.add("delete").setOnMenuItemClickListener((MenuItem menuItem) -> {
                            if(menuItem.getTitle().toString().equalsIgnoreCase("Delete")){
                                EmployeeDao dao = new EmployeeDao(mContext);
                                if(name != null && name.getText() != null){
                                    dao.deleteEmployee(name.getText().toString());
                                    updateList(new EmployeeImpl().getAllEmployees(mContext));
                                }
                                else{
                                    Toast.makeText(mContext, "Error in deleting employee", Toast.LENGTH_SHORT).show();
                                }
                            }
                            return true;
                    });
            });
        }
    }

    public void updateList(ArrayList<Employee> newList){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilsCallBackEmployee(newList, this.mList));
        mList.clear();
        mList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public EmployeeListAdapter(Context context, ArrayList<Employee> list){
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_home, parent, false);
        return new EmployeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getName());
        holder.salary.setText(mList.get(position).getSalary() + "");
        holder.joinDate.setText(mList.get(position).getJoiningDate());
        holder.endDate.setText(mList.get(position).getEndDate());
        if(holder.endDate.getText().toString().isEmpty()){
            holder.endDate.setVisibility(View.GONE);
        }
    }
}
