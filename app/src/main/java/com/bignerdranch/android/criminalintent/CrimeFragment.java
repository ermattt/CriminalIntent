package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;


public class CrimeFragment
        extends Fragment
{
    private static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_CONTACT = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private EditText mDetailsField;
    private Button mSuspectButton;
    private Button mCrimeDateButton;
    private CheckBox mSolvedCheckBox;

    public CrimeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CrimeFragment.
     */
    public static CrimeFragment newInstance(UUID crimeId)
    {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //UUID crimeId = getActivity().getIntent().getSerializableExtra()
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        // Date
        mCrimeDateButton = v.findViewById(R.id.crime_date_button);
        mCrimeDateButton.setText(mCrime.getDate().toString());
        mCrimeDateButton.setEnabled(false);

        // Title
        mTitleField = v.findViewById(R.id.crime_title_text_edit);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // do nothing
            }
        });

        // Details
        mDetailsField = v.findViewById(R.id.crime_details_text_edit);
        if (mCrime.getDetails() != null) {
            mDetailsField.setText(mCrime.getDetails());
        }
        mDetailsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { mCrime.setDetails(s.toString()); }

            @Override
            public void afterTextChanged(Editable s)
            {
                // do nothing
            }
        });

        // Solved
        mSolvedCheckBox = v.findViewById(R.id.crime_solved_checkbox);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mCrime.setSolved(isChecked);
            }
        });

        // Suspect
        mSuspectButton = v.findViewById(R.id.suspect_button);
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, packageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }
            finally {
                c.close();
            }
        }
    }
}
