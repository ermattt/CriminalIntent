package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

// feedback, please!

public class CrimeListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment getFragment()
    {
        return new CrimeListFragment();
    }
}
