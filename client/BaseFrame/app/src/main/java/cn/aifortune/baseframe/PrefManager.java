package cn.aifortune.baseframe;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liaohh on 17-3-17.
 */

public class PrefManager {
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;

    int MODE = 0;

    private static final  String NAME="intro_slide";
    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    public PrefManager(Context context) {
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences(NAME, MODE);
        mEditor = mPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        mEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        mEditor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return mPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
