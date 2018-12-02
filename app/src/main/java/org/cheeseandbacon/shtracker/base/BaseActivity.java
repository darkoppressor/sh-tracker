/*
 * Copyright (c) 2018 Cheese and Bacon Games, LLC
 * This file is licensed under the MIT License.
 * See the file docs/LICENSE.txt for the full license text.
 */

package org.cheeseandbacon.shtracker.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ArrayList<NavigationItem> navigationItems;
    @Nullable
    private org.cheeseandbacon.shtracker.base.Menu menu;

    public void onCreate (int contentId, @NonNull final String toolbarTitle,
                          @Nullable org.cheeseandbacon.shtracker.base.Menu menu) {
        this.menu = menu;

        setContentView(R.layout.drawer);

        drawerLayout = findViewById(R.id.drawerLayout);

        FrameLayout frameLayout = findViewById(R.id.drawerContent);
        frameLayout.addView(View.inflate(this, contentId, null));

        navigationView = findViewById(R.id.drawerNavigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(toolbarTitle);

            setSupportActionBar(toolbar);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(ContextCompat.getColor(this, android.R.color.white));
        actionBarDrawerToggle.setDrawerArrowDrawable(drawerArrowDrawable);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Vibration.buttonPress(BaseActivity.this);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                Vibration.buttonPress(BaseActivity.this);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        navigationItems = null;

        buildNavigationMenu();
    }

    private void buildNavigationMenu () {
        navigationItems = new ArrayList<>();

        /*QQQ navigationItems.add(new NavigationItem("Example", R.drawable.example, () -> {
            ///QQQ
        }));*/

        setupNavigationMenu();

        setupNavigationView();
    }

    private void setupNavigationMenu () {
        navigationView.getMenu().clear();

        for (int i = 0; i < navigationItems.size(); i++) {
            NavigationItem item = navigationItems.get(i);
            item.setId(i);

            MenuItem menuItem = navigationView.getMenu().add(Menu.NONE, i, Menu.NONE, item.getName());
            menuItem.setIcon(item.getDrawableId());
        }
    }

    private void setupNavigationView () {
        navigationView.setNavigationItemSelectedListener(item -> {
            for (NavigationItem navigationItem : navigationItems) {
                if (navigationItem.getId() == item.getItemId()) {
                    Vibration.buttonPress(this);

                    if (navigationItem.getOnClick() != null) {
                        navigationItem.getOnClick().onClick();
                    }

                    return true;
                }
            }

            return false;
        });
    }

    @Override
    protected void onPostCreate (@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        if (this.menu != null) {
            getMenuInflater().inflate(this.menu.getResIdGetter().getMenuResId(), menu);

            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (!actionBarDrawerToggle.onOptionsItemSelected(item) && (menu == null ||
                !menu.getOnMenuItemClickListener().onMenuItemClick(item))) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            return true;
        }
    }

    @Override
    public void onResume () {
        super.onResume();

        drawerLayout.closeDrawers();
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
