package com.example.myhm;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity(tableName = "datiDB")
    public class Reports {

        @PrimaryKey(autoGenerate = true)
        private long id;

        @Embedded(prefix = "peso")
        private Tupla peso;
        @Embedded(prefix = "temperatura")
        private Tupla temperatura;
        @Embedded(prefix = "glicemia")
        private Tupla glicemia;
        @ColumnInfo(name = "data")
        private String data;
        @ColumnInfo(name = "note")
        private String note;
        private int priorita;




        public long getId() {
            return id;
        }

        public Tupla getPeso() {
            return peso;
        }

        public Tupla getGlicemia() {
            return glicemia;
        }

        public Tupla getTemperatura() {
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

        public void setTemperatura(Tupla temperatura) {
            this.temperatura = temperatura;
        }

        public void setPeso(Tupla peso) {
            this.peso = peso;
        }
        public void setId(long id) {

            this.id = id;
        }
        public void setGlicemia(Tupla glicemia) {
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

