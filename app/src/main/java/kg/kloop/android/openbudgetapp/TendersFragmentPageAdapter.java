package kg.kloop.android.openbudgetapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TendersFragmentPageAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Все", "Задания", "Готовые"};

    public TendersFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return AllTendersFragment.newInstance();
            case 1:
                return TendersWithTasksFragment.newInstance();
            default:
                return AllTendersFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
