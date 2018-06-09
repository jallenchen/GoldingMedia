//
// Created by Administrator on 2018/6/4 0004.
//

#include<stdio.h>
#include<stdlib.h>
#include<fcntl.h>
#include<errno.h>
#include<unistd.h>
#include<sys/ioctl.h>
#include<jni.h>  // 一定要包含此文件
#include<string.h>
#include<sys/types.h>
#include<sys/stat.h>
#include <android/log.h>
//驱动里的命令码.
#define CMD_FLAG 'i'
#define USB_POWER_ON      _IOR(CMD_FLAG,0x00000001,__u32)
#define USB_POWER_OFF     _IOR(CMD_FLAG,0x00000000,__u32)

#define DEVICE_NAME "/dev/usbpowergpio"
int fd;


static const char *TAG="012";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

/* * Class:     Linuxc
* Method:    openled
* Signature: ()I
*/

JNIEXPORT void JNICALL Java_com_goldingmedia_jni_GPIOUsbPower_usbPowerOn(JNIEnv* env, jclass mc)
{
  LOGI("POWER ON BY LARSON");
  LOGI("USB_POWER_ON:%d   USB_POWER_OFF:%d",USB_POWER_ON,USB_POWER_OFF);
  fd=open(DEVICE_NAME,O_RDWR);
  if(fd<0)
      {
            LOGI("don't open dev");
        }
        else
            {
            ioctl(fd,USB_POWER_ON,NULL) ;
            LOGI("open success");
            }


}

/* * Class:     Linuxc
* Method:    clsoeled
* Signature: ()V
*/
JNIEXPORT void JNICALL Java_com_goldingmedia_jni_GPIOUsbPower_usbPowerOff(JNIEnv* env, jclass mc)
{
    LOGI("POWER Off BY LARSON");
    ioctl(fd,USB_POWER_OFF,NULL) ;
    close(fd);

}