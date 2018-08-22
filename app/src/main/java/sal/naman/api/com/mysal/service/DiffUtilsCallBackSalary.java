package sal.naman.api.com.mysal.service;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sal.naman.api.com.mysal.model.Salary;

public class DiffUtilsCallBackSalary extends DiffUtil.Callback {

    private ArrayList<Salary> oldList;
    private ArrayList<Salary> newList;

    public DiffUtilsCallBackSalary(ArrayList<Salary> newList, ArrayList<Salary> oldList){
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
        return oldList.get(oldItemPosition).getEmployeeName().equalsIgnoreCase(newList.get(newItemPosition).getEmployeeName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compareSalaryObject(oldList.get(oldItemPosition), newList.get((newItemPosition)));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
