package sal.naman.api.com.mysal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.Employee;

/**
 * Created by Naman-PC on 29-06-2018.
 */

public class HomeViewEmployeeAdapter extends ArrayAdapter<Employee> {

    private Context mContext;
    private ArrayList<Employee> mList;
    private int resourceId;

    public HomeViewEmployeeAdapter(Context context, int resourceId, ArrayList<Employee> list){
        super(context, resourceId, list);
        this.mContext = context;
        this.mList = list;
        this.resourceId = resourceId;
    }

    private static class ViewHolder{
        private TextView name, salary, joinDate, endDate;

        ViewHolder(View v){
            name = v.findViewById(R.id.content_home_emp_name);
            salary = v.findViewById(R.id.content_home_emp_salary);
            joinDate = v.findViewById(R.id.content_home_emp_joining_date);
            endDate = v.findViewById(R.id.content_home_emp_end_date);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)   convertView.getTag();
        }
        holder.name.setText(mList.get(position).getName());
        holder.salary.setText(mList.get(position).getSalary() + "");
        holder.joinDate.setText(mList.get(position).getJoiningDate());
        holder.endDate.setText(mList.get(position).getEndDate());
        if(holder.endDate.getText().toString().isEmpty()){
            holder.endDate.setVisibility(View.GONE);
        }
        return convertView;
    }
}
