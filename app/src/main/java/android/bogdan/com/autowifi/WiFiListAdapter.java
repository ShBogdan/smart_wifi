package android.bogdan.com.autowifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WiFiListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<WFItem> mItems;
    private WFItem mMenuItem;

    public WiFiListAdapter(Context context, ArrayList<WFItem> items) {
        mItems = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }
        mMenuItem = (WFItem) getItem(position);

        ((TextView)convertView.findViewById(R.id.tv_name)).setText(mMenuItem.getName());
        ((TextView)convertView.findViewById(R.id.tv_description)).setText(mMenuItem.getDescription());

        return convertView;
    }
}