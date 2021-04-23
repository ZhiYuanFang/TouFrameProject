package xyz.ttyz.tourfrxohc.fragment;

import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentWaitKeyRoomOperatorBinding;

public class WaitKeyRoomOperatorFragment extends BaseFragment<FragmentWaitKeyRoomOperatorBinding>{
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
}
