package isf.proagro.android.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

import isf.proagro.android.R;
import isf.proagro.android.ui.abstracts.BaseActivity;
import isf.proagro.android.ui.drawer.NavigationDrawerCallbacks;
import isf.proagro.android.ui.drawer.NavigationDrawerFragment;
import isf.proagro.android.ui.fragments.AboutFragment;
import isf.proagro.android.ui.fragments.FavoritesFragment;
import isf.proagro.android.ui.fragments.KnowledgeSharingFragment;
import isf.proagro.android.utils.SaveInstanceUtils;


public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String[] mTitles;
    private int mCurrentSelectedPosition = -1;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(SaveInstanceUtils.STATE_SELECTED_POSITION , -1);
        }
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        if (mCurrentSelectedPosition == -1) {
            addFragmentToContainer(KnowledgeSharingFragment.newInstance());
        }
        mTitles = getResources().getStringArray(R.array.drawer_menu_title);
        mNavigationDrawerFragment = (NavigationDrawerFragment) mFragmentManager.findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mCurrentSelectedPosition != position) {
            switch (position) {
                case 0:
                    addFragmentToContainer(KnowledgeSharingFragment.newInstance());
                    break;
                case 1:
                    addFragmentToContainer(FavoritesFragment.newInstance());
                    break;
                case 2:
                    addFragmentToContainer(new AboutFragment());

                default:
                    break;
            }
        }
        if (mTitles == null) {
            mTitles = getResources().getStringArray(R.array.drawer_menu_title);
        }
        if (mCurrentSelectedPosition > -1) {
            setTitle(mTitles[position]);
        }
        mCurrentSelectedPosition = position;
    }

    private void addFragmentToContainer(Fragment fragment) {
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void addBookletFragmentToContainer(Fragment fragment, String title) {
        addFragmentToContainer(fragment);
        setTitle(title);
        mCurrentSelectedPosition = -1;
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            onNavigationDrawerItemSelected(mCurrentSelectedPosition);
//            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            setTitle(R.string.app_name);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SaveInstanceUtils.STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    public boolean isNavigationDraweOpen() {
        return mNavigationDrawerFragment.isDrawerOpen();
    }
}
