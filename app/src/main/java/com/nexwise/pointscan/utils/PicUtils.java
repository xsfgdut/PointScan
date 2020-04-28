package com.nexwise.pointscan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dcyy on 2017/1/16.
 */

public class PicUtils {
    /**
     * 首先默认文件保存路径
     */
    public static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    //public static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/zhongjing";//保存的确切位置
    public static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/ZJBOX";//保存的确切位置

    //    public static final String SAVE_REAL_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/zhongjing/";
    public static void saveBitmap(Bitmap bm, String fileName) {
        saveBitmap(bm, SAVE_REAL_PATH, fileName);
//        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/zhongjing/");
        /**    File filePath = new File(SAVE_REAL_PATH);
         if (!filePath.exists()) {
         filePath.mkdirs();
         }
         File picPath = new File(filePath, fileName);
         try {
         if(!picPath.exists()){
         picPath.createNewFile();
         }
         } catch (IOException e) {
         e.printStackTrace();
         Log.d("测试", "展示错误" + e.toString());
         }

         try {

         //            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picPath));
         FileOutputStream foStream = new FileOutputStream(picPath);
         bm.compress(Bitmap.CompressFormat.PNG, 100, foStream);
         foStream.flush();
         foStream.close1();
         } catch (IOException e) {
         Log.d("测试", "展示错误" + e.toString());
         }
         **/
    }

    public static void saveBitmap(Bitmap bm, String path, String fileName) {
//        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/zhongjing/");
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File picPath = new File(filePath, fileName);
        try {
            if (!picPath.exists()) {
                picPath.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("测试", "展示错误" + e.toString());
        }

        try {

//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picPath));
            FileOutputStream foStream = new FileOutputStream(picPath);
            bm.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.flush();
            foStream.close();
        } catch (IOException e) {
            Log.d("测试", "展示错误" + e.toString());
        }
    }

    public static Uri getOutputMediaFileUri(Context context) {
        File mediaFile = null;
        String cameraPath;
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            mediaFile = new File(mediaStorageDir.getPath()
                    + File.separator
                    + "Pictures/temp.jpg");//注意这里需要和filepaths.xml中配置的一样
            cameraPath = mediaFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// sdk >= 24  android7.0以上
            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",//与清单文件中android:authorities的值保持一致
                    mediaFile);//FileProvider方式或者ContentProvider。也可使用VmPolicy方式
            return contentUri;

        } else {
            return Uri.fromFile(mediaFile);//或者 Uri.isPaise("file://"+file.toString()

        }
    }

}
