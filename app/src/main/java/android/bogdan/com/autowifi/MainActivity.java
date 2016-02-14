package android.bogdan.com.autowifi;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Fragment mFOptions, mFWiFi;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFOptions = new FrOptions();
        mFWiFi = new FrWiFis();

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mFOptions);
        mFragmentTransaction.commit();

    }

}
