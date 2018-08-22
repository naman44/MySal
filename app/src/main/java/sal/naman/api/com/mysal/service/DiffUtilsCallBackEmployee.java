package sal.naman.api.com.mysal.service;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

import sal.naman.api.com.mysal.model.Employee;

/**
 * Created by Naman-PC on 19-08-2018.
 */

public class DiffUtilsCallBackEmployee extends DiffUtil.Callback {

    private ArrayList<Employee> oldList;
    private ArrayList<Employee> newList;

    public DiffUtilsCallBackEmployee(ArrayList<Employee> newList, ArrayList<Employee> oldList){
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
        return oldList.get(oldItemPosition).getName().equalsIgnoreCase(newList.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getName().equalsIgnoreCase(newList.get(newItemPosition).getName());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
