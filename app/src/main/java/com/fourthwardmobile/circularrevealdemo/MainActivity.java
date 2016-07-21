package com.fourthwardmobile.circularrevealdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    int ANIM_DURATION = 500;

    LinearLayout mViewTop;
    LinearLayout mViewBottom;

    Switch mSwitchTop;
    Switch mSwitchBottom;

    TextView mTextViewTop;
    TextView mTextViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewTop = (LinearLayout)findViewById(R.id.viewTop);
        mViewBottom = (LinearLayout)findViewById(R.id.viewBottom);

        mSwitchTop = (Switch)findViewById(R.id.switchTop);
        mSwitchBottom = (Switch)findViewById(R.id.switchBottom);

        //Set Listeners
        mSwitchTop.setOnCheckedChangeListener(this);
        mSwitchBottom.setOnCheckedChangeListener(this);

        mTextViewTop = (TextView)findViewById(R.id.textviewTop);
        mTextViewBottom = (TextView)findViewById(R.id.textviewBottom);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e(TAG, "onCheckedChanged with buttonView = " + buttonView.getId());

        if (buttonView.getId() == R.id.switchTop) {
            setTextViewColor(mTextViewTop, isChecked);

            if (isChecked)
                animateRevealColor(mViewTop, mSwitchTop, R.color.colorBlue);
            else
                animateHideColor(mViewTop, mSwitchTop);
        } else if (buttonView.getId() == R.id.switchBottom) {
            setTextViewColor(mTextViewBottom, isChecked);

            if (isChecked)
                animateRevealColor(mViewBottom, mSwitchBottom, R.color.colorGreen);
            else
                animateHideColor(mViewBottom, mSwitchBottom);
        }
    }

    private void setTextViewColor(TextView textView, boolean isChecked) {

        if(isChecked) {
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
            textView.setText(getString(R.string.on));
        }
        else {
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
            textView.setText(getString(R.string.off));
        }
    }

    private void animateRevealColor(ViewGroup view, Switch toggle, @ColorRes int color) {

        //Get center position of toggle switch
        int xPos = (toggle.getLeft() + toggle.getRight()) / 2;
        int yPos = (toggle.getTop() + toggle.getBottom()) / 2;

        float finalRadius = (float) Math.hypot(view.getWidth(),view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view,xPos,yPos,0,finalRadius);
        view.setBackgroundColor(ContextCompat.getColor(this,color));
        anim.setDuration(ANIM_DURATION);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    private void animateHideColor(final ViewGroup view, Switch toggle) {

        //Get center position of toggle switch
        int xPos = (toggle.getLeft() + toggle.getRight()) / 2;
        int yPos = (toggle.getTop() + toggle.getBottom()) / 2;

        float initialRadius = (float)Math.hypot(xPos,yPos);

        Animator anim = ViewAnimationUtils.createCircularReveal(view,xPos,yPos,initialRadius,0);

        //make the view invisible when the animation ends
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //Reset the background color after hiding the reveal color
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            }
        });

        //start annimation
        anim.start();
    }
}
