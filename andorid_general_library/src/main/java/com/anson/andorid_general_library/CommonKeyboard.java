package com.anson.andorid_general_library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Anson on 2015/3/25.
 */
public class CommonKeyboard {

    /**顯示鍵盤*/
    public static void showKeyBoard(final Context context,final View view){
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 300);
    }

    /**隱藏鍵盤*/
    public static void hideKeyBoard(Activity activity){
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**隱藏鍵盤*/
    public static void hideKeyBoard(Activity activity,View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    /**設定Focus & 鍵盤*/
    @SuppressLint("Recycle")
    public static void setRequestFocus(final Context context , final EditText et){
        et.requestFocus();
        et.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        et.setSelection(et.getText().length());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
            }
        });
    }

}
