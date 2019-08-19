package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment
        extends Fragment
{

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    // METHODS

    public CrimeListFragment()
    {
        // required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mCrimeRecyclerView.setLayoutManager(llm);

        updateUI();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }
    }


    // INNER CLASSES
    private class CrimeHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener  {

        private Crime mCrime;
        private TextView mCrimeTitleTextView;
        private TextView mCrimeDateTextView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            mCrimeTitleTextView = itemView.findViewById(R.id.crime_title);
            mCrimeDateTextView = itemView.findViewById(R.id.crime_date);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime)
        {
            mCrime = crime;
            mCrimeTitleTextView.setText(mCrime.getTitle());
            mCrimeDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View v)
        {
            Log.e("CrimeListFragment", "onClick activated");
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes)
        {
            super();
            mCrimes = crimes;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position)
        {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, viewGroup);
        }

        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }
    }
}
