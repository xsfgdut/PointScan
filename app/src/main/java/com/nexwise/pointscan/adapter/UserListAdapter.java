package com.nexwise.pointscan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.User;

import java.util.List;

/**
 * Created by shifan_xiao on 2019/7/22.
 */
public class UserListAdapter extends BaseAdapter {

    Context mContext;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return (userList != null ? userList.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user, null);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.name = convertView.findViewById(R.id.user_name1);
            viewHolder.userPhone = convertView.findViewById(R.id.user_number);
            viewHolder.userAddress = convertView.findViewById(R.id.user_address);
            viewHolder.userMark = convertView.findViewById(R.id.user_mark);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userName.setText(userList.get(position).getUserName());
        viewHolder.name.setText(userList.get(position).getName());
        viewHolder.userPhone.setText(userList.get(position).getTel());
        viewHolder.userAddress.setText(userList.get(position).getAddress());
        viewHolder.userMark.setText(userList.get(position).getRemark());

        return convertView;
    }


    class ViewHolder {
        TextView userName;
        TextView name;
        TextView userPhone;
        TextView userAddress;
        TextView userMark;
    }


}
