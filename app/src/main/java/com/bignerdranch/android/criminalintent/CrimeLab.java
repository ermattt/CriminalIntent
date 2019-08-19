package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// feedback, please!

public class CrimeLab
{
    private List<Crime> mCrimes;

    private static CrimeLab ourInstance;

    public static CrimeLab getInstance(Context context)
    {
        if (ourInstance == null) {
            ourInstance = new CrimeLab(context);
        }
        return ourInstance;
    }

    private CrimeLab(Context context)
    {
        mCrimes = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID uuid)
    {
        for (Crime c : mCrimes) {
            if (c.getId().equals(uuid)) {
                return c;
            }
        }
        return null;
    }
}
