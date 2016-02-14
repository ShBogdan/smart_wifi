package android.bogdan.com.autowifi;

public class WFItem {
    private String mName;
    private String mDescription;
    private boolean mCbStatus;

    public WFItem(String mName, String mDescription, boolean mCbStatus) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mCbStatus = mCbStatus;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isCbStatus() {
        return mCbStatus;
    }
}
