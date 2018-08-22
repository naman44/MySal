package sal.naman.api.com.mysal.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import sal.naman.api.com.mysal.AppUtil;
import sal.naman.api.com.mysal.DAO.SalaryDao.SalaryImpl;
import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.Salary;
import sal.naman.api.com.mysal.screens.AttendanceDetailScreen;
import sal.naman.api.com.mysal.service.DiffUtilsCallBackSalary;
import sal.naman.api.com.mysal.service.SalaryCreation;

/**
 * Created by Naman-PC on 30-06-2018.
 */

public class RecyclerSalaryAdapter extends RecyclerView.Adapter<RecyclerSalaryAdapter.SalaryViewHolder> {

    private ViewGroup mParent;
    private Context mContext;
    private ArrayList<Salary> mList;
    private int month;
    static class SalaryViewHolder extends RecyclerView.ViewHolder {


        CardView card;
        TextView nameView, salaryView, attendanceView, advance, penalty, rest, disbursedSalary, absent,
                paidAmount, remarks, paidOn, paymentDetails, carryOver;
        Button payBtn, detailsBtn;
        View divideLine;
        LinearLayout layoutPaid;
        public SalaryViewHolder(View itemView){
            super(itemView);
            card = itemView.findViewById(R.id.card_salary);
            nameView = itemView.findViewById(R.id.name_card_salary);
            attendanceView = itemView.findViewById(R.id.attendance_card_salary);
            salaryView = itemView.findViewById(R.id.salary_card_salary);
            advance = itemView.findViewById(R.id.salary_card_advance);
            penalty = itemView.findViewById(R.id.salary_card_penalty);
            rest = itemView.findViewById(R.id.salary_card_rest);
            disbursedSalary = itemView.findViewById(R.id.to_pay_salary_card_salary);
            absent = itemView.findViewById(R.id.absent_card_salary);
            payBtn = itemView.findViewById(R.id.pay_salary_card_btn);
            detailsBtn = itemView.findViewById(R.id.salary_card_details_btn);
            divideLine = itemView.findViewById(R.id.paid_divide_line);
            layoutPaid = itemView.findViewById(R.id.paid_layout);
            paidAmount = itemView.findViewById(R.id.salary_card_paid_amount);
            remarks = itemView.findViewById(R.id.salary_card_remarks);
            paidOn = itemView.findViewById(R.id.salary_card_paid_on);
            paymentDetails = itemView.findViewById(R.id.salary_card_payment_details_text);
            carryOver = itemView.findViewById(R.id.salary_card_carry_over);
        }
    }

