package com.maker.use.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
//    public final static String HEAD_PATH = Environment.getExternalStorageDirectory() + "/UsedMarket/head";
//    public final static String USEDMARKET_PATH = Environment.getExternalStorageDirectory() + "/UsedMarket";

    public final static String HEAD_PATH = UIUtils.getContext().getFilesDir() + "/UsedMarket/head";
    public final static String IMG_PATH = UIUtils.getContext().getFilesDir() + "/UsedMarket/img";
    public final static String USEDMARKET_PATH = UIUtils.getContext().getFilesDir() + "/UsedMarket";

    /**
     * 创建一个以userId为文件名的文件，保存用户头像。文件路径为"/sdcard/woliao/selfId/friendId.jpg"
     *
     * @param selfId
     * @param friendId
     * @return
     */
    public static File createFile(String selfId, String friendId) {
        String filePath = UIUtils.getContext().getFilesDir() + "/UsedMarket/" + selfId;
        File fileParent = new File(filePath);
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        File file = new File(filePath + "/" + friendId + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 根据fileType,创建普通的jpg或3gp文件来保存图片或语音
     *
     * @param selfId
     * @param fileType
     * @return
     */
    public static File createFile(String selfId, int fileType) {
        String nowTime = TimeUtil.getAbsoluteTime();
        String filePath = UIUtils.getContext().getFilesDir() + "/UsedMarket/" + selfId;
        File fileParent = new File(filePath);
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        File file = null;
        if (fileType == Config.MESSAGE_TYPE_IMG) {
            file = new File(filePath + "/" + nowTime + ".jpg");
        } else if (fileType == Config.MESSAGE_TYPE_AUDIO) {
            file = new File(filePath + "/" + nowTime + ".3gp");
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static boolean writeFile(ContentResolver cr, File file, Uri uri) {
        boolean result = true;
        try {
            FileOutputStream fout = new FileOutputStream(file);
            Log.i("FileUtil", "fout=" + fout);
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            Log.i("FileUtil", "bitmap=" + bitmap);
            //压缩图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);

            try {
                fout.flush();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (Exception e) {
            Log.e("FileUtil", "exception=" + e.toString());
        }

        return result;
    }

    //根据用户ID,创建一个以该ID为文件名的jpg图片
    public static File createHeadFile(String userId) {
        File fileParent = new File(HEAD_PATH);
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        File file = null;
        file = new File(HEAD_PATH + "/" + userId + ".jpg");
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static Bitmap getHeadFile(int userId) {
        File file = new File(HEAD_PATH + "/" + userId + ".jpg");
        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(HEAD_PATH + "/" + userId + ".jpg");
        return bitmap;
    }


    /**
     * 读出图片的kb大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static boolean writeFile(File file, String path) {
        boolean result = true;
        try {
            FileOutputStream fout = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //压缩图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fout);

            try {
                fout.flush();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (Exception e) {
            Log.e("FileUtil", "exception=" + e.toString());
        }

        return result;
    }

    //根据传值,创建一个以该值为文件名的jpg图片
    public static File createImgFile(String filename) {
        File fileParent = new File(IMG_PATH);
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        File file = null;
        file = new File(IMG_PATH + "/" + filename + ".jpg");
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
