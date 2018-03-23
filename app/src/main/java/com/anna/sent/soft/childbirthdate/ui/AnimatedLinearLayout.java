package com.anna.sent.soft.childbirthdate.ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class AnimatedLinearLayout extends LinearLayout {
    private Animation mInAnimation;
    private Animation mOutAnimation;

    public AnimatedLinearLayout(Context context) {
        super(context);
    }

    public AnimatedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    private int getHeightForAnimation() {
        int measureSpec = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 ? 0
                : MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        measure(measureSpec, measureSpec);
        return getMeasuredHeight();
    }

    @SuppressWarnings("unused")
    public void show() {
        if (!isVisible()) {
            show(true);
        }
    }

    public void show(boolean withAnimation) {
        if (withAnimation) {
            mInAnimation = new DropDownAnimation(this, getHeightForAnimation(),
                    true);
            startAnimation(mInAnimation);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        if (mInAnimation != null) {
            setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings("unused")
    public void hide() {
        if (isVisible()) {
            hide(true);
        }
    }

    public void hide(boolean withAnimation) {
        if (withAnimation) {
            mOutAnimation = new DropDownAnimation(this,
                    getHeightForAnimation(), false);
            startAnimation(mOutAnimation);
        } else {
            setVisibility(View.GONE);
        }
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if (mOutAnimation != null) {
            setVisibility(View.GONE);
            mOutAnimation = null;
        }

        if (mInAnimation != null) {
            mInAnimation = null;
        }
    }

    private boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    public class DropDownAnimation extends Animation {
        final int targetHeight;
        final View view;
        final boolean down;

        public DropDownAnimation(View view, int targetHeight, boolean down) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.down = down;
            setDuration(500);
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            int newHeight;
            if (down) {
                newHeight = (int) (targetHeight * interpolatedTime);
            } else {
                newHeight = (int) (targetHeight * (1 - interpolatedTime));
            }

            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
