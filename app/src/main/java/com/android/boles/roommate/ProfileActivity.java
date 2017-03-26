package com.android.boles.roommate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by boles on 2/11/2017.
 */

public class ProfileActivity extends AppCompatActivity
{
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView mDrawerList;
    private String name;
    private String picID;
    private DatabaseReference mDatabase;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        picID = intent.getStringExtra("profileID");

        loadProfile();

        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    //private class for drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the fragment to show based on position
        switch (position) {
            case 0:
                ProfileFragment myFragment = (ProfileFragment) getSupportFragmentManager()
                        .findFragmentByTag("PROFILE_FRAGMENT");

                if (myFragment != null && myFragment.isVisible()) {
                    mDrawerLayout.closeDrawers();
                } else  {
                    loadProfile();
                }
                break;

            case 1:
                MyTasksFragment myTasksFragment = (MyTasksFragment) getSupportFragmentManager()
                        .findFragmentByTag("TASKS_FRAGMENT");

                if (myTasksFragment != null && myTasksFragment.isVisible()) {
                    mDrawerLayout.closeDrawers();
                } else {
                    Fragment fragment2 = new MyTasksFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment2, "TASKS_FRAGMENT")
                            .addToBackStack("myTaskFrag")
                            .commit();
                }
                break;

            case 2:
                Fragment fragment4 = new LogoutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment4)
                        .addToBackStack("logoutFrag")
                        .commit();
                break;

            default:
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void loadProfile() {
        Fragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("picID", picID);
        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, "PROFILE_FRAGMENT")
                .addToBackStack("profileFrag")
                .commit();
    }

}
