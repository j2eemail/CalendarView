package com.lxs.calendar;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Description：
 * User: lixishuang
 * Date: 2016-07-05
 * Time: 17:47
 */
public class GlobalVariable {

    public static final GlobalVariable globalVariable = new GlobalVariable();

    public GlobalVariable() {

    }

    public static final GlobalVariable getInstance() {
        return globalVariable;
    }

    private Context context;
    public int onScreenWidth;    // 屏幕宽
    public int onScreenHeight;    // 屏幕高
    public float onDensity;
    public float onScreenDensity;

    public void init(Context context) {
        this.context = context;
        getScreenData(context);
    }

    public void getScreenData(Context context) {
        // 获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        onScreenWidth = dm.widthPixels;
        onScreenHeight = dm.heightPixels;
        onDensity = dm.density;
        onScreenDensity = dm.scaledDensity;
    }
}
