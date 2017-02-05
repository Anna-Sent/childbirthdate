package com.anna.sent.soft.childbirthdate.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;

public class LongPressedButton extends Button {
    private Listener mListener = null;

    public LongPressedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LongPressedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongPressedButton(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cancelLongpressIfRequired(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        cancelLongpressIfRequired(event);
        return super.onTrackballEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                || (keyCode == KeyEvent.KEYCODE_ENTER)) {
            cancelLongpress();
        }

        return super.onKeyUp(keyCode, event);
    }

    private void cancelLongpressIfRequired(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_CANCEL)
                || (event.getAction() == MotionEvent.ACTION_UP)) {
            cancelLongpress();
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private void cancelLongpress() {
        if (mListener != null) {
            mListener.cancelLongPress();
        }
    }

    public interface Listener {
        void cancelLongPress();
    }
}
