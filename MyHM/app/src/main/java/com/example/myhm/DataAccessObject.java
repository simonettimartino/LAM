package com.example.myhm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataAccessObject {
    @Insert
    void aggiungiReport(Reports reports);
    @Update
    void aggiornaReport(Reports reports);

    @Delete
    void rimuoviReport(Reports reports);

    @Query("select * from datiDB")
    public List<Reports> getDati();

    /*
    @Query("select * from reports where prioritaReport = :prio")
    public List<DatoUtente> getDatiFiltrati(int prio);
*/

   /* @Query("select data, count(*) from datiDB group by data ")
    public List<Reports> getDatiMedi();

    */
}

