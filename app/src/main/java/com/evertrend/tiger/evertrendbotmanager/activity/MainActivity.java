package com.evertrend.tiger.evertrendbotmanager.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.device.fragment.DevicesFragment;
import com.evertrend.tiger.device.fragment.DeviceLocationFragment;
import com.evertrend.tiger.device.service.DeviceCommunicationService;
import com.evertrend.tiger.evertrendbotmanager.R;
import com.evertrend.tiger.user.fragment.MeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private int devicesTabIndex = 0;
    private int locationTabIndex = 1;
    private int meTabIndex = 2;
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private List<BaseFragment> fragments;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        fragments = getFragments();
        initView();
        Intent intent = new Intent(this, DeviceCommunicationService.class);
        startService(intent);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPager2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                LogUtil.i(TAG, "onPageScrolled: "+position+"--->"+positionOffset+"--->"+positionOffsetPixels );
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                LogUtil.i(TAG, "onPageSelected: "+position );
                bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
//                LogUtil.i(TAG, "onPageScrollStateChanged: "+state );
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_devices:
                        viewPager2.setCurrentItem(devicesTabIndex, false);
                        break;
                    case R.id.action_location:
                        viewPager2.setCurrentItem(locationTabIndex, false);
                        break;
                    case R.id.action_me:
                        viewPager2.setCurrentItem(meTabIndex, false);
                        break;
                }
                return true;
            }
        });
    }

    private List<BaseFragment> getFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>(3);
        DevicesFragment devicesFragment = new DevicesFragment();
        fragments.add(devicesFragment);
        DeviceLocationFragment deviceLocationFragment = new DeviceLocationFragment();
        fragments.add(deviceLocationFragment);
        MeFragment meFragment = new MeFragment();
        fragments.add(meFragment);
        return fragments;
    }

}
