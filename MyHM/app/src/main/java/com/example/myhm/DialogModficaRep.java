package com.example.myhm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;



public class DialogModficaRep extends AppCompatDialogFragment {

    private EditText editTextP;
    private EditText editTextT;
    private EditText editTextG;

    private Reports reportNew;

    public DialogModficaRep(Reports report){
        this.reportNew = report;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_modifica, null);
        editTextP = view.findViewById(R.id.modificaPeso);
        editTextT = view.findViewById(R.id.modificaTemperatura);
        editTextG = view.findViewById(R.id.modificaGlicemia);

        builder.setView(view)
                .setTitle("Modifica Report" + reportNew.getId())
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //System.out.println("Peso 0 " + reportNew.getPeso().getValore());

                        if (editTextP.getText().length() != 0 &&
                            editTextT.getText().length() != 0 &&
                            editTextG.getText().length() != 0 ) {
                           // System.out.println("Peso 1 " + reportNew.getPeso().getValore());

                            reportNew.setPeso(new Tupla(Double.parseDouble(editTextP.getText().toString()), reportNew.getPeso().getPriorità()));
                            reportNew.setTemperatura(new Tupla(Double.parseDouble(editTextT.getText().toString()), reportNew.getTemperatura().getPriorità()));
                            reportNew.setGlicemia(new Tupla(Double.parseDouble(editTextG.getText().toString()),reportNew.getGlicemia().getPriorità()));
                          //  System.out.println("Peso 2 " + reportNew.getPeso().getValore());

                            new AsyncTaskModificaReport().execute(reportNew);


                        }else {
                            Toast.makeText(getActivity(), "Inserici tutti i dati!", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

        return builder.create();
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
            Toast.makeText(getActivity(), "Report aggiornato", Toast.LENGTH_LONG).show();
        }

    }
}


