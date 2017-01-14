package br.ufc.ec.pet.bustracker.trackerapp;

import android.widget.ScrollView;

/**
 * Created by santana on 14/01/17.
 */
public interface ScrollViewListener {
    void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy);
    void onEndScroll(ScrollViewExt scrollView);
}