// IGoldingSysService.aidl
package aidl;

// Declare any non-default types here with import statements

interface IGoldingSysService {
  int exeCmd(String cmd);
     int installApk(String apkPath);
     boolean setSystemTime(long time);
     int updateSystem(String filePath,String md5);
     boolean checkInitData();
     boolean setSystembacklight(int level);
}
