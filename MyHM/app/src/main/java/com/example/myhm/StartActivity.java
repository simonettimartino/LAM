package com.example.myhm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private Button avanti;
    private EditText nome;
    private String name;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NOME = "nome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        avanti = this.findViewById(R.id.buttonStart);
        nome = this.findViewById(R.id.nome);
        Toast.makeText(this, "Inserisci il tuo nome", Toast.LENGTH_SHORT).show();
        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nome.getText().toString().equals("")){
                    saveData();
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                } else
                    Toast.makeText(StartActivity.this, "Inserisci il tuo nome", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NOME, nome.getText().toString());
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

}