package com.example.myhm;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myhm.ui.main.DataAccessObject;

//classe che rappresenta il Database
@Database(entities = {Reports.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract DataAccessObject dataAccessObject();
}
