package com.nexwise.pointscan.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.LruCache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownload {
    public static final class CacheHelper{
        public static LruCache<String, Bitmap> sLruCache;
        static {
            sLruCache = new LruCache<String, Bitmap> ((int)Runtime.getRuntime ().maxMemory ()/4){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount ();
                }
            };
        }
    }

    public static final class ImageTask extends AsyncTask<String, Void, Bitmap> {

        public Listener mListener;

        public ImageTask(Listener listener) {
            mListener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = getBitmap (strings[0]);
            CacheHelper.sLruCache.put (strings[1], bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mListener.onSuccess (bitmap);
        }

        public interface Listener{
            void onSuccess(Bitmap bitmap);
        }

        public static Bitmap getBitmap(String url){
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            BufferedInputStream stream = null;
            URL url1 = null;
            try {
                url1 = new URL (url);
                URLConnection connection = url1.openConnection ();
                stream = new BufferedInputStream(connection.getInputStream ());
                bitmap = BitmapFactory.decodeStream (stream);
                newBitmap = ThumbnailUtils.extractThumbnail(bitmap, 180,
                        180);
                bitmap.recycle();

            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }finally {
                if (stream != null) {
                    try {
                        stream.close ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                }
            }
            return newBitmap;
        }
    }
}
