package com.example.myapplication;

import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

public class HapticListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //--The user has pressed a soft keyboard key.
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            //--The user has performed a long press on an object that is resulting in an action being performed.
            // v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            //--The user has pressed on a virtual on-screen key.
            // v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
        return true;
    }
}
