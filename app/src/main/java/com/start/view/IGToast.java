package com.start.view;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.start.activity.R;
import com.start.utils.IGApplication;
import com.start.utils.Utils;


public class IGToast {
    public static void show(final String message){
        Toast toast = new Toast(IGApplication.getGlobalContext());
        toast.setGravity(Gravity.BOTTOM, 0, Utils.dp2px(50));
        toast.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = LayoutInflater.from(IGApplication.getGlobalContext());
        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.view_toast, null);
        TextView toastMessage = (TextView)view.findViewById(R.id.toast_message);
        toastMessage.setText(message);
        toast.setView(view);
        toast.show();
    }
    public static void show(final String message, int Gravity){
        Toast toast = new Toast(IGApplication.getGlobalContext());
        toast.setGravity(Gravity, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = LayoutInflater.from(IGApplication.getGlobalContext());
        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.view_toast, null);
        TextView toastMessage = (TextView)view.findViewById(R.id.toast_message);
        toastMessage.setText(message);
        toast.setView(view);
        toast.show();
    }
}
