package kg.kloop.android.openbudgetapp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kg.kloop.android.openbudgetapp.fragments.AllTendersFragment;
import kg.kloop.android.openbudgetapp.fragments.TendersCompletedFragment;
import kg.kloop.android.openbudgetapp.fragments.TendersWithTasksFragment;

public class TendersFragmentEditorsPageAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Все", "Задания", "Готовые"};

    public TendersFragmentEditorsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return AllTendersFragment.newInstance();
            case 1:
                return TendersWithTasksFragment.newInstance();
            case 2:
                return TendersCompletedFragment.newInstance();
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
