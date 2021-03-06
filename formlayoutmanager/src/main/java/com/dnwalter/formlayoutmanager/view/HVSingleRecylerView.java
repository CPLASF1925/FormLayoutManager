package com.dnwalter.formlayoutmanager.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dnwalter.formlayoutmanager.layoutmanager.FormLayoutManager;

/**
 * @author ousiyuan
 * @date 2019/10/18
 * 只能单方向滑动的recyclerview，当手指松开才能改变方向
 */
public class HVSingleRecylerView extends RecyclerView {
    // 滚动的类型
    private int mScrollType = -1;
    private int mPreScrollType = RecyclerView.VERTICAL;
    private float mDownX;
    private float mDownY;

    public HVSingleRecylerView(@NonNull Context context) {
        super(context);
    }

    public HVSingleRecylerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        // 最好在这里获取点下的坐标，因为当recyclerview的item加了点击事件的话，onTouchEvent是拿不到ACTION_MOVE的事件
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = e.getX();
                mDownY = e.getY();
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = e.getX() - mDownX;
                float dy = e.getY() - mDownY;
                if (mScrollType == -1) {
                    FormLayoutManager layoutManager = (FormLayoutManager) getLayoutManager();
                    if (Math.abs(dx) > Math.abs(dy)) {
                        mScrollType = RecyclerView.HORIZONTAL;
                        mPreScrollType = RecyclerView.HORIZONTAL;
                        layoutManager.setCanScrollV(false);
                    }
                    else {
                        mScrollType = RecyclerView.VERTICAL;
                        mPreScrollType = RecyclerView.VERTICAL;
                        layoutManager.setCanScrollH(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrollType = -1;
                FormLayoutManager layoutManager1 = (FormLayoutManager) getLayoutManager();
                layoutManager1.setCanScrollV(true);
                layoutManager1.setCanScrollH(true);
                break;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        switch (mPreScrollType){
            case RecyclerView.HORIZONTAL:
                velocityY = 0;
                break;
            case RecyclerView.VERTICAL:
                velocityX = 0;
                break;
        }
        return super.fling(velocityX, velocityY);
    }
}
