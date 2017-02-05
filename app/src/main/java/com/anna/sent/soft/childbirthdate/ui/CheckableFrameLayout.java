package com.anna.sent.soft.childbirthdate.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

import com.anna.sent.soft.childbirthdate.R;

public class CheckableFrameLayout extends FrameLayout implements Checkable {
    private boolean mChecked;

    public CheckableFrameLayout(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public CheckableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableFrameLayout(Context context) {
        super(context);
    }

    public boolean isChecked() {
        return mChecked;
    }

    @SuppressWarnings("deprecation")
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundDrawable(checked ? new ColorDrawable(getResources()
                .getColor(R.color.blue)) : null);
    }

    public void toggle() {
        setChecked(!mChecked);
    }
}
