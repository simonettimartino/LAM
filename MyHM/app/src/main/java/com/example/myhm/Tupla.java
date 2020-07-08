package com.example.myhm;

public class Tupla {
    double valore;
    int priorità;

    public Tupla(double valore, int priorità){
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
