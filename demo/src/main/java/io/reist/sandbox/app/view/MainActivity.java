/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.sandbox.app.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.reist.sandbox.R;
import io.reist.sandbox.cryptocurrency.view.CryptoCurrencyFragment;
import io.reist.sandbox.feed.view.FeedListFragment;
import io.reist.sandbox.food.view.RestaurantListFragment;
import io.reist.sandbox.repos.view.RepoListFragment;
import io.reist.sandbox.result.view.ResultActivity;
import io.reist.sandbox.time.view.TimeFragment;
import io.reist.sandbox.users.view.UserListFragment;
import io.reist.sandbox.weather.view.WeatherFragment;
import io.reist.visum.view.VisumFragment;
import io.reist.visum.view.VisumFragmentManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentManager.OnBackStackChangedListener,
        BaseFragment.FragmentController {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setToolbarNavigationClickListener(v -> fragmentManager.popBackStackImmediate());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_repos);

        fragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            showFragment(new RestaurantListFragment(), false);
        }
        syncBackStack();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_repos:
                showFragment(new RepoListFragment(), true, true);
                break;
            case R.id.nav_users:
                showFragment(new UserListFragment(), true, true);
                break;
            case R.id.nav_time:
                showFragment(new TimeFragment(), true, true);
                break;
            case R.id.nav_result:
                startActivity(new Intent(this, ResultActivity.class));
                break;
            case R.id.nav_feed:
                showFragment(new FeedListFragment(), true, true);
                break;
            case R.id.nav_weather:
                showFragment(new WeatherFragment(), true, true);
                break;
            case R.id.nav_cryptocurrency:
                showFragment(new CryptoCurrencyFragment(), true, true);
                break;
            case R.id.nav_restaurants:
                showFragment(new RestaurantListFragment(), true, true);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void syncBackStack() {
        boolean showBurger = fragmentManager.getBackStackEntryCount() == 1;
        drawerToggle.setDrawerIndicatorEnabled(showBurger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!showBurger);
        drawerToggle.syncState();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBackStackChanged() {
        syncBackStack();
    }

    @Override
    public void showFragment(VisumFragment fragment, boolean remove) {
        showFragment(fragment, remove, false);
    }

    private void showFragment(VisumFragment fragment, boolean remove, boolean popBackStack) {
        VisumFragmentManager.showFragment(getSupportFragmentManager(),
                fragment,
                R.id.fragment_container,
                remove,
                popBackStack,
                R.anim.fade_in,
                R.anim.fade_out,
                0,
                0
        );
    }

}
