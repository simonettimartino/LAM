package com.example.myhm;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Grafici extends Fragment implements AdapterView.OnItemSelectedListener{

    private Button bP, bS;
    private Spinner spinner;
    private String graf;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_grafici, container, false);

        bP = v.findViewById(R.id.buttonGraficoPeso);
        bS = v.findViewById(R.id.buttonScat);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grafico,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = v.findViewById(R.id.spinnerGrafici);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        bP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), ActivityGraficoPeso.class));
            }

        });

        bS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), ScatterPlotActivity.class));
            }

        });


        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        graf = adapterView.getItemAtPosition(i).toString();
        ActivityGraficoPeso.setGrafico(graf);
        ScatterPlotActivity.setGrafico(graf);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
