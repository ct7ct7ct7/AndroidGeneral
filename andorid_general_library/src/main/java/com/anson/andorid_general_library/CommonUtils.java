package com.anson.andorid_general_library;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Anson on 2015/3/25.
 */
public class CommonUtils {

    /**
     * 隨機產生2~4位 亂數字串
     */
    public static String getRandomStr() {
        int z;
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < 2; i++) {
            z = (int) ((Math.random() * 7) % 3);

            if (z == 1) { // 放數字
                sb.append((int) ((Math.random() * 10) + 48));
            } else if (z == 2) { // 放大寫英文
                sb.append((char) (((Math.random() * 26) + 65)));
            } else {// 放小寫英文
                sb.append(((char) ((Math.random() * 26) + 97)));
            }
        }
        return sb.toString();
    }


    /**
     * 取得版本名稱
     *
     * @param context 目標對象
     * @return 版本名稱
     */
    public static String getVersionName(Context context) {
        String verName;
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            verName = "";
        }
        return verName;
    }

    /**
     * 取得版本號
     *
     * @param context 目標對象
     * @return 版本號
     */
    public static int getVersionCode(Context context) {
        int verCode;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            verCode = 0;
        }
        return verCode;
    }

    /**
     * 檢查 intent 是否有對應的 App 可以處理
     *
     * @param intent 即將使用的 intent
     * @return true if exist App can handle it
     */
    public static boolean checkIntent(Intent intent, Context c) {
        List<ResolveInfo> resolveInfo = c.getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }


    /**
     * 檢查是否有網路
     *
     * @param context 目標對象
     * @return true if has network
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    /**編碼*/
    public static String base64Encode(String src) {
        return new String(Base64.encode(src.getBytes(), Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    /**解碼*/
    public static String base64Decode(String src) {
        return new String(Base64.decode(src.getBytes(), Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    /**
     * 計算經緯度距離
     *
     * @param lat1 座標(1)緯度
     * @param lng1 座標(1)經度
     * @param lat2 座標(2)緯度
     * @param lng2 座標(2)經度
     * @return 兩點距離(km)
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        float[] results = new float[1];
        try {
            Location.distanceBetween(lat1, lng1, lat2, lng2, results);
            results[0] = results[0] / 1000;
        } catch (IllegalArgumentException e) {
            results[0] = 0;
        }
        String distance = String.valueOf(results[0]);
        if (!distance.equals("0")) {
            int decimal = distance.indexOf(".");
            if (decimal != -1) {
                if (distance.substring(decimal).length() >= 2)
                    distance = distance.substring(0, decimal) + distance.substring(decimal, decimal + 2);
            }
        }
        return Double.parseDouble(distance);
    }

    /**
     * 取得目前正在運行的Activity名稱<br>
     * 需宣告權限< uses-permission android:name="android.permission.GET_TASKS" />
     */
    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    /**
     * 加上數字小逗點
     */
    public static String numberPointAdd(String number) {
        if (number.length() >= 3) {
            int a = Integer.parseInt(number);
            DecimalFormat decimal = new DecimalFormat("#,000");
            return String.valueOf(decimal.format(a));
        } else {
            return number;
        }
    }

    /**
     * 取得裝置IMEI
     */
    public static String getIMEI(Context context) {
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.getDeviceId();
    }

    /**
     * 取得裝置AndroidID
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 取得裝置 WiFi MAC address
     */
//    public static String getMacAddress(Context context) {
//        WifiManager mgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        return mgr.getConnectionInfo().getMacAddress();
//    }


    /**
     * 取得 App PackageName
     */
    public static String getPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }


    /**
     * Covert dp to px
     *
     * @param dp
     * @param context
     * @return pixel
     */
    public static int convertDpToPixel(float dp, Context context) {
        float px = dp * getDensity(context);
        return (int) px;
    }

    /**
     * Covert px to dp
     *
     * @param px
     * @param context
     * @return dp
     */
    public static int convertPixelToDp(float px, Context context) {
        float dp = px / getDensity(context);
        return (int) dp;
    }

    /**
     * 120dpi = 0.75
     * 160dpi = 1 (default)
     * 240dpi = 1.5
     *
     * @param context
     * @return
     */
    private static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    /**
     * 取得螢幕寬(px)
     */
    public static int getScreenWidthPixels(Context context) {
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        return displaymetrics.widthPixels;
    }

    /**
     * 取得螢幕高(px)
     */
    public static int getScreenHeightPixels(Context context) {
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        return displaymetrics.heightPixels;
    }


    /**
     * 取得裝置 Bitmap 轉 File SD卡
     */
    public static File bitmapToCacheFile(Bitmap bitmap, Context context, String filename) throws IOException {
        File file = new File(context.getCacheDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
        fos.flush();
        fos.close();
        return file;
    }


    /**
     * 取得最佳化後的Bitmap
     */
    public static Bitmap getBitmap(Context context, Uri uri, int reqWidth, int reqHeight) {

        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 取得最佳化後的Bitmap
     */
    public static Bitmap getBitmap(String filePath, int reqHeight, int reqWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 取得FB需要用到的 hashKey
     */
    public static String getKeyHash(Context context) {
        PackageInfo info;
        String hashKey = null;
        try {
            info = context.getPackageManager().getPackageInfo(getPackageName(context), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = new String(Base64.encode(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashKey;
    }
}

