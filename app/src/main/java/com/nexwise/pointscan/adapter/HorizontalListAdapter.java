package com.nexwise.pointscan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.Image;
import com.nexwise.pointscan.constant.CloudConstant;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by shifan_xiao on 2019/7/22.
 */
public class HorizontalListAdapter extends BaseAdapter {

    Context mContext;
    private List<Image> images;

    public HorizontalListAdapter(Context context, List<Image> images) {
        this.images = images;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return (images != null ? images.size() : 0);
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
        final ViewHolder viewHolder;
        Image image = images.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, null);
            viewHolder.name = convertView.findViewById(R.id.type_name);
            viewHolder.imageView = convertView.findViewById(R.id.imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (image.getType()) {
            case 1://监控杆图
                viewHolder.name.setText("监控杆图");
                break;
            case 2://地图截图
                viewHolder.name.setText("地图截图");
                break;
        }
        Bitmap bitmap = CacheHelper.sLruCache.get("ChatRecyclerAdapter" + position);
        if (bitmap == null) {
            new ImageTask(new ImageTask.Listener() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    viewHolder.imageView.setImageBitmap(bitmap);
                }
            }).execute(CloudConstant.Source.network_IP + image.getUrl(), "ChatRecyclerAdapter" + position);
        } else {
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }

    private static final class CacheHelper {
        private static LruCache<String, Bitmap> sLruCache;

        static {
            sLruCache = new LruCache<String, Bitmap>((int) Runtime.getRuntime().maxMemory() / 4) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
    }

    private static final class ImageTask extends AsyncTask<String, Void, Bitmap> {

        private HorizontalListAdapter.ImageTask.Listener mListener;

        ImageTask(HorizontalListAdapter.ImageTask.Listener listener) {
            mListener = listener;
        }

        private static Bitmap getBitmap(String url) {
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            BufferedInputStream stream = null;
            URL url1 = null;
            try {
                url1 = new URL(url);
                URLConnection connection = url1.openConnection();
                stream = new BufferedInputStream(connection.getInputStream());
                bitmap = BitmapFactory.decodeStream(stream);
                newBitmap = ThumbnailUtils.extractThumbnail(bitmap, 80,
                        80);
                bitmap.recycle();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return newBitmap;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = getBitmap(strings[0]);
            HorizontalListAdapter.CacheHelper.sLruCache.put(strings[1], bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mListener.onSuccess(bitmap);
        }

        public interface Listener {
            void onSuccess(Bitmap bitmap);
        }
    }

    class ViewHolder {
        TextView name;
        ImageView imageView;

    }

}
