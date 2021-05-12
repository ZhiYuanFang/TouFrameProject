package xyz.ttyz.tourfrxohc.dialog;

import androidx.databinding.ObservableInt;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.PopUtils;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.FragmentDialogCreateHomeBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class CreateHomeDialogFragment extends BaseDialogFragment<FragmentDialogCreateHomeBinding>{
    public ObservableInt roomLimitNumberFiled = new ObservableInt(4);

    @Override
    protected int initLayoutID() {
        return R.layout.fragment_dialog_create_home;
    }

    @Override
    protected void initViriable(FragmentDialogCreateHomeBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    protected void initData() {

    }

    public OnClickAdapter.onClickCommand selectNumberCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            PopUtils.show(mBinding.btnNumber, new NumberSelectView(getContext(),  number -> {
                roomLimitNumberFiled.set(number);

                PopUtils.disMiss();
            }), (customPopWindow, donwView) -> customPopWindow.showAsDropDown(mBinding.btnNumber, 0, -50));
        }
    };

    public OnClickAdapter.onClickCommand confirmCreateHomeCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            new RxOHCUtils<HomeModel>(getActivity()).executeApi(BaseApplication.apiService.createRoom(roomLimitNumberFiled.get(), UserUtils.getCurUserModel().getId()), new BaseSubscriber<HomeModel>((LifecycleProvider) getActivity()) {
                @Override
                public void success(HomeModel data) {
                    ToastUtil.showToast("房间创建成功");
                    // TODO: 2021/5/12 进入房间人员界面

                    CreateHomeDialogFragment.this.dismiss();
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };
}
