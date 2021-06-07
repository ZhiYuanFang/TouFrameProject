package xyz.ttyz.tourfrxohc.fragment;

import androidx.databinding.ObservableInt;

import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentWaitKeyRoomOperatorBinding;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;

public class WaitKeyRoomOperatorFragment extends BaseFragment<FragmentWaitKeyRoomOperatorBinding>{
    public ObservableInt backTimeFiled = new ObservableInt(DefaultUtils.backPutTime);
    @Override
    protected boolean isInViewPager() {
        return false;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_wait_key_room_operator;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initServer() {

    }

    public void resetTime(int time){
        backTimeFiled.set(time);
    }
}
