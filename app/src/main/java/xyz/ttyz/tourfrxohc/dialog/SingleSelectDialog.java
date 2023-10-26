package xyz.ttyz.tourfrxohc.dialog;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import xyz.ttyz.toubasemvvm.adapter.OnClickAdapter;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent;
import xyz.ttyz.toubasemvvm.adapter.utils.BaseRecyclerAdapter;
import xyz.ttyz.tourfrxohc.R;
import xyz.ttyz.tourfrxohc.databinding.DialogSingleSelectBinding;
import xyz.ttyz.tourfrxohc.fragment.BaseDialogFragment;
import xyz.ttyz.tourfrxohc.fragment.SelectStrViewHolder;
import xyz.ttyz.tourfrxohc.models.SelectItemModel;

public class SingleSelectDialog extends BaseDialogFragment<DialogSingleSelectBinding> {
    SingleSelectDelegate singleSelectDelegate;
    List<SelectItemModel> selectItemModelList;

    BaseEmptyAdapterParent adapter;
    RecyclerView.ItemDecoration itemDecoration;

    private static SingleSelectDialog singleSelectDialog;

    public  static SingleSelectDialog getInstance( List<SelectItemModel> selectItemModelList, SingleSelectDelegate singleSelectDelegate){
        if(singleSelectDialog == null){
            singleSelectDialog = new SingleSelectDialog(singleSelectDelegate, selectItemModelList);
        } else {
            singleSelectDialog.resetData(singleSelectDelegate, selectItemModelList);
        }
        return singleSelectDialog;
    }

    private SingleSelectDialog(SingleSelectDelegate singleSelectDelegate, List<SelectItemModel> selectItemModelList) {
        this.singleSelectDelegate = singleSelectDelegate;
        this.selectItemModelList = selectItemModelList;
    }
    private void resetData(SingleSelectDelegate singleSelectDelegate, List<SelectItemModel> selectItemModelList) {
        this.singleSelectDelegate = singleSelectDelegate;
        this.selectItemModelList = selectItemModelList;
        initData();
    }
    @Override
    protected int initLayoutID() {
        return R.layout.dialog_single_select;
    }

    @Override
    protected void initViriable(DialogSingleSelectBinding mBinding) {
        adapter = new BaseEmptyAdapterParent(getContext(), new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new SelectStrViewHolder(getContext(), parent, new SelectStrViewHolder.SelectItemDelegate() {
                    @Override
                    public void selectItem(SelectItemModel selectItemModel) {
                        if(singleSelectDelegate != null){
                            singleSelectDelegate.selectItem(selectItemModel);
                        }
                        dismiss();
                    }
                });
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((SelectStrViewHolder)holder).bindData((SelectItemModel) adapter.getItem(position));
            }
        });
        itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL){
            @Override
            public void setDrawable(@NonNull Drawable drawable) {
                super.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_1_gray));
            }
        };
        mBinding.setAdapter(adapter);
        mBinding.setItemDecoration(itemDecoration);
        mBinding.setContext(this);
    }

    @Override
    protected void initData() {
        if(adapter == null){
            return;
        }
        adapter.clear();
        adapter.addAll(selectItemModelList);
    }

    public OnClickAdapter.onClickCommand clickCancelCommand = new OnClickAdapter.onClickCommand() {
        @Override
        public void click() {
            dismiss();
        }
    };

    public interface SingleSelectDelegate{
        void selectItem(SelectItemModel selectItemModel);
    }

}
