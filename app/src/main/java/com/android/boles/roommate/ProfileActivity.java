package com.android.boles.roommate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by boles on 2/11/2017.
 */

public class ProfileActivity extends AppCompatActivity implements DrawerLock
{
    public static final String ARG_FBOOK_NAME = "fbook_name";
    public static final String ARG_FBOOK_PIC_ID = "fbook_pic_id";
    public static final String DIALOG_LOGOUT = "DialogLogout";
    public static final String TASK_HEADER = "Task List";
    public static final String PROFILE_HEADER = "My Profile";

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private String name;
    private String picID;
    private DatabaseReference mDatabase;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        setTitle(TASK_HEADER);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        name = intent.getStringExtra(ARG_FBOOK_NAME);
        picID = intent.getStringExtra(ARG_FBOOK_PIC_ID);

        loadTasks();
        initNavDrawer();
    }

    //create the navigation drawer
    public void initNavDrawer() {

        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id){

                    case R.id.tasks_drawer:
                        PublicTasksFragment publicTasksFragment = (PublicTasksFragment) getSupportFragmentManager()
                                .findFragmentByTag("TASKS_FRAGMENT");

                        if (publicTasksFragment != null && publicTasksFragment.isVisible()) {
                            mDrawerLayout.closeDrawers();
                        } else {
                            loadTasks();
                        }
                        setTitle(TASK_HEADER);
                        mDrawerLayout.closeDrawers();
                        navigationView.getMenu().getItem(0).setChecked(true);
                        break;

                    case R.id.logout_drawer:
                        LogoutDialogFragment logout = new LogoutDialogFragment();
                        logout.show(getSupportFragmentManager(), DIALOG_LOGOUT);
                        break;

                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        TextView nav_name = (TextView) header.findViewById(R.id.nav_profile_name);
        nav_name.setText(name);
        ProfilePictureView profilePic = (ProfilePictureView) header.findViewById(R.id.nav_profile_pic);
        profilePic.setProfileId(picID);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment myMyProfileFragment = (MyProfileFragment) getSupportFragmentManager()
                        .findFragmentByTag("PROFILE_FRAGMENT");
                if (myMyProfileFragment != null && myMyProfileFragment.isVisible()) {
                    mDrawerLayout.closeDrawers();
                } else {
                    loadProfile();
                    mDrawerLayout.closeDrawers();
                }
                setTitle(PROFILE_HEADER);
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
            }
        });

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.black);

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

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
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

    //load tasks fragment
    public void loadTasks() {
        Fragment fragment2 = new PublicTasksFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment2, "TASKS_FRAGMENT")
                .addToBackStack("myTaskFrag")
                .commit();
    }

    //load profile fragment
    public void loadProfile() {
        Fragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(MyProfileFragment.ARG_NAME, name);
        args.putString(MyProfileFragment.ARG_PIC_ID, picID);
        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, "PROFILE_FRAGMENT")
                .addToBackStack("profileFrag")
                .commit();
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        if (enabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}
