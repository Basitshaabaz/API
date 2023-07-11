package com.ili.digital.assessmentproject.di

import android.content.Context
import com.ili.digital.assessmentproject.data.datasource.MarsPhotosDataSource
import com.ili.digital.assessmentproject.data.datasource.MarsPhotosDataSourceImpl
import com.ili.digital.assessmentproject.data.datasource.roomDB.AppDatabase
import com.ili.digital.assessmentproject.data.datasource.roomDB.dao.MarsPhotoDao
import com.ili.digital.assessmentproject.data.respositories.MarsPhotosRepository
import com.ili.digital.assessmentproject.data.respositories.MarsPhotosRepositoryImpl
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataModule {


    @Provides
    fun provideApiDataSource(apiService: ApiInterface): MarsPhotosDataSource {
        return MarsPhotosDataSourceImpl(apiService)
    }



    @Provides
    fun provideApiRepository(apiDataSource: MarsPhotosDataSource): MarsPhotosRepository {
        return MarsPhotosRepositoryImpl(apiDataSource)
    }

    /**
     * will be responsible for creating the [AppDatabase] and handling memory leaks
     */
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getAppDatabase(context)
    }


    /**
     * will be responsible for getting the [MarsPhotoDao] Dao through which the data is manipulated in
     * database (Room )
     */
    @Provides
    fun provideMarsPhotosDao(database: AppDatabase): MarsPhotoDao {
        return database.marsPhotoDao()
    }


}