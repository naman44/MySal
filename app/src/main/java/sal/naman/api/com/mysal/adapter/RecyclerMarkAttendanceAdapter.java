package sal.naman.api.com.mysal.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.EmployeeAttendance;
import sal.naman.api.com.mysal.service.DiffUtilsCallBack;

/**
 * Created by Naman-PC on 16-06-2018.
 */

public class RecyclerMarkAttendanceAdapter extends RecyclerView.Adapter<RecyclerMarkAttendanceAdapter.EmployeeViewHolder> {

    private ArrayList<EmployeeAttendance> listEmployee;
    private Context mContext;
    private ViewGroup mParent;

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView name, timeIn, timeOut, id;
        EditText advance;
        Button saveCard;
        ImageButton saveSmall, expandCard;
        EmployeeViewHolder(View itemView){
            super(itemView);
            card = itemView.findViewById(R.id.card_mark);
            name = itemView.findViewById(R.id.employee_name_mark);
            timeIn = itemView.findViewById(R.id.employee_time_in);
            timeOut = itemView.findViewById(R.id.employee_time_out);
            advance = itemView.findViewById(R.id.employee_advance);
            id = itemView.findViewById(R.id.employee_id_mark);
            saveCard = itemView.findViewById(R.id.save_card_details);
            saveSmall = itemView.findViewById(R.id.btn_save_small);
            expandCard = itemView.findViewById(R.id.btn_expand);
        }
    }

    public RecyclerMarkAttendanceAdapter(Context mContext, ArrayList<EmployeeAttendance> list){
        this.listEmployee = list;
        this.mContext = mContext;
    }

    public void updateList(ArrayList<EmployeeAttendance> newList){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilsCallBack(newList, this.listEmployee));
        listEmployee.clear();
        listEmployee.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return listEmployee.size();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_mark, parent, false);
        mParent = parent;
        return new EmployeeViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        // init values
        holder.name.setText(listEmployee.get(position).getName());
        holder.advance.setText(listEmployee.get(position).getAdvance()+"");
        if(!listEmployee.get(position).isPresent()){
            holder.name.setTextColor(Color.RED);
            holder.timeIn.setText("-");
            holder.timeOut.setText("-");
        }
        else {
            holder.name.setTextColor(Color.BLACK);
            holder.timeIn.setText(listEmployee.get(position).getTimeIn());
            holder.timeOut.setText(listEmployee.get(position).getTimeOut());
        }

        // listeners
        holder.expandCard.setOnClickListener((View v)->{
            TransitionManager.beginDelayedTransition(mParent);
            holder.timeIn.setVisibility(View.VISIBLE);
            holder.timeOut.setVisibility(View.VISIBLE);
            holder.advance.setVisibility(View.VISIBLE);
            holder.saveCard.setVisibility(View.VISIBLE);
            holder.expandCard.setVisibility(View.GONE);
            holder.saveSmall.setVisibility(View.GONE);
        });

        holder.timeIn.setOnClickListener((View v) ->{
            int[] timeVal = getHourMinuteForTimePicker(position, "IN");
            new TimePickerDialog(mContext, (TimePicker timePicker, int selectedHour, int selectedMinute) ->
                    holder.timeIn.setText(String.format("%02d:%02d",selectedHour,selectedMinute)),
                    timeVal[0], timeVal[1], true).show();
        });

        holder.timeOut.setOnClickListener((View v) ->{
            int[] timeVal = getHourMinuteForTimePicker(position, "OUT");
            new TimePickerDialog(mContext, (TimePicker timePicker, int selectedHour, int selectedMinute) ->
                    holder.timeOut.setText(String.format("%02d:%02d",selectedHour,selectedMinute)),
                    timeVal[0], timeVal[1], true).show();
        });

        holder.name.setOnClickListener((View v) ->{
            if(listEmployee.get(position).isPresent()){
                holder.name.setTextColor(Color.RED);
                holder.timeIn.setText("-");
                holder.timeOut.setText("-");
                listEmployee.get(position).setPresent(false);
            }
            else{
                holder.name.setTextColor(Color.BLACK);
                if(listEmployee.get(position).getTimeIn().equals("-") || listEmployee.get(position).getTimeOut().equals("-")){
                    holder.timeIn.setText("09:00");
                    holder.timeOut.setText("17:30");
                }else{
                    holder.timeIn.setText(listEmployee.get(position).getTimeIn());
                    holder.timeOut.setText(listEmployee.get(position).getTimeOut());
                }
                listEmployee.get(position).setPresent(true);
            }
        });

        holder.advance.setOnClickListener((View v) ->{
            holder.advance.setFocusable(true);
            holder.advance.setFocusableInTouchMode(true);
        });

        holder.saveSmall.setOnClickListener((View v)->{
            EmployeeAttendance attend = new EmployeeAttendance();
            attend.setName(holder.name.getText().toString());
            attend.setAdvance(0);
            attend.setPresent(listEmployee.get(position).isPresent());
            attend.setDate(listEmployee.get(position).getDate());
            attend.setTimeIn("-");
            attend.setTimeOut("-");
            if(attend.isPresent()){
                attend.setTimeIn(holder.timeIn.getText().toString());
                attend.setTimeOut(holder.timeOut.getText().toString());
            }
            AttendanceImpl impl = new AttendanceImpl();
            long result = impl.saveIndividualAttendance(mContext, attend);
            if(result > 0){
                Toast.makeText(mContext, "Attendance updated", Toast.LENGTH_SHORT).show();
            }
        });

        holder.saveCard.setOnClickListener((View v) ->{
            EmployeeAttendance attend = new EmployeeAttendance();
            attend.setName(holder.name.getText().toString());
            attend.setAdvance(Double.parseDouble(holder.advance.getText().toString()));
            attend.setPresent(listEmployee.get(position).isPresent());
            attend.setDate(listEmployee.get(position).getDate());
            attend.setTimeIn("-");
            attend.setTimeOut("-");
            if(attend.isPresent()){
                attend.setTimeIn(holder.timeIn.getText().toString());
                attend.setTimeOut(holder.timeOut.getText().toString());
            }
            AttendanceImpl impl = new AttendanceImpl();
            long result = impl.saveIndividualAttendance(mContext, attend);
            if(result > 0){
                Toast.makeText(mContext, "Attendance updated", Toast.LENGTH_SHORT).show();
            }
            //notifyDataSetChanged();
        });

    }

    private int[] getHourMinuteForTimePicker(int position, String inOut){
        int[] timeArr = new int[2];
        if(listEmployee.get(position).isPresent()){
            String timeString;
            if(inOut.equalsIgnoreCase("IN")){
                timeString = listEmployee.get(position).getTimeIn();
            }
            else{
                timeString = listEmployee.get(position).getTimeOut();
            }
            if(timeString != null && !timeString.equals("-")){
                String[] strArr = timeString.split(":");
                timeArr[0] = Integer.parseInt(strArr[0]);
                timeArr[1] = Integer.parseInt(strArr[1]);
            }
        }
        else{
            timeArr[0] = 9;
            timeArr[1] = 0;
        }
        return timeArr;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
