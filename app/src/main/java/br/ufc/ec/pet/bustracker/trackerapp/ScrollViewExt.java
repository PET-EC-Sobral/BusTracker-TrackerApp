package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by santana on 14/01/17.
 */
public class ScrollViewExt extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public ScrollViewExt(Context context) {
        super(context);
    }

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }

        // We take the last son in the scrollview
        View view =  this.getChildAt(this.getChildCount() - 1);
        int diff = (view.getBottom() - (this.getHeight() + this.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            scrollViewListener.onEndScroll(this);
        }

    }
}
