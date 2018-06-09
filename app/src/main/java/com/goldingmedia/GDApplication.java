package com.goldingmedia;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.goldingmedia.goldingcloud.LauncherUiProtos;
import com.goldingmedia.jni.BacklightLevel;
import com.goldingmedia.mvp.mode.TruckMedia;
import com.goldingmedia.sqlite.DataSQLHelper;
import com.goldingmedia.utils.CrashHandler;
import com.goldingmedia.utils.DataUtils;
import com.goldingmedia.most.ipc.MostTcpProtoBuf;
import com.goldingmedia.utils.ProtoDataParse;
import com.goldingmedia.utils.Utils;

import aidl.IGoldingSysService;

/**
 * Created by Jallen on 2017/8/18 0018 09:37.
 */

public class GDApplication extends Application{
    public static GDApplication mInstance;
    private ProtoDataParse mProtoDataParse;
    private DataUtils mDataUtils;
    public LauncherUiProtos.CLauncherUiTemplate launcherUiTemplate;
    public TruckMedia categoryTruckMedia = new TruckMedia() ;
    private DataSQLHelper dataInsert;
    private MostTcpProtoBuf mMostTcp;
   // private BitmapCache bitmapCache;
   public IGoldingSysService IgoldingSysService;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
      //  CrashHandler.getInstance().init(mInstance);
        registerActivityLifecycleCallbacks(new ActivityLifecycleListener());


        try {
            Intent intent = new Intent();
            intent.setAction("com.goldingmedia.goldingservice.goldingservice");
            Intent eintent = new Intent(Utils.createExplicitFromImplicitIntent(this,intent));
            bindService(eintent,mServiceConnection, Service.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IgoldingSysService = IGoldingSysService.Stub.asInterface(service);
            try {
                IgoldingSysService.checkInitData();
                BacklightLevel.lcdOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.e("GDApplication", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("GDApplication", "onServiceDisconnected");
            IgoldingSysService = null;
        }
    };

    public static GDApplication getmInstance(){
        return mInstance;
    }

    public void loadLauncherUI() throws  Exception{

    }

    public IGoldingSysService getIgoldingSysService(){
        if(IgoldingSysService == null){
            Intent intent = new Intent();
            intent.setAction("com.goldingmedia.goldingservice.goldingservice");
            Intent eintent = new Intent(Utils.createExplicitFromImplicitIntent(this,intent));
            bindService(eintent,mServiceConnection, Service.BIND_AUTO_CREATE);
        }
        try {
            Thread.sleep(5000);  //wait connect finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return IgoldingSysService;
    }

    public ProtoDataParse getProtoDataParse(){
        if(mProtoDataParse == null){
            mProtoDataParse = new ProtoDataParse(mInstance);
        }
        return mProtoDataParse;
    }


    public MostTcpProtoBuf getMostTcp() {
        if(mMostTcp == null){
            mMostTcp = new MostTcpProtoBuf();
        }
        return mMostTcp;
    }
    public void setMostTcp(MostTcpProtoBuf task){
        mMostTcp = task;
    }

    public DataUtils getDataUtils(){
        if(mDataUtils == null){
            mDataUtils = new DataUtils(mInstance);
        }
        return mDataUtils;
    }

    public DataSQLHelper getDataInsert() {
        if(dataInsert == null){
            dataInsert = new DataSQLHelper(mInstance);
        }
        return dataInsert;
    }


    public void setDataUtils(DataUtils dataUtils){
        mDataUtils = dataUtils;
    }

    public TruckMedia getTruckMedia() {
        return categoryTruckMedia;
    }

    public void setTruckMedia(TruckMedia categoryTruckMedia) {
        this.categoryTruckMedia = categoryTruckMedia;
    }

    ///////////////////////////////////////////////////////////////////////////////

    // 线程同步/定时器部�?
    Handler mUiHandler = null; // 用于向界面发送执行代�?
    Handler mWorkHandler = null; // 用于向工作线程发送执行代�?
    HandlerThread mWorkThread = null; // 工作线程处理耗时操作,防止在主线程中执行界面卡

    /**
     * 获取Handler�?化跨线程执行代码,实现同步/定时器等功能 1.获取指定线程Handler 2.实现Runnable:run()代码
     * 2.调用Handler:post/postAt postDelayed传入Runnable�? 获取界面Handler
     */
    public Handler getUiHandler() {
        return (mUiHandler != null) ? (mUiHandler) : (mUiHandler = new Handler(
                getMainLooper()));
    }

    /**
     * 获取工作线程Handler
     *
     * @return
     */
    public Handler getWorkHandler() {
        return (mWorkHandler != null) ? (mWorkHandler)
                : (mWorkHandler = new Handler(getWorkLooper()));
    }

    /**
     * 获取Looper实现自定义Handler,在指定线�?(非当前线�?)处理消息 获取工作线程Looper,�?:获取主线程Looper请调�?
     * getMainLooper()
     */
    public Looper getWorkLooper() {
        if (mWorkThread == null) { // 如果工作线程未开�?,则开启工作线�?
            mWorkThread = new HandlerThread("Rtcclient_WorkThread");
            mWorkThread.start();
        }
        return mWorkThread.getLooper();
    }

    /**
     * 释放工作线程
     */
    public void clearWorkThread() {
        if (mWorkHandler != null)
            mWorkHandler = null;
        if (mWorkThread != null) {
            if (mWorkThread.isAlive()) {
                mWorkThread.quit();
                try {
                    mWorkThread.join(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mWorkThread = null;
        }
    }

    /**
     * 静�?�工具函�? 免创建Handler实现同步和定时器 直接向ui线程执行代码
     */
    static public boolean post2UIRunnable(Runnable r) {
        return (mInstance != null) ? mInstance.getUiHandler().post(r) : false;
    }

    /**
     * UI线程中的定时�?
     *
     * @param r
     * @param uptimeMillis
     * @return
     */
    static public boolean post2UIAtTime(Runnable r, long uptimeMillis) {
        return (mInstance != null) ? mInstance.getUiHandler().postAtTime(r,
                uptimeMillis) : false;
    }

    static public boolean post2UIDelayed(Runnable r, long delayMillis) {
        return (mInstance != null) ? mInstance.getUiHandler().postDelayed(r,
                delayMillis) : false;
    }

    /**
     * 直接向工作线程线程执行代�?
     *
     * @param r
     * @return
     */
    static public boolean post2WorkRunnable(Runnable r) {
        return (mInstance != null) ? mInstance.getWorkHandler().post(r)
                : false;
    }

    /**
     * 工作线程中的定时�?
     *
     * @param r
     * @param uptimeMillis
     * @return
     */
    static public boolean post2WorkAtTime(Runnable r, long uptimeMillis) {
        return (mInstance != null) ? mInstance.getWorkHandler().postAtTime(r,
                uptimeMillis) : false;
    }

    static public boolean post2WorkDelayed(Runnable r, long delayMillis) {
        return (mInstance != null) ? mInstance.getWorkHandler().postDelayed(
                r, delayMillis) : false;
    }
}
