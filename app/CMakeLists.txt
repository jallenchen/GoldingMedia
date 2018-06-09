cmake_minimum_required(VERSION 3.4.1)

add_library( usbpowercontrol
             SHARED
             src/main/jni/usbpowercontrol.c )

target_link_libraries(usbpowercontrol log android)