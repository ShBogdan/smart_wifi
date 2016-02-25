package android.bogdan.com.autowifi;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class FrWiFis extends Fragment {
    WiFiListAdapter mAdapter;
    ArrayList<WFItem> mNetworks;

    ListView mListView;
    Button add;
    EditText name, wifiid;

    LinearLayout mBtnOptions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_wifis, null);
        mNetworks = new WFItemList(getActivity()).getNetworks();
        mListView = (ListView) view.findViewById(R.id.listView);

        mAdapter = new WiFiListAdapter(getActivity(), mNetworks);
        mListView.setAdapter(mAdapter);
///
        add = (Button) view.findViewById(R.id.btn_add_wifi);
        name = (EditText) view.findViewById(R.id.editTextName);
        wifiid = (EditText) view.findViewById(R.id.editTextID);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DbHelper(getActivity()).addWiFi(new WFItem(null, name.getText().toString(), new ArrayList<String>()));
            }
        });
///


        mBtnOptions = (LinearLayout) view.findViewById(R.id.btn_options);
        mBtnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fTrans = getActivity().getFragmentManager().beginTransaction();
                Log.d("Click", "Options");
                switch (v.getId()) {
                    case R.id.btn_options:
                        fTrans.replace(R.id.fragment_container, new FrOptions());
                        fTrans.addToBackStack(null).commit();
                        break;
                }
            }
        });

        return view;
    }
}