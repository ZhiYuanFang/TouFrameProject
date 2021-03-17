package xyz.ttyz.toubasemvvm.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.R;

public class DialogUtils {
    //region Double
    private static CustomDoubleButtonDialog customDoubleButtonDialog;

    /**
     * 显示标题/信息/按钮(1,2)
     */
    public static void showDialog(String title, String message, DialogButtonModule okModule, DialogButtonModule cancelModule) {
        Activity activity = ActivityManager.getInstance();
        if (null == activity || activity.isDestroyed()) {
            return;
        }

        if(customDoubleButtonDialog != null){
            if(customDoubleButtonDialog.getMessage().equals(message) && customDoubleButtonDialog.isShowing()){
                //已经有一个一样的dialog显示了
                return;
            }
        }

        customDoubleButtonDialog = new CustomDoubleButtonDialog(activity, title, message, okModule, cancelModule);
        if (activity.isFinishing()) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customDoubleButtonDialog.show();
            }
        });
    }

    /**
     * 显示标题/信息/按钮(取消,2)
     */
    public static void showDialog(String title, String message, DialogButtonModule okModule) {
        showDialog(title, message, okModule, new DialogButtonModule("取消"));
    }

    public static void showDialog(String message, DialogButtonModule okModule, DialogButtonModule cancelModule) {
        showDialog("", message, okModule, cancelModule);
    }
    public static void showDialog(String message, DialogButtonModule okModule) {
        showDialog(message, okModule, new DialogButtonModule("取消"));
    }
    public static class DialogButtonModule {
        String value;
        int btnBackGround;
        int btnTextColor;
        DialogClickDelegate dialogClickDelegate;

        public DialogButtonModule(String value) {
            this.value = value;
        }

        public DialogButtonModule(String value, DialogClickDelegate dialogClickDelegate) {
            this(value);
            this.dialogClickDelegate = dialogClickDelegate;
        }

        public DialogButtonModule(String value, @ColorRes int btnTextColor, @DrawableRes int btnBackGround, DialogClickDelegate dialogClickDelegate) {
            this(value);
            this.btnBackGround = btnBackGround;
            this.btnTextColor = btnTextColor;
            this.dialogClickDelegate = dialogClickDelegate;
        }

        public int getBtnTextColor() {
            return btnTextColor;
        }

        public int getBtnBackGround() {
            return btnBackGround;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public DialogClickDelegate getDialogClickDelegate() {
            return dialogClickDelegate;
        }

        public void setDialogClickDelegate(DialogClickDelegate dialogClickDelegate) {
            this.dialogClickDelegate = dialogClickDelegate;
        }
    }

    public interface DialogClickDelegate {
        void click(DialogButtonModule dialogButtonModule);
    }

    private static class CustomDoubleButtonDialog extends BaseDialog {
        TextView tv_confirm, tv_cancel, tv_title, tv_message;
        DialogButtonModule okModule, cancelModule;

        public CustomDoubleButtonDialog(@NonNull Context context, String title, String message, DialogButtonModule okModule, DialogButtonModule cancelModule) {
            super(context, R.style.DialogTheme);
            this.okModule = okModule;
            this.cancelModule = cancelModule;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
            setContentView(view);
            setCanceledOnTouchOutside(false);
            tv_confirm = view.findViewById(R.id.tv_confirm);
            tv_cancel = view.findViewById(R.id.tv_cancel);
            tv_title = view.findViewById(R.id.tv_title);
            tv_message = view.findViewById(R.id.tv_message);
            tv_title.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                tv_title.setText(title);
            }
            tv_message.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(message)) {
                tv_message.setText(message);
            }

            if (okModule != null) {
                tv_confirm.setText(okModule.getValue());
            }

            if (this.okModule.btnBackGround != 0) {
                tv_confirm.setBackground(ContextCompat.getDrawable(context, this.okModule.getBtnBackGround()));
            }
            if (this.okModule.btnTextColor != 0) {
                tv_confirm.setTextColor(ContextCompat.getColor(context, this.okModule.getBtnTextColor()));
            }

            tv_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (CustomDoubleButtonDialog.this.okModule != null
                            && CustomDoubleButtonDialog.this.okModule.getDialogClickDelegate() != null) {
                        CustomDoubleButtonDialog.this.okModule.getDialogClickDelegate().click(CustomDoubleButtonDialog.this.okModule);
                    }
                }
            });

            if (cancelModule != null) {
                tv_cancel.setText(cancelModule.getValue());
            }
            if (this.cancelModule.btnBackGround != 0) {
                tv_cancel.setBackground(ContextCompat.getDrawable(context, this.cancelModule.getBtnBackGround()));
            }

            if (this.cancelModule.btnTextColor != 0) {
                tv_cancel.setTextColor(ContextCompat.getColor(context, this.cancelModule.getBtnTextColor()));
            }
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (CustomDoubleButtonDialog.this.cancelModule != null
                            && CustomDoubleButtonDialog.this.cancelModule.getDialogClickDelegate() != null) {
                        CustomDoubleButtonDialog.this.cancelModule.getDialogClickDelegate().click(CustomDoubleButtonDialog.this.cancelModule);
                    }
                }
            });
        }

        public CustomDoubleButtonDialog(@NonNull Context context, String title, String message, DialogButtonModule okModule) {
            this(context, title, message, okModule, null);
        }

        public String getMessage(){
            if(tv_message != null){
                return tv_message.getText().toString();
            }
            return "";
        }
    }

    //endregion
    @SuppressLint("StaticFieldLeak")
    private static CustomSingleButtonDialog customSingleButtonDialog;

    //region single

    public static void showSingleDialog(String title, String message, DialogButtonModule okModule) {
        Activity activity = ActivityManager.getInstance();
        customSingleButtonDialog = new CustomSingleButtonDialog(activity, title, message, okModule);
        if (activity.isFinishing()) {
            return;
        }
        if(customSingleButtonDialog != null){
            if(customSingleButtonDialog.getMessage().equals(message) && customSingleButtonDialog.isShowing()){
                //已经有一个一样的dialog显示了
                return;
            }
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customSingleButtonDialog.show();
            }
        });
    }

    public static void showSingleDialog(String message, DialogButtonModule okModule) {
        showSingleDialog(null, message, okModule);
    }

    public static void showSingleDialog(String title, String message) {
        showSingleDialog(title, message, new DialogButtonModule("确定"));
    }

    private static class CustomSingleButtonDialog extends BaseDialog {
        TextView tv_confirm, tv_title, tv_message;
        DialogButtonModule okModule;

        public CustomSingleButtonDialog(@NonNull Context context, String title, String message, DialogButtonModule okModule) {
            super(context, R.style.DialogTheme);
            this.okModule = okModule;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_single_dialog, null);
            setContentView(view);
            setCanceledOnTouchOutside(false);
            tv_confirm = view.findViewById(R.id.tv_confirm);
            tv_title = view.findViewById(R.id.tv_title);
            tv_message = view.findViewById(R.id.tv_message);
            resetInfo(title, message, okModule);
        }


        public String getMessage(){
            if(tv_message != null){
                return tv_message.getText().toString();
            }
            return "";
        }

        public void resetInfo(String title, String message, DialogButtonModule okModule) {
            tv_title.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
            tv_message.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                tv_title.setText(title);
            }
            if (!TextUtils.isEmpty(message)) {
                tv_message.setText(message);
            }
            if (okModule != null) {
                tv_confirm.setText(okModule.getValue());
            }
            tv_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (CustomSingleButtonDialog.this.okModule != null
                            && CustomSingleButtonDialog.this.okModule.getDialogClickDelegate() != null) {
                        CustomSingleButtonDialog.this.okModule.getDialogClickDelegate().click(CustomSingleButtonDialog.this.okModule);
                    }
                }
            });
        }
    }
    //endregion


    private static abstract class BaseDialog extends Dialog {
        boolean downLoad;

        public BaseDialog(@NonNull Context context) {
            super(context);
        }

        public BaseDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        public void dismiss() {
            super.dismiss();
//            isShowIng = false;
        }

        @Override
        public void show() {
            try {
                super.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if(downLoad){
//                super.show();
//                return;
//            }
//            if(isShowIng){
//                return;
//            }
//            super.show();
//            isShowIng = true;
        }
    }
}
