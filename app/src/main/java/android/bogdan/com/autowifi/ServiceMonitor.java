package android.bogdan.com.autowifi;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ServiceMonitor extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        /*Start service*/
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
//                    Toast.makeText(getActivity(), wifi.getConnectionInfo().toString() ,  Toast.LENGTH_SHORT).show();
        mStatusChecker.run();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
           /*stop service*/
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
//        убиваем тред
        mHandler.removeCallbacksAndMessages(null);
    }


    int mInterval = 5000;
    Handler mHandler = new Handler();
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            /*do something*/
            Log.d("MyService", "Service");
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void setInterval(Integer i){
        this.mInterval = i;
    }

    class GetService extends Binder{
        ServiceMonitor getSM(){
            return ServiceMonitor.this;
        }
    }


}
