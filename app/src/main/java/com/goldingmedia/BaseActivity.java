package com.goldingmedia;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.goldingmedia.contant.Contant;
import com.goldingmedia.jni.BacklightLevel;
import com.goldingmedia.mvp.mode.EventBusCMD;
import com.goldingmedia.sqlite.DataSharePreference;
import com.goldingmedia.utils.NLog;

import java.util.Locale;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class BaseActivity extends FragmentActivity{
    private final String TAG = "BaseActivity";
    protected Context context;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = BaseActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置横屏
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        EventBus.getDefault().register(this);
        setLanguage();

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        NLog.d("BaseActivity", "mWidth="+dm.widthPixels+"..mHeight="+dm.heightPixels+".mDensity."+dm.density);
    }

    public void setLanguage(){
        String local = DataSharePreference.getLanguage(this,"local");
        if(local.equals("EN") ){
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources() .getDisplayMetrics();
            config.locale = Locale.US;
            getResources().updateConfiguration(config, dm);
        }else if(local.equals("TW") ){
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources() .getDisplayMetrics();
            config.locale = Locale.TAIWAN;
            getResources().updateConfiguration(config, dm);
        }else{
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources() .getDisplayMetrics();
            config.locale = Locale.SIMPLIFIED_CHINESE;
            getResources().updateConfiguration(config, dm);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void OnEventCmd(final  EventBusCMD cmd) {
        NLog.e(TAG,"cmd:"+cmd.getCmdId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  EventBus.getDefault().unregister(this);
        NLog.e(TAG,"onDestroy:");
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    public void getDBData(){
        GDApplication.post2WorkRunnable(new Runnable() {
            @Override
            public void run() {
                GDApplication.getmInstance().getTruckMedia().getcHotZone().setTruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_HOTZONE,1));
                GDApplication.getmInstance().getTruckMedia().getcMoviesShow().setmCategorys(GDApplication.getmInstance().getDataInsert().getCategoryData(Contant.CATEGORY_MEDIA_ID));
                GDApplication.getmInstance().getTruckMedia().getcMoviesShow().setmGOLDINGtruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MOVIESSHOW,1));
                GDApplication.getmInstance().getTruckMedia().getcMoviesShow().setmDGTVtruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MOVIESSHOW,2));
                GDApplication.getmInstance().getTruckMedia().getcMoviesShow().setmWhalestruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MOVIESSHOW,3));
                GDApplication.getmInstance().getTruckMedia().getcMoviesShow().setmSDLMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MOVIESSHOW,4));
                GDApplication.getmInstance().getTruckMedia().getcGoldingMedia().setmCategorys(GDApplication.getmInstance().getDataInsert().getCategoryData(Contant.CATEGORY_GOLDING_ID));
                GDApplication.getmInstance().getTruckMedia().getcGoldingMedia().setmJTVtruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_GOLDING,1));
                GDApplication.getmInstance().getTruckMedia().getcGoldingMedia().setmMagazinetruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_GOLDING,2));
                GDApplication.getmInstance().getTruckMedia().getcGoldingMedia().setmJMalltruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_GOLDING,3));
                GDApplication.getmInstance().getTruckMedia().getcGameCenter().setLMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_GAME,1));
                GDApplication.getmInstance().getTruckMedia().getcGameCenter().setMMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_GAME,2));
                GDApplication.getmInstance().getTruckMedia().getcGameCenter().setSMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_GAME,3));
                GDApplication.getmInstance().getTruckMedia().getCeLive().setmCategorys(GDApplication.getmInstance().getDataInsert().getCategoryData(Contant.CATEGORY_ELIVE_ID));
                GDApplication.getmInstance().getTruckMedia().getCeLive().setmMalltruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_ELIVE,1));
                GDApplication.getmInstance().getTruckMedia().getCeLive().setmHoteltruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_ELIVE,2));
                GDApplication.getmInstance().getTruckMedia().getCeLive().setmFoodtruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_ELIVE,3));
                GDApplication.getmInstance().getTruckMedia().getCeLive().setmTraveltruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_ELIVE,4));
                GDApplication.getmInstance().getTruckMedia().getcMyApp().setmCategorys(GDApplication.getmInstance().getDataInsert().getCategoryData(Contant.CATEGORY_MYAPP_ID));
                GDApplication.getmInstance().getTruckMedia().getcMyApp().seteBooktruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MYAPP,1));
                GDApplication.getmInstance().getTruckMedia().getcMyApp().setApptruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MYAPP,2));
                GDApplication.getmInstance().getTruckMedia().getcMyApp().setSettingtruckMediaNodes(GDApplication.getmInstance().getDataInsert().getMediaMetaDataList(Contant.TABLE_NAME_MYAPP,3));
                GDApplication.getmInstance().getTruckMedia().getcAds().setmCategorys(GDApplication.getmInstance().getDataInsert().getCategoryData(Contant.CATEGORY_ADS_ID));

                EventBus.getDefault().post(new EventBusCMD(Contant.MsgID.REFLESH_DATA));
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            if(BacklightLevel.lcdGet() == 0){
                BacklightLevel.lcdOn();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void checkPermission() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                }
        );
        // 如果这3个权限全都拥有, 则直接执行读取短信代码
        if (isAllGranted) {
            NLog.d("checkPermission","权限全都拥有");
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行读取短信代码
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮


            }
        }
    }
}
