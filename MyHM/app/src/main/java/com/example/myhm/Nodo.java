package com.example.myhm;

public class Nodo {
    double valore;
    int priorità;

    public Nodo(double valore, int priorità){
        this.valore = valore;
        this.priorità = priorità;
    }

    public double getValore() {
        return valore;
    }

    public int getPriorità() {
        return priorità;
    }

    public void setValore(double valore) {
        this.valore = valore;
    }

    public void setPriorità(int priorità) {
        this.priorità = priorità;
    }
}
