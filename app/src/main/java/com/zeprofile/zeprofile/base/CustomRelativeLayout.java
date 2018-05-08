package com.zeprofile.zeprofile.base;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {

    private IMovementListener mMovementListener;

    public CustomRelativeLayout(Context context) {
        super(context);
        this.initMembers();
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initMembers();
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initMembers();
    }

    private void initMembers() {
        this.mMovementListener = new SimpleMovementListener();
    }

    public float getXFraction() {
        final WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (width == 0) ? 0 : getX() / (float) width;
    }

    public void setXFraction(float xFraction) {
        final WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        this.mMovementListener.onSetXFraction(xFraction);
        setX((width > 0) ? (xFraction * width) : 0);
    }

    public float getYFraction() {
        final WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return (height == 0) ? 0 : getY() / (float) height;
    }

    public void setYFraction(float yFraction) {
        final WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        this.mMovementListener.onSetYFraction(yFraction);
        setY((height > 0) ? (yFraction * height) : 0);
    }

    public interface IMovementListener {
        public void onSetXFraction(float pXFraction);

        public void onSetYFraction(float pYFraction);
    }

    private class SimpleMovementListener implements IMovementListener {

        @Override
        public void onSetXFraction(float pXFraction) {

        }

        @Override
        public void onSetYFraction(float pYFraction) {

        }
    }
}