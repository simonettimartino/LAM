package com.example.myhm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner filtro;
    private Switch maggiore;
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
    private Integer prog = 0;
    private SwipeRefreshLayout swipeRefreshLayout;



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

        maggiore = findViewById(R.id.switch1);


        filtra.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = true;
                new AsyncTaskRiceviReports().execute();
            }
        });




        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                check = false;
                new AsyncTaskRiceviReports().execute();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new AsyncTaskRiceviReports().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                            if (maggiore.isChecked()) {
                                if (list.get(i).getPeso().getValore() > prog)
                                    exampleList.add(list.get(i));
                            }
                            if (!maggiore.isChecked()) {
                                if (list.get(i).getPeso().getValore() < prog)
                                    exampleList.add(list.get(i));
                            }
                            break;
                        case "temperatura":
                            if (maggiore.isChecked()) {
                                if (list.get(i).getTemperatura().getValore() > prog)
                                    exampleList.add(list.get(i));
                            }
                            if (!maggiore.isChecked() ){
                                if (list.get(i).getTemperatura().getValore() < prog)
                                    exampleList.add(list.get(i));
                            }
                            break;
                        case "glicemia":
                            if (maggiore.isChecked()) {
                                if (list.get(i).getGlicemia().getValore() > prog)
                                    exampleList.add(list.get(i));
                            }
                            if (!maggiore.isChecked()) {
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

            mRecyclerView = ReportsActivity.this.findViewById(R.id.RV);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(ReportsActivity.this);
            mAdapter = new RecycleAdapter(exampleList, interfaceClick);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(ReportsActivity.this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            Toast.makeText(ReportsActivity.this, "Data report: " + exampleList.get(position).getData(), Toast.LENGTH_SHORT).show();
                        }

                        @Override public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    })
            );

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
class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onLongItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }


    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
}
