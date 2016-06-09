/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.sandbox.app.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import io.reist.sandbox.repos.view.RepoListFragment;
import io.reist.sandbox.result.view.ResultActivity;
import io.reist.sandbox.time.view.TimeFragment;
import io.reist.sandbox.users.view.UserListFragment;
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
        drawerToggle.syncState();
        drawerToggle.setToolbarNavigationClickListener(v -> fragmentManager.popBackStackImmediate());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_repos);

        fragmentManager.addOnBackStackChangedListener(this);

        // TODO check if the fragment has been already restored by the fragment manager
        showFragment(new RepoListFragment(), false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
    public boolean onNavigationItemSelected(MenuItem item) {

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
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBackStackChanged() {
        boolean showBurger = fragmentManager.getBackStackEntryCount() == 1;
        drawerToggle.setDrawerIndicatorEnabled(showBurger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!showBurger);
        drawerToggle.syncState();
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
                popBackStack
        );
    }

}
