package xyz.ttyz.tourfrxohc.dialog;

import android.view.Gravity;
import android.view.WindowManager;

import androidx.databinding.ObservableField;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import xyz.ttyz.mylibrary.method.BaseTouSubscriber;
import xyz.ttyz.mylibrary.method.RxOHCUtils;
import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.utils.ToastUtil;
import xyz.ttyz.tourfrxohc.BaseApplication;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.activity.BaseActivity;
import xyz.ttyz.tourfrxohc.databinding.FragmentDialogEditNickNameBinding;
import xyz.ttyz.tourfrxohc.event.user.UserNickNameChangeEvent;
import xyz.ttyz.tourfrxohc.models.UserModel;
import xyz.ttyz.tourfrxohc.utils.UserUtils;

public class EditNickNameDialogFragment extends BaseDialogFragment<FragmentDialogEditNickNameBinding>{
    public ObservableField<String> nickNameFiled = new ObservableField<>("");
    @Override
    protected int initLayoutID() {
        return R.layout.fragment_dialog_edit_nick_name;
    }

    @Override
    protected void initViriable(FragmentDialogEditNickNameBinding mBinding) {
        mBinding.setContext(this);
    }

    @Override
    protected void initData() {
        nickNameFiled.set(UserUtils.getCurUserModel().getNickname());
    }

    @Override
    protected int showGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void limitHeight(WindowManager.LayoutParams lp) {
        lp.height = DensityUtil.dp2px(250);
    }

    public OnClickAdapter.onClickCommand clickConfirmChangeNickNameCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            if(nickNameFiled.get().isEmpty()){
                ToastUtil.showToast("请输入昵称");
                return;
            }
            UserModel curUser = UserUtils.getCurUserModel();
            new RxOHCUtils<UserModel>(getActivity()).executeApi(BaseApplication.apiService.updateNickName(curUser.getPhone(), curUser.getPassword(), nickNameFiled.get()), new BaseTouSubscriber<UserModel>((BaseActivity) getActivity()) {
                @Override
                public void success(UserModel data) {
                    ToastUtil.showToast("昵称修改成功");
                    UserUtils.getCurUserModel().setNickname(data.getNickname());
                    dismiss();
                    EventBus.getDefault().post(new UserNickNameChangeEvent());
                }

                @Override
                public String initCacheKey() {
                    return null;
                }
            });
        }
    };
}
