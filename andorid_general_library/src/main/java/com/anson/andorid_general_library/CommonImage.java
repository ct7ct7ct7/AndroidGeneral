package com.anson.andorid_general_library;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Anson on 2015/3/26.
 */
public class CommonImage {

    /**
     * 取得Padding 圖片
     */
    public static Bitmap getPaddingBitmap(Bitmap src, int paddingWidth, int paddingHeight){
        Bitmap outPutBitmap = Bitmap.createBitmap(src.getWidth() + (paddingWidth*2),src.getHeight() + (paddingHeight*2), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(outPutBitmap);
        can.drawARGB(0,255,255,255);
        can.drawBitmap(src, paddingWidth, paddingHeight, null);
        src.recycle();
        return outPutBitmap;
    }


    /** 取得網路上圖片
     * @param url 網址
     * @return 圖片
     */
    public static Bitmap getBitmapFromUrl(String url) {
        URL imageUrl = null;
        Bitmap bitmap = null;
        try {
            imageUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * 取得SD卡內的圖片
     * @param folder 資料夾
     * @param fileName 檔案名稱
     * @param format 圖片副檔名(jpg png 等等)
     * @return 圖片
     */
    public static Bitmap getSdcardBitmap(String folder, String fileName,String format) {
        try{
            Bitmap bitmap = BitmapFactory.decodeFile(CommonFiles.SDCARD_PATH + "/" + folder + "/" + fileName + "." + format);
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 取得drawable裡的圖片
     * @param context
     * @param id Drawable路徑
     * @param w 想要顯示的寬
     * @param h 想要顯示的高
     * @return Bitmap
     */
    public static Bitmap getDrawableBitmap(Context context, int id, int w, int h) {
        Resources resources = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, id, options);
        options.inSampleSize = calculateInSampleSize(options, w, h);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, id, options);
    }

    /** 取得options的SampleSize */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 下載URL上的檔案到SD卡
     * @param url url地址
     * @param path SD卡路徑
     * @param fileName 欲取名的檔案名稱
     */
//    public static void downloadFileFromUrl(String url, String path, String fileName) throws IOException {
//        File dir = new File(path);
//        if (dir.exists() == false) {
//            dir.mkdirs();
//        }
//        URL downloadUrl = new URL(url); // you can write any link here
//        File file = new File(dir, fileName);
//        URLConnection uCon = downloadUrl.openConnection();
//        InputStream is = uCon.getInputStream();
//        BufferedInputStream bis = new BufferedInputStream(is);
//        ByteArrayBuffer baf = new ByteArrayBuffer(5000);
//        int current = 0;
//        while ((current = bis.read()) != -1) {
//            baf.append((byte) current);
//        }
//        FileOutputStream fos = new FileOutputStream(file);
//        fos.write(baf.toByteArray());
//        fos.flush();
//        fos.close();
//    }

    /**
     * 取得ZIP檔裡的圖片
     *
     * @param path     SD卡路徑
     * @param fileName 檔案名稱
     * @param w        想要顯示的寬
     * @param h        想要顯示的高
     * @return Bitmap
     */
    public static Bitmap getZipBitmap(String TAG ,String path, String fileName, int w, int h) {
        File file = new File(path);
        Bitmap result = null;
        ZipFile zip = null;
        try {
            zip = new ZipFile(file, ZipFile.OPEN_READ);
            ZipEntry entry = zip.getEntry(fileName);
            if (entry != null && !entry.isDirectory()) {
                InputStream is = null;
                InputStream readIs = null;
                is = zip.getInputStream(entry);
                readIs = zip.getInputStream(entry);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                // 讀取原圖大小
                BitmapFactory.decodeStream(readIs, null, options);
                readIs.close();
                options.inSampleSize = calculateInSampleSize(options, w, h);
                options.inJustDecodeBounds = false;

                result = BitmapFactory.decodeStream(is, null, options);
                is.close();
            } else {
                Log.e(TAG,"zip file name error");
            }
            zip.close();
        } catch (IOException e) {
            Log.e(TAG,"zip path error");
        }
        return result;
    }



    /**
     * 用 youtube 影片網址取的影片的 ID
     * @param videoUri 影片的網址，例如: https://www.youtube.com/watch?v=pm30TnTrtUU
     * @return pm30TnTrtUU
     */
    public static String getVideoId(String videoUri) {
        return Uri.parse(videoUri).getQueryParameter("v");
    }

    /**
     * 用影片的 ID 取得影片的縮圖
     * @param videoId 影片的 ID，例如: pm30TnTrtUU
     * @return http://i1.ytimg.com/vi/pm30TnTrtUU/default.jpg
     */
    public static String getImageUrl(String videoId) {
        return String.format("http://i1.ytimg.com/vi/%1$s/default.jpg",videoId);
    }
}
