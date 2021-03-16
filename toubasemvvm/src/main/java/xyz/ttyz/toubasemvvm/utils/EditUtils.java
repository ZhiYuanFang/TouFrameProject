package xyz.ttyz.toubasemvvm.utils;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import xyz.ttyz.tou_example.ActivityManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EditUtils {
    public static void showEdit(EditText editText){
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) ActivityManager.getInstance().getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
