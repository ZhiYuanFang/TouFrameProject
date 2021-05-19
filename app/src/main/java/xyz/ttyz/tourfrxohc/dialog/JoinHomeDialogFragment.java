package xyz.ttyz.tourfrxohc.dialog;

import androidx.databinding.ObservableField;

import com.trello.rxlifecycle2.LifecycleProvider;

import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.MainActivity;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.activity.RoomWaitActivity;
import xyz.ttyz.tourfrxohc.databinding.FragmentDialogJoinHomeBinding;
import xyz.ttyz.tourfrxohc.http.BaseSubscriber;
import xyz.ttyz.tourfrxohc.models.game.HomeModel;
import xyz.ttyz.tourfrxohc.utils.DefaultUtils;
import xyz.ttyz.tourfrxohc.utils.HomeUtils;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class JoinHomeDialogFragment extends BaseDialogFragment<FragmentDialogJoinHomeBinding>{
    public ObservableField<String> roomIdFiled = new ObservableField("");


    @Override
    protected int initLayoutID() {
        return R.layout.fragment_dialog_join_home;
    }

    @Override
    protected void initViriable(FragmentDialogJoinHomeBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    protected void initData() {

    }

    public OnClickAdapter.onClickCommand confirmJoinCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if(roomIdFiled.get().isEmpty()){
                ToastUtil.showToast("请输入房间号");
                return;
            }
            new RxOHCUtils<HomeModel>(getActivity()).executeApi(BaseApplication.apiService.joinRoom(Long.valueOf(roomIdFiled.get()), UserUtils.getCurUserModel().getId()), new BaseSubscriber<HomeModel>((LifecycleProvider) getActivity()) {
                @Override
                public void success(HomeModel data) {
                    HomeUtils.inHome(data);
                    //成功加入了房间
                    //进入组队房间
                    dismiss();
                    RoomWaitActivity.show();
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };
}
