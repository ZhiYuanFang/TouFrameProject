package xyz.ttyz.tourfrxohc.fragment;

import androidx.databinding.ViewDataBinding;

public abstract class BaseInViewPagerFragment<T extends ViewDataBinding, B> extends BaseContainLoadMoreFragment<T, B> {


   public BaseInViewPagerFragment() {
   }

   @Override
   protected boolean isInViewPager() {
      return true;
   }

   public abstract String initTitle();

}