    public RecyclerSalaryAdapter(Context context, ArrayList<Salary> list, int month){
        this.mContext = context;
        this.mList = list;
        this.month = month;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public SalaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv_salary, parent, false);
        mParent = parent;
        return new SalaryViewHolder(v);
    }

    public void updateList(ArrayList<Salary> newList){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilsCallBackSalary(newList, mList));
        mList.clear();
        mList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    private void animate(SalaryViewHolder holder){
        final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(anim);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryViewHolder holder, int position) {
        holder.nameView.setText(mList.get(position).getEmployeeName());
        holder.attendanceView.setText("P: " + mList.get(position).getAttendance());
        holder.salaryView.setText("Salary: " + mList.get(position).getSalary());
        holder.advance.setText("Advance: " + mList.get(position).getAdvance());

        double diff = SalaryCreation.getCarryOverAmount(mContext, mList.get(position).getEmployeeName(), month - 1);
        if(diff != 0){
            holder.carryOver.setVisibility(View.VISIBLE);
            holder.carryOver.setText("Carry Over: " + diff);
        }
        else
            holder.carryOver.setVisibility(View.GONE);

        holder.penalty.setText("Penalty: " + mList.get(position).getTimePenalty());
        holder.rest.setText("R: " + mList.get(position).getRestDays());
        holder.disbursedSalary.setText(mList.get(position).getDisbursedSalary() + "");
        holder.absent.setText("A: " + mList.get(position).getAbsent() + "");
        if(mList.get(position).getPaidOn() == null || mList.get(position).getPaidOn().isEmpty()){
            holder.divideLine.setVisibility(View.GONE);
            holder.layoutPaid.setVisibility(View.GONE);
            holder.paymentDetails.setVisibility(View.GONE);
        }
        else{
            holder.divideLine.setVisibility(View.VISIBLE);
            holder.layoutPaid.setVisibility(View.VISIBLE);
            holder.paymentDetails.setVisibility(View.VISIBLE);
            holder.paidAmount.setText(mList.get(position).getPaidSalary() + "");
            holder.paidOn.setText(mList.get(position).getPaidOn());
            holder.payBtn.setVisibility(View.GONE);
        }
        if(mList.get(position).getAttendance() == 0){
            holder.payBtn.setVisibility(View.GONE);
        }

        // Listeners below
        holder.rest.setOnClickListener((View v)->{
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("Rest Edit");
            EditText text = new EditText(mContext);
            text.setText(holder.rest.getText());
            text.setSelection(holder.rest.getText().length());
            dialog.setView(text);
            dialog.setPositiveButton("Save", (DialogInterface dialogInterface, int i)->{
                String restValue = text.getText().toString();
                restValue = restValue.replace("R: ", "");
                mList.get(position).setRestDays(Integer.parseInt(restValue));
                SalaryCreation create = new SalaryCreation(mContext, month);
                create.updateSalaryOnChange(mList.get(position), "");
                notifyDataSetChanged();
                dialogInterface.dismiss();
            });
            dialog.show();
        });

        holder.advance.setOnClickListener((View v)->{
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("Advance Edit");
            EditText text = new EditText(mContext);
            text.setText(holder.advance.getText());
            text.setSelection(holder.advance.getText().length());
            dialog.setView(text);
            dialog.setPositiveButton("Save", (DialogInterface dialogInterface, int i)->{
                String advanceValue = text.getText().toString();
                advanceValue = advanceValue.replace("Advance: ", "");
                mList.get(position).setAdvance(Double.valueOf(advanceValue));
                SalaryCreation create = new SalaryCreation(mContext, month);
                create.updateSalaryOnChange(mList.get(position), "");
                notifyDataSetChanged();
                dialogInterface.dismiss();
            });
            dialog.show();
        });

        holder.payBtn.setOnClickListener((View v) ->{
            TransitionManager.beginDelayedTransition(mParent);
            holder.divideLine.setVisibility(View.VISIBLE);
            holder.layoutPaid.setVisibility(View.VISIBLE);
            holder.paymentDetails.setVisibility(View.VISIBLE);
            holder.paidAmount.setText("" + mList.get(position).getDisbursedSalary());
            holder.payBtn.setVisibility(View.GONE);
            holder.paidOn.setVisibility(View.VISIBLE);
            holder.paidOn.setText(AppUtil.formatDate(Calendar.getInstance().getTime()));
            mList.get(position).setPaidSalary(Double.valueOf(holder.paidAmount.getText().toString()));
            mList.get(position).setPaidOn(holder.paidOn.getText().toString());
            SalaryImpl impl = new SalaryImpl();
            long result = impl.updateSalary(mContext, mList.get(position));
            if(result > 0){
                Toast.makeText(mContext, "Salary Updated!!!", Toast.LENGTH_SHORT).show();
            }
                });
        holder.paidAmount.setOnClickListener((View vi)->{
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("Paid Amount");
            EditText text = new EditText(mContext);
            text.setText(holder.paidAmount.getText());
            text.setSelection(holder.paidAmount.getText().length());
            dialog.setView(text);
            dialog.setPositiveButton("Save", (DialogInterface dialogInterface, int i)->{
                SalaryImpl impl = new SalaryImpl();
                int result = impl.updateSalary(mContext, holder.nameView.getText().toString(),
                        Double.valueOf(text.getText().toString()), month);
                holder.paidAmount.setText(text.getText().toString());
                if(result > 0){
                    Toast.makeText(mContext, "Salary Updated!!!", Toast.LENGTH_SHORT).show();
                }
                dialogInterface.dismiss();
            });
            dialog.show();
        });
        holder.paidOn.setOnClickListener((View vi) ->{
            Calendar cal = Calendar.getInstance();
            try{
                cal.setTime(AppUtil.formatDateFromString(holder.paidOn.getText().toString()));
            }catch (ParseException pe){
                pe.printStackTrace();
            }
            DatePickerDialog dialogDate = new DatePickerDialog(mContext, (DatePicker datePicker, int year, int month, int day)->{
                cal.set(year, month, day);
                holder.paidOn.setText(AppUtil.formatDate(cal.getTime()));
                SalaryImpl impl = new SalaryImpl();
                int result = impl.updateSalary(mContext, holder.nameView.getText().toString(),
                        holder.paidOn.getText().toString(), this.month);
                if(result > 0){
                    Toast.makeText(mContext, "Salary Updated!!!", Toast.LENGTH_SHORT).show();
                }
            }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            dialogDate.show();
        });

        holder.detailsBtn.setOnClickListener((View v)->{
            Intent intent = new Intent(mContext, AttendanceDetailScreen.class);
            intent.putExtra("name", mList.get(position).getEmployeeName());
            intent.putExtra("month", month+"");
            mContext.startActivity(intent);
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
