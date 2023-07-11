package com.ili.digital.assessmentproject.data.respositories

import com.ili.digital.assessmentproject.data.remote.ApiInterface
import com.ili.digital.assessmentproject.data.datasource.roomDB.dao.MarsPhotoDao
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CuriosityFragmentRepository @Inject constructor(
    private val apiClient: ApiInterface,
    private val marsPhotoDao: MarsPhotoDao
) {


}