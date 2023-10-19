package xyz.ttyz.tourfrxohc.fragment;

import androidx.databinding.ViewDataBinding;

import xyz.ttyz.mylibrary.method.RecordsModule;

public abstract class BaseInViewPagerFragment<T extends ViewDataBinding,D, B extends RecordsModule<D>> extends BaseContainLoadMoreFragment<T, D, B> {


   public BaseInViewPagerFragment() {
   }

   @Override
   protected boolean isInViewPager() {
      return true;
   }

   public abstract String initTitle();

}
