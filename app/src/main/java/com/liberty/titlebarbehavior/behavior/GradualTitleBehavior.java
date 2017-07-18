package com.liberty.titlebarbehavior.behavior;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntRange;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by liberty on 2017/7/17.
 */

public class GradualTitleBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG="GradualBehavior";

    public GradualTitleBehavior(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof NestedScrollView)
            return true;
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int toolBarHeight=child.getHeight();
        int scrollY=target.getScrollY();
        Log.d(TAG,"scrollY="+scrollY);
        Log.d(TAG,"child.getBackground():"+(child.getBackground() instanceof ColorDrawable));
        ColorDrawable colorDrawable= (ColorDrawable) child.getBackground();
        int color=colorDrawable.getColor();
        int alpha=0;
        if (scrollY<0){
            int alphaColor=ColorUtils.setAlphaComponent(color,0);
            child.setBackgroundColor(alphaColor);
            loopSubViewAlpha(child,0);
        }else if (scrollY>=0&&scrollY<=toolBarHeight){
            float alphaPercent=scrollY*1.f/toolBarHeight;
            alpha= (int) (255*alphaPercent);
            int alphaColor=ColorUtils.setAlphaComponent(color,alpha);
            child.setBackgroundColor(alphaColor);
            loopSubViewAlpha(child,alpha);
        }else {
            int alphaColor=ColorUtils.setAlphaComponent(color,255);
            child.setBackgroundColor(alphaColor);
            loopSubViewAlpha(child,255);
        }
//        int color=child.getSolidColor()
    }

    private void loopSubViewAlpha(View child,@IntRange(from = 0, to = 255)int alpha){
        if (child instanceof ViewGroup){
            ViewGroup viewGroup= (ViewGroup) child;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view=viewGroup.getChildAt(i);
                if (view instanceof TextView){
                    TextView textView= (TextView) view;
                    int color=textView.getCurrentTextColor();
                    textView.setTextColor(ColorUtils.setAlphaComponent(color,alpha));
                }
            }
        }
    }
}
