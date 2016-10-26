package com.maker.use.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
//            bitmap = compressImage(bitmap);
            bitmap = imageZoom(bitmap);
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


    public static boolean writeFile(File file, String path) {
        boolean result = true;
        try {
            FileOutputStream fout = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            //压缩图片
//            bitmap = compressImage(bitmap);
            bitmap = imageZoom(bitmap);
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


    /**
     * 压缩图片大小（通过压缩质量）
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 40) {  //循环判断如果压缩后图片是否大于50kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩图片（通过压缩宽高）
     */
    private static Bitmap imageZoom(Bitmap bitmap) {
        //图片允许最大空间   单位：KB
        double maxSize = 500.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));

            return bitmap;
        }
        return bitmap;
    }


    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
