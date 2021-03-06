package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * This class represent the home page
 */
public class Home extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /** Create the adapter that will return a fragment for each of the three
        * primary sections of the activity. */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /**  Set up the ViewPager with the sections adapter. */
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         *   getItem is called to instantiate the fragment for the given page.
         Return a PlaceholderFragment (defined as a static inner class below).
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            /** getItem is called to instantiate the fragment for the given page.
             Return a PlaceholderFragment (defined as a static inner class below). */
            switch (position) {
                /** place events , group  , my account tab in that order. */
                case 1:
                    Groups x = new Groups();


                    return x;

                case 2:
                    Myaccount y = new Myaccount();
                    return y;
                case 0:
                    Events z = new Events();
                    return z;


                default:
                    return null;
            }
        }

        /**
         *  return total no. of pages
         * @return
         */
        @Override
        public int getCount() {
            /** Show 3 total pages. */
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                /** place names for the tabs*/
                case 0:
                    return "Events";
                case 1:
                    return "Groups";
                case 2:
                    return "My Account";
            }
            return null;
        }
    }

    /**
     * Overide to go directly to login page
     */
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext() , MainActivity.class));
    }
}


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */