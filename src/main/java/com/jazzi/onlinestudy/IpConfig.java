package com.jazzi.onlinestudy;

public class IpConfig {
//    private static final String ipAddress="http://192.168.43.101";
    private static String ipAddress="http://169.254.115.87";
    public static String getIp(){
        return ipAddress;
    }
    public static void setIP(String ip){
        ipAddress="http://"+ip;
    }
}
