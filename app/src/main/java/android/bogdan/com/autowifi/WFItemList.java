package android.bogdan.com.autowifi;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;

import java.util.ArrayList;

public class WFItemList{
    private Context mContext;
    private ArrayList<WFItem> mNetworks;

    WifiManager wifi;

    public WFItemList(Activity activity) {
        mContext = activity;
    }

    public ArrayList<WFItem> getNetworks() {
        wifi = (WifiManager)  mContext.getSystemService(Context.WIFI_SERVICE);
        mNetworks = new ArrayList<>();

        if (wifi.isWifiEnabled()) {
            mNetworks.add(new WFItem(wifi.getConnectionInfo().getSSID(), wifi.getConnectionInfo().getBSSID(), false));
        }
        return mNetworks;
    }
}
