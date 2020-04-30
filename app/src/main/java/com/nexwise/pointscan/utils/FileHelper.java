package com.nexwise.pointscan.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shifan_xiao on 2018/8/6.
 */

public class FileHelper {
    private static String TXT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Aircraft" + File.separator;
    private static String fileName;// 文件命名

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName, boolean isCover) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            if (isCover) {//写文件覆盖前面的内容
                raf.seek(0);
            } else {
                raf.seek(file.length());
            }
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        List newList = new ArrayList<Double>();
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        newList.add(line + "\n");
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return strFilePath;
    }

    /**
     * 根据行读取内容
     *
     * @return
     */
    public static List<String> getTxtData(Context context, String filePath) {
        //将读出来的一行行数据使用List存储
//        String filePath = "/mnt/sdcard/efb/about.txt";

        List newList = new ArrayList<String>();
        try {
            File file = new File(filePath);
            int count = 0;//初始化 key值
            if (file.isFile() && file.exists()) {//文件存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                        newList.add(count, reds);
                        count++;
                    }
                }
                isr.close();
                br.close();
            } else {
                Toast.makeText(context, "can not find file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newList;
    }

    /**
     * @param fileName 文件夹名称
     * @return 生成的文件夹
     */
    public static File creatSDFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建文件夹
     *
     * @param dirName 文件路径
     */
    public static boolean creatSDDir(String dirName) {
        File dir = new File(dirName);
        return dir.mkdirs();
    }

    //删除文件
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名称
     * @return 存在返回true，不存在返回false
     */
    public static boolean isFileExist(String fileName) {
        try {
            File file = new File(fileName);
            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Bitmap文件保存到本地文件
     *
     * @param file  文件名
     * @param photo 要保存的bitmap
     */
    public static File saveFile(File file, Bitmap photo) {
        FileOutputStream fOut = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 通知媒体库更新文件
     */
    public static void scanFile(Context context, String filePath) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(filePath);
                Uri contentUri = Uri.fromFile(file);
                // Uri contentUri = Uri.fromFile(Environment.getExternalStorageDirectory()); //指定SD卡路径
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //及时更新媒体数据库
    public static void updateGallery(Context context, String filename)//filename是我们的文件全名，包括后缀哦
    {
        MediaScannerConnection.scanFile(context,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    //截图
    public static void screenShot(Context context, String filename, Bitmap bitmap) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// HH:mm:ss
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
//        String picName = System.currentTimeMillis() + ".JPEG";
        String picName = simpleDateFormat.format(date) + ".JPEG";
        File file1 = new File(filename);
        if (file1.exists()) {
            file1.mkdir();
        }
        String path = filename;
        //Toast.makeText(this, path+"  " +picName, Toast.LENGTH_SHORT).show();
        PicUtils.saveBitmap(bitmap, path, picName);
        File file = new File(path, picName);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
        context.sendBroadcast(intent);
        //Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, context.getResources().getString(R.string.save_photo), Toast.LENGTH_SHORT).show();
    }

    //获取文件大小
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

//    public static void saveCapturePictrue(String filePath, Bitmap bitmap) throws InnerException {
//        if (TextUtils.isEmpty(filePath)){
//            LogUtil.d("EZUtils","saveCapturePictrue file is null");
//            return;
//        }
//        File filepath = new File(filePath);
//        File parent = filepath.getParentFile();
//        if (parent == null || !parent.exists() || parent.isFile()) {
//            parent.mkdirs();
//        }
//        FileOutputStream out = null;
//        try {
//            // 保存原图
//
//            if (!TextUtils.isEmpty(filePath)) {
//                out = new FileOutputStream(filepath);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                //out.write(tempBuf, 0, size);
//                out.flush();
//                out.close1();
//                out = null;
//            }
//
//
//        } catch (FileNotFoundException e) {
////            throw new InnerException(e.getLocalizedMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
////            throw new InnerException(e.getLocalizedMessage());
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                try {
//                    out.close1();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    //格式化文件大小
    public static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    public static void createFileWithByte(byte[] bytes) {
        fileName = "byte_to_file.txt";
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        File file = new File(Environment.getExternalStorageDirectory(),
                fileName);
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    //删除指定txt文件   通过路径
    public void deleteFile(String filePath, String fileName) {
        File f = new File(filePath + fileName);  // 输入要删除的文件位置
        if (f.exists()) {
            f.delete();
        }

    }

    //读取指定目录下的所有TXT文件的文件名
    private String getFileName(File[] files) {
        String str = "";
        if (files != null) { // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                    Log.i("zeng", "若是文件目录。继续读1"
                            + file.getName() + file.getPath());
                    getFileName(file.listFiles());
                    Log.i("zeng", "若是文件目录。继续读2"
                            + file.getName() + file.getPath());
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        String s = fileName.substring(0, fileName.lastIndexOf("."));
                        Log.i("zeng", "文件名txt：：   " + s);
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    }
                }
            }

        }
        return str;
    }

    // 获取当前目录下所有的txt文件名
    public static String getStrFileName(String fileAbsolutePath) {
        List<String> fileNameList = new ArrayList<>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        String fileString = "";
        if (subFile != null) {

            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    String filePath = subFile[iFileLength].getPath();
                    String fileSize = FileUtils.getAutoFileOrFilesSize(filePath);
                    // 判断是否为MP4结尾
                    if (filename.trim().toLowerCase().endsWith(".txt")) {
//                        VersionFile versionFile1 = new VersionFile();
//                        versionFile1.setFileName(filename);
//                        versionFile1.setFilePath(filePath);
//                        versionFile1.setSizeString(fileSize);
//                        versionFile.add(versionFile1);
                        fileNameList.add(filename);
                    }
                }
            }
            if (fileNameList.size() > 0) {
                fileString = fileNameList.get(fileNameList.size() - 1);
            } else {
                fileString = "log.txt";
            }
        } else {
            fileString = "log.txt";
        }
        return fileString;
    }

    public static Bitmap getBitmap(String path) throws IOException{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    public static String getFileName(String pathandname) {
        /**
         * * 仅保留文件名不保留后缀
         * */
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        }
        else {
            return null;
        }
    }
    /**
     * * 保留文件名及后缀
     * */
    public static String getFileNameWithSuffix(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }


}
