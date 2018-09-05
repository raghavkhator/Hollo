package com.iecsemanipal.holocaust.activities;
/**Modified by Raghav, Shashank, Roshan on 5/09/2018*/
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.iecsemanipal.holocaust.R;
import com.iecsemanipal.holocaust.fragments.PendingFragment;
import com.iecsemanipal.holocaust.fragments.RegisteredFragment;
import com.iecsemanipal.holocaust.fragments.ScanFragment;

public class MainActivity extends AppCompatActivity implements PendingFragment.PendingInterface{
    private static final String SCAN_TAG = "scan";
    private static final String REG_TAG = "register";
    private static final String PENDING_TAG = "pending";
    private static final int REGISTER_CODE = 1;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.main_nav_view);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navView.setCheckedItem(R.id.nav_reg);
        navView.setSelected(true);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_bottom_navigation_view);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new ScanFragment(),SCAN_TAG).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bottom_nav_scan:
                        if (getSupportFragmentManager().findFragmentByTag(SCAN_TAG) != null)
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, getSupportFragmentManager().findFragmentByTag(SCAN_TAG), SCAN_TAG).commit();
                        else
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new ScanFragment(), SCAN_TAG).commit();
                        break;
                    case R.id.bottom_nav_registered:
                        if (getSupportFragmentManager().findFragmentByTag(REG_TAG) != null)
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, getSupportFragmentManager().findFragmentByTag(REG_TAG), REG_TAG).commit();
                        else
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new RegisteredFragment(), REG_TAG).commit();
                        break;
                    case R.id.bottom_nav_pending:
                        if (getSupportFragmentManager().findFragmentByTag(PENDING_TAG) != null)
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, getSupportFragmentManager().findFragmentByTag(PENDING_TAG), PENDING_TAG).commit();
                        else
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new PendingFragment(), PENDING_TAG).commit();
                        break;
                }
                return true;
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch(item.getItemId()){
                            case R.id.nav_events:   Intent intent = new Intent(MainActivity.this, EventsActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                }, 375);
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_CODE){
            if (resultCode == RESULT_OK){
                if (getSupportFragmentManager().findFragmentByTag(REG_TAG) != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, getSupportFragmentManager().findFragmentByTag(REG_TAG), REG_TAG).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new RegisteredFragment(), REG_TAG).commit();
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_registered);
            }else if (resultCode == RESULT_CANCELED){
                if (getSupportFragmentManager().findFragmentByTag(PENDING_TAG) != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, getSupportFragmentManager().findFragmentByTag(PENDING_TAG), PENDING_TAG).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new PendingFragment(), PENDING_TAG).commit();
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_pending);
            }
        }
    }

    @Override
    public void completePending() {
        if (getSupportFragmentManager().findFragmentByTag(REG_TAG) != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, getSupportFragmentManager().findFragmentByTag(REG_TAG), REG_TAG).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new RegisteredFragment(), REG_TAG).commit();
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_registered);
    }
}
