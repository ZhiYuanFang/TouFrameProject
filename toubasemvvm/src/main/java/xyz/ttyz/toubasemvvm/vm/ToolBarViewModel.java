package xyz.ttyz.toubasemvvm.vm;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.android.material.appbar.AppBarLayout;

import xyz.ttyz.tou_example.ActivityManager;
import xyz.ttyz.toubasemvvm.R;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;

public class ToolBarViewModel extends BaseViewModle{
    public ObservableField<String> title = new ObservableField<>("");
    public ObservableInt titleColor = new ObservableInt(ContextCompat.getColor(ActivityManager.getInstance(), R.color.white));
    public ObservableBoolean notJudgeLogin = new ObservableBoolean(true);
    public ObservableField<OnClickAdapter.onClickCommand> backClick;
    public ObservableField<OnClickAdapter.onClickCommand> saveClick;
    public ObservableField<OnClickAdapter.onClickCommand> shareClick;
    public ObservableInt saveLikeDraResID;
    public ObservableInt saveNoLikeDraResID;
    public ObservableBoolean isFavored = new ObservableBoolean(false);
    public ObservableInt backDraResID = new ObservableInt(R.drawable.fanhui);
    public ObservableInt shareDraResID;
    public ObservableField<String> rightTxt = new ObservableField<>("");
    public ObservableField<OnClickAdapter.onClickCommand> rightClick;
    public ObservableField<AppBarLayout.OnOffsetChangedListener[]> offsetChangedListeners = new ObservableField<>();
    public ObservableBoolean rightNoBac = new ObservableBoolean(false);


    private ToolBarViewModel(Builder builder) {
        titleColor = builder.titleColor;
        title = builder.title;
        notJudgeLogin = builder.notJudgeLogin;
        backClick = builder.backClick;
        saveClick = builder.saveClick;
        shareClick = builder.shareClick;
        saveLikeDraResID = builder.saveLikeDraResID;
        saveNoLikeDraResID = builder.saveNoLikeDraResID;
        isFavored = builder.isFavored;
        backDraResID = builder.backDraResID;
        shareDraResID = builder.shareDraResID;
        rightTxt = builder.rightTxt;
        rightClick = builder.rightClick;
        offsetChangedListeners = builder.offsetChangedListeners;
        rightNoBac = builder.rightNoBac;
    }


    public static final class Builder {
        public ObservableInt titleColor = new ObservableInt(ContextCompat.getColor(ActivityManager.getInstance(), R.color.white));
        public ObservableBoolean rightNoBac = new ObservableBoolean(false);
        private ObservableField<String> title = new ObservableField<>("");
        private ObservableBoolean notJudgeLogin = new ObservableBoolean(true);
        private ObservableField<OnClickAdapter.onClickCommand> backClick = new ObservableField<OnClickAdapter.onClickCommand>(new OnClickAdapter.onClickCommand() {
            @Override
            public void click() {
                ActivityManager.getInstance().onBackPressed();
            }
        });
        private ObservableField<OnClickAdapter.onClickCommand> saveClick;
        private ObservableField<OnClickAdapter.onClickCommand> shareClick;
        private ObservableInt saveLikeDraResID;
        private ObservableInt saveNoLikeDraResID ;
        private ObservableBoolean isFavored = new ObservableBoolean(false);
        private ObservableInt backDraResID = new ObservableInt(R.drawable.fanhui);
        private ObservableInt shareDraResID;
        private ObservableField<String> rightTxt = new ObservableField<>("");
        private ObservableField<OnClickAdapter.onClickCommand> rightClick;
        public ObservableField<AppBarLayout.OnOffsetChangedListener[]> offsetChangedListeners = new ObservableField<>();

        public Builder() {
        }


        public Builder titleColor(int val) {
            titleColor = new ObservableInt(val);
            return this;
        }

        public Builder title(String val) {
            title = new ObservableField<>(val);
            return this;
        }

        public Builder notJudgeLogin(boolean val) {
            notJudgeLogin = new ObservableBoolean(val);
            return this;
        }


        public Builder backClick(OnClickAdapter.onClickCommand val) {
            backClick = new ObservableField<>(val);
            return this;
        }
        public Builder saveClick(OnClickAdapter.onClickCommand val) {
            saveClick = new ObservableField<>(val);
            return this;
        }

        public Builder shareClick(OnClickAdapter.onClickCommand val) {
            shareClick = new ObservableField<>(val);
            return this;
        }

        public Builder saveLikeDraResID(int val) {
            saveLikeDraResID = new ObservableInt(val);
            return this;
        }
        public Builder saveNoLikeDraResID(int val) {
            saveNoLikeDraResID = new ObservableInt(val);
            return this;
        }
        public Builder backDraResID(int val) {
            backDraResID = new ObservableInt(val);
            return this;
        }

        public Builder shareDraResID(int val) {
            shareDraResID = new ObservableInt(val);
            return this;
        }
        public Builder isFavored(boolean val) {
            isFavored = new ObservableBoolean(val);
            return this;
        }
        public Builder rightNoBac(boolean val) {
            rightNoBac = new ObservableBoolean(val);
            return this;
        }

        public Builder rightTxt(String val){
            rightTxt = new ObservableField<>(val);
            return this;
        }

        public Builder rightClick(OnClickAdapter.onClickCommand val){
            rightClick = new ObservableField<>(val);
            return this;
        }

        public Builder offsetChangedListeners(AppBarLayout.OnOffsetChangedListener... val){
            offsetChangedListeners = new ObservableField<>(val);
            return this;
        }
        public ToolBarViewModel build() {
            return new ToolBarViewModel(this);
        }



    }
}
