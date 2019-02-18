package com.example.wifiapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private TextView mWifiName;
    private TextView mWifiPassword;
    private TextView mWifiIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        mWifiName = findViewById(R.id.wifi_name);
        mWifiPassword = findViewById(R.id.wifi_password);
        mWifiIp = findViewById(R.id.wifi_ip);

        NetWorkInfoUtil mNetWorkInfoUtil = new NetWorkInfoUtil(this);

        boolean netWorkConnected = NetWorkInfoUtil.isNetworkConnected(this);
        if(netWorkConnected){
            Log.d("zhengdan","******** 有网络连接netWorkConnected = " +netWorkConnected+"********");
            boolean isWifiEnable = NetWorkInfoUtil.isWifiEnabled(this);
            boolean isMobileConnected = NetWorkInfoUtil.isMobileConnected(this);
            if(isWifiEnable){
                Log.d("zhengdan","******** 连接的是WiFi isWifiEnable= " +isWifiEnable+"********");
                String name = NetWorkInfoUtil.getSSID(this);
                Log.d("zhengdan","********wifi name ssid = " +name+"********");
                mWifiName.setText(name);

                int ip = NetWorkInfoUtil.getWifiIp(this);
                String ipStr = NetWorkInfoUtil.intToIp(ip);
                Log.d("zhengdan","********wifi ip = " +ipStr+"********");
                if(!TextUtils.isEmpty(ipStr)){
                    mWifiIp.setText(ipStr);
                }
            }else if(isMobileConnected){
                Log.d("zhengdan","******** 连接的是手机网络isMobileConnected= " +isMobileConnected+"********");
                int connectType = NetWorkInfoUtil.getConnectedType(this);
                Log.d("zhengdan","******** 连接的是手机网络类型是 " +connectType+"********");
                boolean isApon = NetWorkInfoUtil.isApOn(this);
                if(isApon){
                    Log.d("zhengdan","********wifi 热点是否开启 sApon  = " +isApon+"********");
                    String ssidTest =  NetWorkInfoUtil.getApSsid(this);
                    Log.d("zhengdan","********wifi 热点 ssidTest  = " +ssidTest+"********");

                    String apIp = NetWorkInfoUtil.getHotspotLocalIpAddress(this);
                    Log.d("zhengdan","********wifi 热点 ip  = " +apIp+"********");
                    String wifiKey = mNetWorkInfoUtil.getWifiApSharedKey();
                    Log.d("zhengdan","********热点 密码 = " +wifiKey+"********");
                    if(!TextUtils.isEmpty(wifiKey)){
                        mWifiPassword.setText(wifiKey);
                    }
                }
            }
        }else{
            Log.d("zhengdan","******** 没有网络连接netWorkConnected = " +netWorkConnected+"********");
        }
    }
}
