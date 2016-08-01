package son.nt.en.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import son.nt.en.base.BaseFragment;

/**
 * Created by sonnt on 7/11/16.
 */
public class HomeAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    public HomeAdapter (FragmentManager fm, List<BaseFragment> mFragments)
    {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    public void addFragment(BaseFragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
