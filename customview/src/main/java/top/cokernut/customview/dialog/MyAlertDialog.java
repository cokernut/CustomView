package top.cokernut.customview.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import top.cokernut.customview.R;

/**
 * 利用AlertDialog自定义Dialog布局
 */
public class MyAlertDialog {
    private AlertDialog dialog;
    public void show(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Transparent);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog,null);
        view.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.rl_nine_image_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
            }
        });

        view.findViewById(R.id.rl_ordinary_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
            }
        });

        view.findViewById(R.id.tv_else_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
            }
        });
        builder.setView(view);
        dialog = builder.show();
    }
}
