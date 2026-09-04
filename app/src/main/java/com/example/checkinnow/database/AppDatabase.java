package com.example.checkinnow.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.checkinnow.dao.LichChamCongDao;
import com.example.checkinnow.dao.NhatKyChamCongDao;
import com.example.checkinnow.model.LichChamCong;
import com.example.checkinnow.model.NhatKyChamCong;

@Database(
        entities = {
                LichChamCong.class,
                NhatKyChamCong.class
        },
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LichChamCongDao lichChamCongDao();

    public abstract NhatKyChamCongDao nhatKyChamCongDao();


    private static volatile AppDatabase INSTANCE;


    public static AppDatabase getDatabase(
            Context context
    ) {

        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE =
                            Room.databaseBuilder(
                                            context.getApplicationContext(),
                                            AppDatabase.class,
                                            "cham_cong.db"
                                    )
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }

        return INSTANCE;
    }
}