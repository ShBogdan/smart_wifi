package android.bogdan.com.autowifi;

import java.util.ArrayList;

public class WFItem {
    int mId;
    private String mName;
    private ArrayList<String> mCells;


    public WFItem() {}

    public WFItem(Integer mId, String mName, ArrayList<String> cells) {
        this.mId = mId;
        this.mName  = mName;
        this.mCells = cells;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<String> getCells() {
        return mCells;
    }

    public void setCells(ArrayList<String> cells) {
        mCells = cells;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
