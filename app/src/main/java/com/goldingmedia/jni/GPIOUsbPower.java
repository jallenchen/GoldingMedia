package com.goldingmedia.jni;

import com.goldingmedia.utils.NLog;

/**
 * Created by Jallen on 2018/6/4 0004 10:07.
 */
public class GPIOUsbPower {
    static {
        try {
            System.loadLibrary("usbpowercontrol");
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    //控制LED上电
    public native static void usbPowerOn();
    // 控制LED下电
    public native static void usbPowerOff();
}
