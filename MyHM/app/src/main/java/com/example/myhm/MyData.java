package com.example.myhm;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class MyData extends Fragment {


    private TextView nome;
    private String n;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NOME = "nome";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mydata, container, false);

        nome = view.findViewById(R.id.nome1);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        n = sharedPreferences.getString(NOME, "");

        nome.setText(n);


        return view;
    }

}


