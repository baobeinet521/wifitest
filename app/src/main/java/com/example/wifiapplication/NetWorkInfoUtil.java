package com.example.wifiapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;

import static android.content.Context.WIFI_SERVICE;

public class NetWorkInfoUtil {
    private WifiManager mWifiManager;

    public NetWorkInfoUtil(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WiFi是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        if (context == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接类型
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前连接WIFI的SSID
     */
    public static String getSSID(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo winfo = wm.getConnectionInfo();
            if (winfo != null) {
                String s = winfo.getSSID();
                if (s.length() > 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                    return s.substring(1, s.length() - 1);
                }
            }
        }
        return "";
    }


    public static int getWifiIp(Context context) {
        if (context == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (isWifiEnabled(context)) {
            int ipAsInt = wifiMgr.getConnectionInfo().getIpAddress();
            if (ipAsInt == 0) {
                return -1;
            } else {
                return wifiMgr.getConnectionInfo().getIpAddress();
//                return Util.intToInet(ipAsInt);
            }
        } else {
            return -1;
        }
    }

    /**
     * ip 地址转换为String
     *
     * @param i
     * @return
     */
    public static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * 获取手机热点密码
     *
     * @return
     */
    public String getWifiApSharedKey() {
        Method method = null;
        String SharedKey = null;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
            SharedKey = apConfig.preSharedKey;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SharedKey;
    }


    /**
     * 判断热点是否开启
     *
     * @param context
     * @return
     */
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        } catch (Throwable ignored) {
        }
        return false;
    }


    /**
     * 获取手机热点名称
     *
     * @param context
     * @return
     */
    public static String getApSsid(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Method method = null;
        String apSsid = null;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
            apSsid = apConfig.SSID;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apSsid;
    }

    /**
     * 获取开启热点后的IP地址
     *
     * @param context
     * @return
     */
    public static String getHotspotLocalIpAddress(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifimanager.getDhcpInfo();
        if (dhcpInfo != null) {
            int address = dhcpInfo.serverAddress;
            return ((address & 0xFF)
                    + "." + ((address >> 8) & 0xFF)
                    + "." + ((address >> 16) & 0xFF)
                    + "." + ((address >> 24) & 0xFF));
        }
        return null;
    }


    /**
     * 开启热点
     */
    public static void openHotspot(Context context, String ssid, String password) {
        closeWifi(context);
        if (isApOn(context)) {
            closeAp(context);
        }
        openAp(context, ssid, password);
    }

    /**
     * 开启热点
     *
     * @param context
     * @param SSID     热点名称
     * @param password 热点密码
     * @return
     */
    public static boolean openAp(Context context, String SSID, String password) {
        if (TextUtils.isEmpty(SSID)) {
            return false;
        }

        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if (wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(false);
        }

        WifiConfiguration wifiConfiguration = getApConfig(SSID, password);
        try {
            if (isApOn(context)) {
                wifimanager.setWifiEnabled(false);
                closeAp(context);
            }

            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wifiConfiguration, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 关闭WiFi
     *
     * @param context
     */
    public static void closeWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if (wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(false);
        }
    }

    /**
     * 设置热点
     *
     * @param SSID 热点名称
     * @param password  热点密码
     * @return
     */
    private static WifiConfiguration getApConfig(String SSID, String password) {
        if (TextUtils.isEmpty(password)) {
            return null;
        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = SSID;
        config.preSharedKey = password;
//        config.hiddenSSID = true;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        return config;
    }

    /**
     * 关闭热点
     *
     * @param context
     */
    public static void closeAp(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
