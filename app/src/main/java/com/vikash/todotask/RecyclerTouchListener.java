package com.vikash.todotask;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;
    Context context;
    Long t1=0l,t2=0l;
    float x1=0,x2=0,y1=0,y2=0;
    Long CLICK_DURATION=500l;
    View child;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                Log.i("touch","OnSingleTapUP");
                return true;

            }

            @Override
            public void onLongPress(MotionEvent e) {


                }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=e.getX();
                y1=e.getY();
                t1=System.currentTimeMillis();
                child = rv.findChildViewUnder(x1, y1);
                return true;

            case MotionEvent.ACTION_UP:
                x2 = e.getX();
                y2 = e.getY();
                t2 = System.currentTimeMillis();
                Log.i("touch",String.valueOf(Math.abs(x1-x2)));

                if (Math.abs(x1 -x2)<40f && Math.abs(y1-y2)<40f && (t2 - t1) < CLICK_DURATION) {

                    Log.i("touch","click detected");

                    if (child != null && clickListener != null) {

                        clickListener.onClick(child, rv.getChildPosition(child));

                    }
                    return false;
                }
                else if (Math.abs(x1 -x2)<40f && Math.abs(y1-y2)<40f && (t2 - t1) >= CLICK_DURATION) {
                    if(child!=null && clickListener!=null){
                        clickListener.onLongClick(child,rv.getChildPosition(child));tatus
                    }

                }
                else if (x1 > x2) {

                }
                else if (x2 > x1) {

                }


                return true;

        }


        return false;

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {


    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ClickListener {

        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
