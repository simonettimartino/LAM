package com.example.myhm;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
    //setto i valori (tabelle)
    @Entity(tableName = "Reports")
    public class Reports {

        @PrimaryKey(autoGenerate = true)
        private long id;

        @Embedded(prefix = "peso")
        private Nodo peso;
        @Embedded(prefix = "temperatura")
        private Nodo temperatura;
        @Embedded(prefix = "glicemia")
        private Nodo glicemia;
        @ColumnInfo(name = "data")
        private String data;
        @ColumnInfo(name = "note")
        private String note;
        private int priorita;




        public long getId() {
            return id;
        }

        public Nodo getPeso() {
            return peso;
        }

        public Nodo getGlicemia() {
            return glicemia;
        }

        public Nodo getTemperatura() {
            return temperatura;
        }

        public String getData() {
            return data;
        }

        public String getNote() {
            return note;
        }

        public int getPriorita() {
            return priorita;
        }

        public void setTemperatura(Nodo temperatura) {
            this.temperatura = temperatura;
        }

        public void setPeso(Nodo peso) {
            this.peso = peso;
        }
        public void setId(long id) {

            this.id = id;
        }
        public void setGlicemia(Nodo glicemia) {
            this.glicemia = glicemia;
        }
        public void setData(String data) {
            this.data = data;
        }

        public void setNote(String note) {

            this.note = note;
        }

        public void setPriorita(int priorita) {

            this.priorita = priorita;
        }
    }

