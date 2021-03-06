package com.travis.library.widget.pager.lazy;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * 懒加载PagerAdapter
 * Created by chenkai.gu on 2016/12/28.
 */
abstract class LazyPagerAdapter<T> extends PagerAdapter {
    protected SparseArray<T> mLazyItems = new SparseArray<>();
    private T mCurrentItem;

    /**
     * add lazy item to container
     *
     * @param container {@link LazyViewPager}
     * @param position  the position that the item added to
     * @return the item added
     */
    protected abstract T addLazyItem(ViewGroup container, int position);

    /**
     * get the lazy item
     *
     * @param container {@link LazyViewPager}
     * @param position  the position of lazy item
     * @return the lazy item
     */
    protected abstract T getItem(ViewGroup container, int position);

    /**
     * whether the item is lazily or not
     *
     * @param position the position of item
     * @return the item is lazily
     */
    public boolean isLazyItem(int position) {
        return mLazyItems.get(position) != null;
    }

    /**
     * get the current item
     *
     * @return the current item
     */
    public T getCurrentItem() {
        return mCurrentItem;
    }

    /**
     * call {@link LazyPagerAdapter#addLazyItem(android.view.ViewGroup, int)}
     * to prevent {@link LazyViewPager#onPageScrolled(int, float, int)} not working when the offset of {@link LazyViewPager} is too big
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentItem = addLazyItem(container, position);
    }
}
