package sal.naman.api.com.mysal.service;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import sal.naman.api.com.mysal.R;
import sal.naman.api.com.mysal.model.EmployeeAttendance;

/**
 * Created by Naman-PC on 23-07-2018.
 */

public class DiffUtilsCallBack extends DiffUtil.Callback {

    private ArrayList<EmployeeAttendance> oldList;
    private ArrayList<EmployeeAttendance> newList;

    public DiffUtilsCallBack(ArrayList<EmployeeAttendance> newList, ArrayList<EmployeeAttendance> oldList){
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getDate().equalsIgnoreCase(newList.get(newItemPosition).getDate());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
