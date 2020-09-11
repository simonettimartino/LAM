package com.example.myhm;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class NewReport extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    public static final String SHARED_PREFS = "sharedPrefs";

    private Spinner spinnerPeso, spinnerTemperatura, spinnerGlicemia;
    private Button nuovoReport, aggiornaPriorita;
    private EditText peso, temperatura, glicemia, note;
    private int j, priorita, prioritaPeso, prioritaTemperatura, prioritaGlicemia;
    private TextView data;
    private String date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     View view = inflater.inflate(R.layout.fragment_newreport, container, false);

     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
             R.array.priority,
             android.R.layout.simple_spinner_item);
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

     nuovoReport = view.findViewById(R.id.buttonInviaR);
     SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedPreferences.edit();

     peso = view.findViewById(R.id.datoPeso);
     temperatura = view.findViewById(R.id.datoTemperatura);
     glicemia = view.findViewById(R.id.datoGlicemia);
     note = view.findViewById(R.id.datoNote);
     data = view.findViewById(R.id.datoData);

     spinnerPeso = view.findViewById(R.id.prioritaPeso);
     spinnerPeso.setAdapter(adapter);
     spinnerPeso.setOnItemSelectedListener(this);

     spinnerTemperatura = view.findViewById(R.id.prioritaTemperatura);
     spinnerTemperatura.setAdapter(adapter);
     spinnerTemperatura.setOnItemSelectedListener(this);

     spinnerGlicemia = view.findViewById(R.id.prioritaGlicemia);
     spinnerGlicemia.setAdapter(adapter);
     spinnerGlicemia.setOnItemSelectedListener(this);

        int kg = sharedPreferences.getInt("peso", 0);
        spinnerPeso.setSelection(kg - 1);
        int gc = sharedPreferences.getInt("temperatura", 0);
        spinnerTemperatura.setSelection(gc - 1);
        int gl = sharedPreferences.getInt("glicemia", 0);
        spinnerGlicemia.setSelection(gl - 1);


            data = (TextView) view.findViewById(R.id.datoData);

            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                    date = month + "/" + day + "/" + year;
                    data.setText(date);
                }
            };

            aggiornaPriorita = view.findViewById(R.id.newPrio);


        aggiornaPriorita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("peso", Integer.parseInt(spinnerPeso.getSelectedItem().toString()));
                editor.putInt("temperatura",Integer.parseInt(spinnerTemperatura.getSelectedItem().toString()));
                editor.putInt("glicemia",Integer.parseInt(spinnerGlicemia.getSelectedItem().toString()));
                editor.apply();
            }

        });

        nuovoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reports rep = new Reports();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();



                if (peso.getText().length() != 0 &&
                    temperatura.getText().length() != 0 &&
                    glicemia.getText().length() != 0 &&
                    data.getText().length() != 0) {

                        rep.setPeso(new Tupla(Double.parseDouble(peso.getText().toString()),Integer.parseInt(spinnerPeso.getSelectedItem().toString())));
                        editor.putInt("peso", Integer.parseInt(spinnerPeso.getSelectedItem().toString()));
                        rep.setTemperatura(new Tupla(Double.parseDouble(temperatura.getText().toString()),Integer.parseInt(spinnerTemperatura.getSelectedItem().toString())));
                        editor.putInt("temperatura",Integer.parseInt(spinnerTemperatura.getSelectedItem().toString()));
                        rep.setGlicemia(new Tupla(Double.parseDouble(glicemia.getText().toString()),Integer.parseInt(spinnerGlicemia.getSelectedItem().toString())));
                        editor.putInt("glicemia",Integer.parseInt(spinnerGlicemia.getSelectedItem().toString()));
                        editor.apply();

                        rep.setPriorita((Integer.parseInt(spinnerPeso.getSelectedItem().toString()) +
                                Integer.parseInt(spinnerTemperatura.getSelectedItem().toString()) +
                                Integer.parseInt(spinnerGlicemia.getSelectedItem().toString()))/3);


                        if (note.getText().length() != 0){
                            rep.setNote(note.getText().toString());
                        } else {
                            rep.setNote("");
                        }

                        rep.setData(date);

                        new AsyncTaskAggiungiReport().execute(rep);
                        peso.setText("");
                        temperatura.setText("");
                        glicemia.setText("");
                        data.setText("");
                        note.setText("");

                        int kg = sharedPreferences.getInt("peso", 0);
                        spinnerPeso.setSelection(kg - 1);
                        int gc = sharedPreferences.getInt("temperatura", 0);
                        spinnerTemperatura.setSelection(gc - 1);
                        int gl = sharedPreferences.getInt("glicemia", 0);
                        spinnerGlicemia.setSelection(gl - 1);
                       // Toast.makeText(view.getContext(), "Report inserito!", Toast.LENGTH_SHORT).show();
                } else {
                        Toast.makeText(view.getContext(), "Inserici tutti i dati!", Toast.LENGTH_SHORT).show();
                }

            }
        }

     );


     return view;
    }

    public class AsyncTaskAggiungiReport extends AsyncTask<Reports, Void, Void> {

        @Override
        protected Void doInBackground(Reports... report) {
            MainActivity.appDatabase.dataAccessObject().aggiungiReport(report[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Report aggiunto",
                    Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text = adapterView.getItemAtPosition(i).toString();
        j = Integer.parseInt(text);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
