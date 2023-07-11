package com.ili.digital.assessmentproject.data.datasource.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.data.datasource.roomDB.dao.MarsPhotoDao

/**
 * Responsible for setting up the database and also
 * all the entities which are to be saved in DB as Table are defined here
 */
@Database(
    entities = [MarsPhoto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun marsPhotoDao(): MarsPhotoDao

    companion object {

        /**
         * For making the database if the database is not created it will create a database
         * if already created it will not create a new one
         */
        fun getAppDatabase(context: Context): AppDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "NASA_PHOTO_DATABASE"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }



    }
}