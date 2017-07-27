package com.droiddevgeeks.untroddenlabsdemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.droiddevgeeks.untroddenlabsdemo.fragments.MainLandingFragment;
import com.droiddevgeeks.untroddenlabsdemo.fragments.SongsDetailFragment;

/**
 * Created by Vampire on 2017-07-27.
 */

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment();
    }

    private void loadFragment()
    {
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MainLandingFragment()).commit();
    }


    @Override
    public void onBackPressed()
    {
        Fragment fragment = (SongsDetailFragment) getSupportFragmentManager().findFragmentByTag(MainLandingFragment.CURRENT_FRAGMENT_TAG);
        if (fragment !=null && fragment.isVisible())
        {
            super.onBackPressed();
            findViewById(R.id.backIcon).setVisibility(View.INVISIBLE);
            getFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
