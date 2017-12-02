package dj.zendo.store.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import dj.zendo.store.R;
import dj.zendo.store.fragments.CheckoutFragment;
import dj.zendo.store.fragments.HomeFragment;
import dj.zendo.store.fragments.StoreFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottomnavigation_home_menu);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.item_home:
                        fragment = HomeFragment.newInstance();
                        break;

                    case R.id.item_store:
                        fragment = StoreFragment.newInstance();
                        break;

                    case R.id.item_checkout:
                        fragment = CheckoutFragment.newInstance();
                        break;
                }

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_home_currentscreen, fragment);
                fragmentTransaction.commit();

                return true;
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home_currentscreen, HomeFragment.newInstance());
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}