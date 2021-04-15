package xyz.ttyz.toubasemvvm.adapter;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class EditAdapter {
    @BindingAdapter("editDelegate")
    public static void ed(final EditText editText, final EditDelegate editDelegate) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND || i == EditorInfo.IME_ACTION_SEARCH|| i == EditorInfo.IME_ACTION_DONE) {
                    String text = editText.getText().toString().trim();
                    if (editDelegate != null)
                        editDelegate.send(text);
                    return true;
                }
                return false;
            }
        });
    }

    public interface EditDelegate {
        void send(String str);
    }


    @BindingAdapter("focus")
    public static void autoFocus(View view, boolean focus){
        if(focus){
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }
}
