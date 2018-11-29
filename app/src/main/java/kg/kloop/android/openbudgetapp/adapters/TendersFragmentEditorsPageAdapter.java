package kg.kloop.android.openbudgetapp.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
