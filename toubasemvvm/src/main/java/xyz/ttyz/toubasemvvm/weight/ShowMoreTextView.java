package xyz.ttyz.toubasemvvm.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 3/6/2019 at 1:17 PM.
 *  * Email :
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 3/6/2019.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */


public class ShowMoreTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final String TAG = ShowMoreTextView.class.getName();

    private int showingLine = 3;

    private String showMore = "更多>";
    private String dotdot = "...";

    private int MAGIC_NUMBER = 0;
    private int showMoreTextColor = Color.parseColor("#4B8EFF");

    private String mainText;//原始数据

    public ShowMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(delegate != null) delegate.changeStatus(false);
    }

    boolean hasInit;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!hasInit){
            mainText = getText().toString();
            hasInit = true;
        }
    }
    ShowMoreDelegate delegate;

    public void setDelegate(ShowMoreDelegate delegate) {
        this.delegate = delegate;
    }

    public interface ShowMoreDelegate{
        void changeStatus(boolean showMore);
        boolean isShowMore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(delegate != null && showingLine < getLineCount() && !delegate.isShowMore()){
            @SuppressLint("DrawAllocation") StringBuilder showingText = new StringBuilder();
            int start = 0;
            int end;
            for (int i = 0; i < showingLine; i++) {
                end = getLayout().getLineEnd(i);
                showingText.append(getText().toString().substring(start, end));
                start = end;
            }

            String newText = showingText.substring(0, showingText.length() - (dotdot.length() + showMore.length() + MAGIC_NUMBER));
            Log.d(TAG, "Text: " + newText);
            Log.d(TAG, "Text: " + showingText);
            newText += dotdot + showMore;

            setText(newText);
            setShowMoreColoringAndClickable();
        }
    }

    private void setShowMoreColoringAndClickable() {
        final SpannableString spannableString = new SpannableString(getText());

        Log.d(TAG, "Text: " + getText());
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                    }

                                    @Override
                                    public void onClick(@Nullable View view) {
                                        setMaxLines(Integer.MAX_VALUE);
                                        setText(mainText);
                                        if(delegate != null) delegate.changeStatus(true);
                                        Log.d(TAG, "Item clicked: " + mainText);

                                    }
                                },
                getText().length() - (dotdot.length() + showMore.length()),
                getText().length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(showMoreTextColor),
                getText().length() - (dotdot.length() + showMore.length()),
                getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, BufferType.SPANNABLE);
    }

}
