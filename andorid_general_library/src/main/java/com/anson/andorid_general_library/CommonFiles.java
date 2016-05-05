package com.anson.andorid_general_library;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Anson on 2015/3/25.
 */
public class CommonFiles {
    /** SD卡位址 */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();


    /**
     * 將檔案儲存份到SD卡
     * @param TAG 標籤
     * @param folder 資料夾
     * @param fileName 檔案名稱
     * @param fileContent 檔案內容
     * @return 檔案名稱
     */
    public static String saveContentToSdcard(String TAG,String folder, String fileName, String fileContent) {
        try {
            String status = Environment.getExternalStorageState();
            // 判斷SD卡是否存在
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                File destDir = new File(SDCARD_PATH + "/" + folder);
                if (!destDir.exists()) {
                    // 創建文件夾
                    destDir.mkdirs();
                }
                File saveFile = new File(SDCARD_PATH + "/" + folder, fileName + ".txt");
                FileOutputStream outStream = new FileOutputStream(saveFile);
                outStream.write(fileContent.getBytes());
                outStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return fileName;
    }

    /**
     * 取得檔案內容
     * @param TAG 標籤
     * @param file 檔案本身
     * @return 檔案文字內容
     */
    public static String getFileContent(String TAG, File file) {
        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = br.readLine();
            while (temp!=null){
                sb.append(temp+"\n");
                temp=br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return sb.toString();
    }
}
