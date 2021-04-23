package xyz.ttyz.tourfrxohc.fragment;

import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentRoleBinding;

public class RoleFragment extends BaseFragment<FragmentRoleBinding>{
    public int roleType;

    public RoleFragment(int roleType) {
        this.roleType = roleType;
    }

    @Override
    protected boolean isInViewPager() {
        return false;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_role;
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

    }
}
