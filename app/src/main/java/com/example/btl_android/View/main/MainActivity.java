package com.example.btl_android.View.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.btl_android.R;
import com.example.btl_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();
    }

    private void setupNavigation() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewpager.setAdapter(adapter);

        // Setup BottomNavigationView
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                binding.viewpager.setCurrentItem(0);
                return true;
            } else if (id == R.id.nav_calendar) {
                binding.viewpager.setCurrentItem(1);
                return true;
            } else if (id == R.id.nav_reports) {
                binding.viewpager.setCurrentItem(2);
                return true;
            } else if (id == R.id.nav_menu) {
                binding.viewpager.setCurrentItem(3);
                return true;
            }

            return false;
        });


        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        binding.bottomNavigation.setSelectedItemId(R.id.nav_calendar);
                        break;
                    case 2:
                        binding.bottomNavigation.setSelectedItemId(R.id.nav_reports);
                        break;
                    case 3:
                        binding.bottomNavigation.setSelectedItemId(R.id.nav_menu);
                        break;
                }
            }
        });
    }
}