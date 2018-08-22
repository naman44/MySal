package sal.naman.api.com.mysal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sal.naman.api.com.mysal.DAO.AttendanceDao.AttendanceImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.EmployeeAttendance;

/**
 * Created by Naman-PC on 20-07-2018.
 */

public class AttendanceDetailAdapter extends RecyclerView.Adapter<AttendanceDetailAdapter.AttendanceViewHolder> {

    private Context mContext;
    private ArrayList<EmployeeAttendance> mList;
    static class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView timeIn, timeOut, advance, date;
        LinearLayout layout;
        public AttendanceViewHolder(View itemView){
            super(itemView);
            timeIn = itemView.findViewById(R.id.time_in_detail);
            timeOut = itemView.findViewById(R.id.time_out_detail);
            advance = itemView.findViewById(R.id.advance_att_detail);
            layout = itemView.findViewById(R.id.linear_att_detail);
            date = itemView.findViewById(R.id.date_att_details);
        }
    }

    public AttendanceDetailAdapter(Context context, ArrayList<EmployeeAttendance> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv_attendance_detail, parent, false);
        return new AttendanceViewHolder(v);
    }

    private void animate(AttendanceViewHolder holder){
        final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anticipate_overshoot_interpolator);
        holder.itemView.setAnimation(anim);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        //animate(holder);
        if(mList.get(position).getTimeIn().equalsIgnoreCase("-")){
            holder.date.setTextColor(Color.RED);
        }
        holder.date.setText(mList.get(position).getDate());
        holder.timeIn.setText(mList.get(position).getTimeIn());
        holder.timeOut.setText(mList.get(position).getTimeOut());
        holder.advance.setText(mList.get(position).getAdvance()+"");

        holder.date.setOnClickListener((View v)->{
            if(!mList.get(position).isPresent()){
                holder.date.setTextColor(Color.BLACK);
                mList.get(position).setPresent(true);
                mList.get(position).setTimeIn("09:00");
                mList.get(position).setTimeOut("17:30");
            }
            else{
                holder.date.setTextColor(Color.RED);
                mList.get(position).setPresent(false);
                mList.get(position).setTimeIn("-");
                mList.get(position).setTimeOut("-");
            }
            holder.timeIn.setText(mList.get(position).getTimeIn());
            holder.timeOut.setText(mList.get(position).getTimeOut());
            AttendanceImpl impl = new AttendanceImpl();
            impl.saveIndividualAttendance(mContext, mList.get(position));
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
