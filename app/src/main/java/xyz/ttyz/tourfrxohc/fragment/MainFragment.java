package xyz.ttyz.tourfrxohc.fragment;

import androidx.databinding.ObservableField;

import xyz.ttyz.toubasemvvm.utils.ImageLoaderUtil;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentMainBinding;

public class MainFragment extends BaseFragment<FragmentMainBinding>{

    public ObservableField<String> imageFiled = new ObservableField<>("");
    @Override
    protected boolean isInViewPager() {
        return false;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {
        imageFiled.set(ImageLoaderUtil.testPic);
    }
}
