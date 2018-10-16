package kg.kloop.android.openbudgetapp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kg.kloop.android.openbudgetapp.fragments.MyTendersFragment;
import kg.kloop.android.openbudgetapp.fragments.TendersCompletedFragment;
import kg.kloop.android.openbudgetapp.fragments.TendersWithTasksFragment;

public class TenderFragmentSimpleUsersPageAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"Задания", "Готовые", "Мои"};

    public TenderFragmentSimpleUsersPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return TendersWithTasksFragment.newInstance();
            case 1:
                return TendersCompletedFragment.newInstance();
            case 2:
                return MyTendersFragment.newInstance();
            default:
                return MyTendersFragment.newInstance();
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
