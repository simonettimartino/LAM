package com.example.myhm.ui.main;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myhm.Reports;

import java.util.List;

//prende le entità dal DB, serve per accedere al database
@Dao
public interface DataAccessObject {
    @Insert
    public void aggiungiDatoUtente(Reports reports);
    @Update
    void aggiornaReport(Reports reports);

    @Delete
    void rimuoviReport(Reports reports);

    @Query("select * from Reports")
    public List<Reports> getDati();

    /*
    @Query("select * from reports where prioritaReport = :prio")
    public List<DatoUtente> getDatiFiltrati(int prio);
*/

}

