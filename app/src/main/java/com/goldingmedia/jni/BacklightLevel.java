package com.goldingmedia.jni;

import android.os.RemoteException;

import com.goldingmedia.GDApplication;
import com.goldingmedia.utils.NLog;
import com.goldingmedia.utils.Utils;

/**
 * Created by Jallen on 2017/12/7 0007 16:43.
 */

public class BacklightLevel {
    public static int state = 0;

    public static void lcdOn(){
        GDApplication.post2WorkRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    GDApplication.getmInstance().getIgoldingSysService().setSystembacklight(255);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                state = 1;
            }
        });
    }

    public static void lcdOff(){
        GDApplication.post2WorkRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    GDApplication.getmInstance().getIgoldingSysService().setSystembacklight(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                state = 0;
            }
        });
    }

    public static int lcdGet(){
        return state;
    }
}
