package android.bogdan.com.autowifi;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FrOptions extends Fragment {
    final String LOG_TAG = "FI_WI_Log";
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_ACTIVE_APP = "mysettings";
    public static final String APP_PREFERENCES_SCAN = "scan_frequency";
    public static final String APP_PREFERENCES_NOTIFICATION = "notification";
    public static final String APP_PREFERENCES_REMEMBER_WIFI = "remember_wifi";
    public static final String APP_PREFERENCES_SCHEDULE = "schedule";
    private SharedPreferences mSettings;
    private Integer mFrequency;
    boolean bound = false;
    CheckBox mCbEnable, mCbRemember, mCbNotifications;
    TextView mTvFrequency, mTvEnableTime, mTvDisableTime;
    LinearLayout mBtnToWiFiFrg, mBtnCheckFrequency, mBtnWiFiAuto;
    ServiceConnection mServiceConnection;
    ServiceMonitor mServiceMonitor;
    Intent mIntentService;
    Dialog mDialogSchedule, mDialogTimePikerOff, mDialogTimePikerOn;
    TimePicker mTimePickerOff, mTimePickerOn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_options, null);

        mTvFrequency = (TextView) view.findViewById(R.id.tv_time);
        mTvEnableTime = (TextView) view.findViewById(R.id.tv_enable_time);
        mTvDisableTime = (TextView) view.findViewById(R.id.tv_disable_time);

        mCbEnable = (CheckBox) view.findViewById(R.id.cb_enable_wifi);
        mCbRemember = (CheckBox) view.findViewById(R.id.cb_remember);
        mCbNotifications = (CheckBox) view.findViewById(R.id.cb_notifications);

        mCbEnable.setOnCheckedChangeListener(mCbListener());
        mCbRemember.setOnCheckedChangeListener(mCbListener());
        mCbNotifications.setOnCheckedChangeListener(mCbListener());

        mBtnCheckFrequency = (LinearLayout) view.findViewById(R.id.btn_check_frequency);
        mBtnWiFiAuto = (LinearLayout) view.findViewById(R.id.btn_wifi_schedule);
        mBtnToWiFiFrg = (LinearLayout) view.findViewById(R.id.btn_wifis);

        mBtnCheckFrequency.setOnClickListener(onClick());
        mBtnWiFiAuto.setOnClickListener(onClick());
        mBtnToWiFiFrg.setOnClickListener(onClick());

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        mIntentService = new Intent(getActivity(), ServiceMonitor.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                mServiceMonitor = ((ServiceMonitor.GetService) service).getSM();
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };


        return view;
    }

    public CompoundButton.OnCheckedChangeListener mCbListener() {
        CompoundButton.OnCheckedChangeListener cbL = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {

                    case R.id.cb_enable_wifi:
                        if (mCbEnable.isChecked()) {
                            getActivity().startService(new Intent(getActivity(), ServiceMonitor.class));
                        } else {
                            getActivity().stopService(new Intent(getActivity(), ServiceMonitor.class));
                        }
                        break;

                    case R.id.cb_remember:
                        if (mCbRemember.isChecked()) {
                            Toast.makeText(getActivity(), "Enable1", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Disable", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.cb_notifications:
                        if (mCbNotifications.isChecked()) {
                            Toast.makeText(getActivity(), "Enable2", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Disable", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        return cbL;
    }

    OnClickListener onClick() {
        OnClickListener onClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.btn_wifis:
                        FragmentTransaction fTrans = getActivity().getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.fragment_container, new FrWiFis());
                        fTrans.addToBackStack(null).commit();
                        break;

                    case R.id.btn_check_frequency:
                        Toast.makeText(getActivity(), "Frequency", Toast.LENGTH_SHORT).show();
                        setCheckFrequency();
                        break;

                    case R.id.btn_wifi_schedule:
                        Toast.makeText(getActivity(), "Torn on off", Toast.LENGTH_SHORT).show();
                        setScheduleTimeOn();
                        break;



                    /*Dialog frequency*/
                    case R.id.tv_1:
                        Toast.makeText(getActivity(), "Min 1", Toast.LENGTH_SHORT).show();
                        mTvFrequency.setText("1 min");
                        mFrequency = 1;
                        mDialogSchedule.dismiss();
                        break;

                    case R.id.tv_5:
                        Toast.makeText(getActivity(), "Min 5", Toast.LENGTH_SHORT).show();
                        mTvFrequency.setText("5 min");
                        mFrequency = 5;
                        mDialogSchedule.dismiss();
                        break;

                    case R.id.tv_10:
                        Toast.makeText(getActivity(), "Min 10", Toast.LENGTH_SHORT).show();
                        mTvFrequency.setText("10 min");
                        mFrequency = 10;
                        mDialogSchedule.dismiss();
                        break;

                    case R.id.tv_20:
                        Toast.makeText(getActivity(), "Min 20", Toast.LENGTH_SHORT).show();
                        mTvFrequency.setText("20 min");
                        mFrequency = 20;
                        mDialogSchedule.dismiss();
                        break;

                    case R.id.tv_30:
                        Toast.makeText(getActivity(), "Min 30", Toast.LENGTH_SHORT).show();
                        mTvFrequency.setText("30 min");
                        mFrequency = 30;
                        mDialogSchedule.dismiss();
                        break;

                    case R.id.tv_60:
                        Toast.makeText(getActivity(), "Min 60", Toast.LENGTH_SHORT).show();
                        mTvFrequency.setText("60 min");
                        mFrequency = 60;
                        mDialogSchedule.dismiss();
                        break;

                }

            }
        };
        return onClick;
    }

    void setCheckFrequency() {

        LinearLayout btn1, btn5, btn10, btn20, btn30, btn60;
        mDialogSchedule = new Dialog(getActivity());
        mDialogSchedule.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogSchedule.setContentView(R.layout.dialog);
        btn1 = (LinearLayout) mDialogSchedule.findViewById(R.id.tv_1);
        btn5 = (LinearLayout) mDialogSchedule.findViewById(R.id.tv_5);
        btn10 = (LinearLayout) mDialogSchedule.findViewById(R.id.tv_10);
        btn20 = (LinearLayout) mDialogSchedule.findViewById(R.id.tv_20);
        btn30 = (LinearLayout) mDialogSchedule.findViewById(R.id.tv_30);
        btn60 = (LinearLayout) mDialogSchedule.findViewById(R.id.tv_60);

        btn1.setOnClickListener(this.onClick());
        btn5.setOnClickListener(this.onClick());
        btn10.setOnClickListener(this.onClick());
        btn20.setOnClickListener(this.onClick());
        btn30.setOnClickListener(this.onClick());
        btn60.setOnClickListener(this.onClick());

        mDialogSchedule.show();
    }

    void setScheduleTimeOn() {
        mDialogTimePikerOff = new Dialog(getActivity());
        mDialogTimePikerOff.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogTimePikerOff.setContentView(R.layout.time_picker_off);
        mDialogTimePikerOff.show();

        Button btnSetTime = (Button) mDialogTimePikerOff.findViewById(R.id.btn_set_time);
        mTimePickerOff = (TimePicker) mDialogTimePikerOff.findViewById(R.id.timePicker);
        mTimePickerOff.setIs24HourView(true);

        btnSetTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String minutes = (mTimePickerOff.getCurrentHour() < 10) ? String.valueOf(0) + mTimePickerOff.getCurrentHour() : String.valueOf(mTimePickerOff.getCurrentHour());
                String hours = (mTimePickerOff.getCurrentMinute() < 10) ? String.valueOf(0) + mTimePickerOff.getCurrentMinute() : String.valueOf(mTimePickerOff.getCurrentMinute());
                mTvDisableTime.setText(minutes + ":" + hours);
                mDialogTimePikerOff.dismiss();
                Log.d("MyLog", "dismiss");


                mDialogTimePikerOn = new Dialog(getActivity());
                mDialogTimePikerOn.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogTimePikerOn.setContentView(R.layout.time_picker_on);
                mDialogTimePikerOn.show();

                Button btnSetTime = (Button) mDialogTimePikerOn.findViewById(R.id.btn_set_time);
                mTimePickerOn = (TimePicker) mDialogTimePikerOn.findViewById(R.id.timePicker);
                mTimePickerOn.setIs24HourView(true);

                btnSetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String minutes = (mTimePickerOn.getCurrentHour() < 10) ? String.valueOf(0) + mTimePickerOn.getCurrentHour() : String.valueOf(mTimePickerOn.getCurrentHour());
                        String hours = (mTimePickerOn.getCurrentMinute() < 10) ? String.valueOf(0) + mTimePickerOn.getCurrentMinute() : String.valueOf(mTimePickerOn.getCurrentMinute());
                        mTvEnableTime.setText(minutes + ":" + hours);
                        mDialogTimePikerOn.dismiss();


                    }
                });
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
//        SharedPreferences.Editor editor = mSettings.edit();
//        editor.putBoolean(APP_PREFERENCES_ACTIVE_APP, mCounter);
//        editor.putInt(APP_PREFERENCES_SCAN, mCounter);
//        editor.putBoolean(APP_PREFERENCES_NOTIFICATION, mCounter);
//        editor.putBoolean(APP_PREFERENCES_REMEMBER_WIFI, mCounter);
//        editor.putInt(APP_PREFERENCES_SCHEDULE, mCounter);
//        editor.apply();
    }


}
