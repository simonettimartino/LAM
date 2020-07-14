package com.example.myhm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner maggiore, filtro;
    private List<Reports> exampleList, list;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String date;
    private RecycleAdapter.OnNoteListener interfaceClick;
    private TextView t;
    private SeekBar seekBar;
    private Boolean mag = true;
    private Boolean check = false;
    private String string;
    private Button filtra;
    private Integer prog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        t = findViewById(R.id.textView17);
        seekBar = findViewById(R.id.seekBar);
        filtra = findViewById(R.id.buttonFilta);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                t.setText( Integer.toString(progress) );
                prog = progress;

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ArrayAdapter<CharSequence> adapterFiltro = ArrayAdapter.createFromResource(this,
                R.array.grafico,
                android.R.layout.simple_spinner_item);
        adapterFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filtro = findViewById(R.id.spinnerFiltro);
        filtro.setAdapter(adapterFiltro);
        filtro.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterMaggiore = ArrayAdapter.createFromResource(this,
                R.array.maggiore,
                android.R.layout.simple_spinner_item);
        adapterMaggiore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        maggiore = findViewById(R.id.spinnerMaggiore);
        maggiore.setAdapter(adapterMaggiore);
        filtro.setOnItemSelectedListener(this);

        filtra.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = true;
                new AsyncTaskRiceviReports().execute();
            }
        });

        new AsyncTaskRiceviReports().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getItemAtPosition(i).toString().equals("maggiore"))
            mag = true;
        else if (adapterView.getItemAtPosition(i).toString().equals("maggiore"))
            mag = false;
        else
            string = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mag = true;
        string = "peso";

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
                if (check) {
                    switch (string) {
                        case "peso":
                            if (mag) {
                                if (list.get(i).getPeso().getValore() > prog)
                                    exampleList.add(list.get(i));
                            }
                            if (!mag) {
                                if (list.get(i).getPeso().getValore() < prog)
                                    exampleList.add(list.get(i));
                            }
                            break;
                        case "temperatura":
                            if (mag) {
                                if (list.get(i).getTemperatura().getValore() > prog)
                                    exampleList.add(list.get(i));
                            }
                            if (!mag ){
                                if (list.get(i).getTemperatura().getValore() < prog)
                                    exampleList.add(list.get(i));
                            }
                            break;
                        case "glicemia":
                            if (mag) {
                                if (list.get(i).getGlicemia().getValore() > prog)
                                    exampleList.add(list.get(i));
                            }
                            if (!mag) {
                                if (list.get(i).getGlicemia().getValore() < prog)
                                    exampleList.add(list.get(i));
                            }
                            break;
                    }
                } else


               // if(list.get(i).getData().equals(date)){
                    exampleList.add(list.get(i));
               // }
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
            mRecyclerView = ReportsActivity.this.findViewById(R.id.RV);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(ReportsActivity.this);
            mAdapter = new RecycleAdapter(exampleList, interfaceClick);
            mRecyclerView.setLayoutManager(mLayoutManager);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(sipmleCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);


            if(exampleList.size() == 0) {
                Toast.makeText(ReportsActivity.this, "Non ci sono report!", Toast.LENGTH_SHORT).show();
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ReportsActivity.this, R.color.delite))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ReportsActivity.this, R.color.modify))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void openDialog(Reports reports) {

        DialogModficaRep exampleDialog = new DialogModficaRep(reports);

        exampleDialog.show(getSupportFragmentManager(), "example dialog");

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
        Toast.makeText(ReportsActivity.this, "Report rimosso",
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
        Toast.makeText(ReportsActivity.this, "Report aggiornato",
                Toast.LENGTH_LONG).show();

    }
}

}
