package com.lodestarapp.cs491.lodestar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class TripActivity extends AppCompatActivity {
    public ViewSwitcher view_switcher;
    public View firstView;
    public View secondView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        view_switcher =   (ViewSwitcher)findViewById(R.id.switcher);
        CardView card = (CardView) findViewById(R.id.my_card);
        firstView= findViewById(R.id.view1);
        secondView = findViewById(R.id.view2);
        card.setOnTouchListener(new OnSwipeTouchListener(TripActivity.this) {
            public void onSwipeLeft() {
                if (view_switcher.getCurrentView() != secondView){
                    view_switcher.showNext();
                }
            }

            public void onSwipeRight() {
                if (view_switcher.getCurrentView() != firstView){
                    view_switcher.showPrevious();
                }
            }
        });

    }




    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            private static final int SWIPE_DISTANCE_THRESHOLD = 100;

            public boolean onDown(MotionEvent e) {
                return true;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

}

