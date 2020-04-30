package com.nexwise.pointscan.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.Image;
import com.nexwise.pointscan.constant.CloudConstant;
import com.nexwise.pointscan.utils.ImageDownload;
import java.util.List;

/**
 * Created by shifan_xiao on 2019/7/22.
 */
public class ImagesListAdapter extends RecyclerView .Adapter<ImagesListAdapter.ViewHolder>{
    private List<Image> imageList;

    public OnItemClickListener itemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ImagesListAdapter(List<Image> images){
        imageList = images;
    }

    public void setImageList(List<Image> images) {
        imageList = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将布局转化为View并传递给RecycleView封装好的ViewHolder
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //建立起ViewHolder中视图与数据的关联
        Image image = imageList.get(position);
        if (image.getUrl().contains("storage")) {
            Bitmap bitmap = BitmapFactory.decodeFile(image.getUrl());
            Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap, 180,
                    180);
            holder.imageView.setImageBitmap (newBitmap);
        } else {
            Bitmap bitmap = null;
         //   Bitmap bitmap = ImageDownload.CacheHelper.sLruCache.get ("ChatRecyclerAdapter" + position);
            if (bitmap == null) {
                new ImageDownload.ImageTask(new ImageDownload.ImageTask.Listener () {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        holder.imageView.setImageBitmap (bitmap);
                    }
                }).execute (CloudConstant.Source.SERVER_IP + image.getUrl(), "ChatRecyclerAdapter" + position);
            }else {
                holder.imageView.setImageBitmap (bitmap);
            }
        }


        switch (image.getType()) {
            case 1://监控杆图
                holder.textView.setText("监控杆图");
                break;
            case 2://地图截图
                holder.textView.setText("地图截图");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
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

