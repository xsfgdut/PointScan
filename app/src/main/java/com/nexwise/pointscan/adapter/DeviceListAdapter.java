package com.nexwise.pointscan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.Device;

import java.util.List;

/**
 * Created by shifan_xiao on 2019/7/22.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{

    private List<Device> deviceList;

    public OnItemClickListener itemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DeviceListAdapter(List<Device> devices){
        this.deviceList=devices;
    }
    public void setData(List<Device> devices) {
        this.deviceList = devices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将布局转化为View并传递给RecycleView封装好的ViewHolder
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //建立起ViewHolder中视图与数据的关联
        holder.textView.setText(deviceList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            textView= itemView.findViewById(R.id.type_name);
            imageView = itemView.findViewById(R.id.imageview);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener!=null){
                itemClickListener.onItemClick(view,getPosition());
            }

        }

    }
}
