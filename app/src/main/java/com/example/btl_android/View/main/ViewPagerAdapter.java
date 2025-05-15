package com.example.btl_android.View.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.btl_android.View.calendar.CalendarFragment;
import com.example.btl_android.View.chart.ChartFragment;
import com.example.btl_android.View.home.HomeFragment;
//import com.example.btl_android.View.menu.MenuFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new CalendarFragment();
//            case 2:
            default:
                return new ChartFragment();
//            default:
//                return new MenuFragment();
        }
    }
}
