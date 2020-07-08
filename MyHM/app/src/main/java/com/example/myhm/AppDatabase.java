package com.example.myhm;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Reports.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract DataAccessObject dataAccessObject();
}
