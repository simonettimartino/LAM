package com.example.myhm;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Home extends Fragment {

    private CalendarView calendario;
    private List<Reports> exampleList, list;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String date;
    private RecycleAdapter.OnNoteListener interfaceClick;
    private static Home h;





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

    public void doRiceviRep(){
        new AsyncTaskRiceviReports().execute();
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
            if (exampleList.size()>1) {
                double mPeso = 0, mTemperatura = 0, mGlicemia = 0;
                int p1 = 0, p2 = 0, p3 = 0;
                for(int j = 0; j < exampleList.size(); j++){
                    mPeso = mPeso + exampleList.get(j).getPeso().getValore();
                    p1 = p1 + exampleList.get(j).getPeso().getPriorità();
                    mTemperatura = mTemperatura + exampleList.get(j).getTemperatura().getValore();
                    p2 = p2 + exampleList.get(j).getTemperatura().getPriorità();
                    mGlicemia = mGlicemia + exampleList.get(j).getGlicemia().getValore();
                    p3 = p3 + exampleList.get(j).getGlicemia().getPriorità();
                }
                int l = exampleList.size();
                Reports r = new Reports();
                r.setData(exampleList.get(0).getData());
                r.setPeso(new Tupla(Math.round(mPeso/l*100)/100,(int)p1/l));
                r.setTemperatura(new Tupla(Math.round(mTemperatura/l*100)/100, (int)p2/l));
                r.setGlicemia(new Tupla(Math.round(mGlicemia/l*100)/100, (int)p3/l));
                r.setNote("Report riassuntivo");
                exampleList.add(0, r);
            }
            mRecyclerView = getView().findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new RecycleAdapter(exampleList, interfaceClick);
            mRecyclerView.setLayoutManager(mLayoutManager);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(sipmleCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);


            if(exampleList.size() == 0) {
                Toast.makeText(getView().getContext(), "Non ci sono report per la data selezionata!", Toast.LENGTH_SHORT).show();
            }

        }
    }


    ItemTouchHelper.SimpleCallback sipmleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:

                    if(!exampleList.get(position).getNote().equals("Report riassuntivo")){
                        new AsyncTaskEliminaReport().execute(exampleList.get(position));
                    }
                    exampleList.remove(position);
                    mAdapter.notifyItemRemoved(position);

                    break;

                case ItemTouchHelper.RIGHT:

                    openDialog(exampleList.get(position));
                    new AsyncTaskRiceviReports().execute();

                    break;

            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.delite))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.modify))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void openDialog(Reports reports) {

        DialogModficaRep exampleDialog = new DialogModficaRep(reports);

        exampleDialog.show(getFragmentManager(), "example dialog");


    }






    public class AsyncTaskEliminaReport extends AsyncTask<Reports, Void, Void> {

        @Override
        protected Void doInBackground(Reports... report) {
            MainActivity.appDatabase.dataAccessObject().rimuoviReport(report[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Report rimosso",
                    Toast.LENGTH_LONG).show();
        }
    }

    public class AsyncTaskModificaReport extends AsyncTask<Reports, Void, Void> {

        @Override
        protected Void doInBackground(Reports... report) {
            MainActivity.appDatabase.dataAccessObject().aggiornaReport(report[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Report aggiornato",
                    Toast.LENGTH_LONG).show();

        }
    }

}
