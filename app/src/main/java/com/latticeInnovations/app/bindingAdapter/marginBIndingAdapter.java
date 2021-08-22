package com.latticeInnovations.app.bindingAdapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;

public class marginBIndingAdapter {

    @BindingAdapter("android:layout_marginEnd")
    public static void setBottomMargin(View view, float endMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                Math.round(endMargin),layoutParams.bottomMargin );
        view.setLayoutParams(layoutParams);
    }

}
