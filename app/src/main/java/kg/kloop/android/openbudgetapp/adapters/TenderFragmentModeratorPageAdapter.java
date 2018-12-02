package kg.kloop.android.openbudgetapp.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import kg.kloop.android.openbudgetapp.fragments.AllTendersFragment;
import kg.kloop.android.openbudgetapp.fragments.TasksForModerationFragment;
import kg.kloop.android.openbudgetapp.fragments.TendersCompletedFragment;
import kg.kloop.android.openbudgetapp.fragments.TendersWithTasksFragment;

public class TenderFragmentModeratorPageAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Все", "Модерация", "Задания", "Готовые"};

    public TenderFragmentModeratorPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return AllTendersFragment.newInstance();
            case 1:
                return TasksForModerationFragment.newInstance();
            case 2:
                return TendersWithTasksFragment.newInstance();
            case 3:
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
