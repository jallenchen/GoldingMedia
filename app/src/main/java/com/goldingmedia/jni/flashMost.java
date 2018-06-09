package com.goldingmedia.jni;

/**
 * Created by Jallen on 2018/6/8 0008 19:06.
 */
public class flashMost {
    static {
        try {
            System.loadLibrary("flashing81118_jni");
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    //控制LED上电
    public native static int init();
    // 控制LED下电
    public native static int flash();
    //控制LED上电
    public native static int readFWVersion();
    // 控制LED下电
    public native static int verifyCS();
    //控制LED上电
    public native static int readCSVersion();
}
