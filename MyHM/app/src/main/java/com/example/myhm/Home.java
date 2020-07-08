package com.example.myhm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private CalendarView calendario;
    private List<Reports> exampleList, list;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String date;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendario = view.findViewById(R.id.calendarView);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month = month + 1;

                date = month + "/" + day + "/" + year;
                   // Toast.makeText(view.getContext(), date, Toast.LENGTH_LONG).show();
                new AsyncTaskRiceviReports().execute();
            }
        });



        return view;
    }


    public class AsyncTaskRiceviReports extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            list = MainActivity.appDatabase.dataAccessObject().getDati();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            exampleList = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).getData().equals(date)){
                    exampleList.add(list.get(i));
                }
            }

            mRecyclerView = getView().findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new RecycleAdapter(exampleList);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            if(exampleList.size() == 0) { Toast.makeText(getView().getContext(), "Non ci sono report per la data selezionata!", Toast.LENGTH_SHORT).show();}

        }
    }
}
