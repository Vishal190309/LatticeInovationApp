package com.latticeInnovations.app.data;

import android.content.Context;

import com.latticeInnovations.app.R;

public class ResourceProvider {
    private Context mContext;

    public ResourceProvider(Context mContext) {
        this.mContext = mContext;
    }

    public String getString(int resId) {
        return mContext.getString(resId);
    }
}
