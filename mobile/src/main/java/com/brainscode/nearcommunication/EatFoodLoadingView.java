package com.brainscode.nearcommunication;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by platerosanchezm on 18/09/15.
 */
public class EatFoodLoadingView extends LinearLayout {

        private Handler mHandler;

        ImageView first;
        ImageView two;
        ImageView three;
        ImageView four;
        ImageView five;

        int loadPosition = 0;

        public EatFoodLoadingView(Context context) {
            super(context);
        }

        public EatFoodLoadingView(Context context, AttributeSet attrs) {
            super(context, attrs);

            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.custom_eat_foody_loading_view, this);

            first = (ImageView) findViewById(R.id.custom_eat_foody_loading_view_one);
            two = (ImageView) findViewById(R.id.custom_eat_foody_loading_view_two);
            three = (ImageView) findViewById(R.id.custom_eat_foody_loading_view_three);
            four = (ImageView) findViewById(R.id.custom_eat_foody_loading_view_four);
            five = (ImageView) findViewById(R.id.custom_eat_foody_loading_view_five);

            mHandler = new Handler();
            mStatusChecker.run();
        }

        Runnable mStatusChecker = new Runnable() {
            @Override
            public void run() {

                displayLoadingPosition(loadPosition);

                loadPosition++;

                mHandler.postDelayed(mStatusChecker, 500);
            }
        };

        private void displayLoadingPosition(int loadPosition) {
            int emphasizedViewPos = loadPosition % 5;

            first.setImageResource(R.drawable.fries);
            two.setImageResource(R.drawable.burger);
            three.setImageResource(R.drawable.hotdog);
            four.setImageResource(R.drawable.sushi);
            five.setImageResource(R.drawable.wrap);

            switch (emphasizedViewPos) {
                case 0:
                    first.setImageResource(R.drawable.fries_new);
                    break;

                case 1:
                    two.setImageResource(R.drawable.burger);
                    break;

                case 2:
                    three.setImageResource(R.drawable.hotdog);
                    break;

                case 3:
                    four.setImageResource(R.drawable.sushi);
                    break;

                case 4:
                    five.setImageResource(R.drawable.wrap);
                    break;
            }
        }


}
