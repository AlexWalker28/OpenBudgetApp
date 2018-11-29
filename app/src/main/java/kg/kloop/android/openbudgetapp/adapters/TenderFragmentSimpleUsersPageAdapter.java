package kg.kloop.android.openbudgetapp.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
