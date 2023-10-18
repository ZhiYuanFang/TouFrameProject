package xyz.ttyz.tourfrxohc.fragment;

import androidx.databinding.ViewDataBinding;

public abstract class BaseInViewPagerFragment<T extends ViewDataBinding> extends BaseFragment<T> {


   public BaseInViewPagerFragment() {
   }

   @Override
   protected boolean isInViewPager() {
      return true;
   }

   public abstract String initTitle();

}
