package com.example.myapplication;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class CustomProgressDialogue extends Dialog {
    public CustomProgressDialogue(Context context) {
        super(context);

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(true);
        setOnCancelListener(dialogInterface -> {
            Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
        });
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_progress, null);
        setContentView(view);

    }
}