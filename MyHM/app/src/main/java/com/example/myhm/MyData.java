package com.example.myhm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class MyData extends Fragment {


    private TextView nome;
    private String n;
    private EditText h;
    private Button aggiorna, rep;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NOME = "nome";
    public static final String ORARIO = "orario";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mydata, container, false);

        nome = view.findViewById(R.id.nome1);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        n = sharedPreferences.getString(NOME, "");
        nome.setText(n);

        h = view.findViewById(R.id.editTextTime);
        h.setHint(sharedPreferences.getString(ORARIO, " hh : mm "));

        rep = view.findViewById(R.id.buttonRep);
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ReportsActivity.class));
            }
        });
        aggiorna = view.findViewById(R.id.aggiorna);
        aggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ORARIO, h.getText().toString());
                editor.apply();
                Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();

                h.setText("");
                h.setHint(sharedPreferences.getString(ORARIO, " hh : mm "));
            }
        });

        return view;
    }

}


