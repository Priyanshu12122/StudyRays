package com.xerox.studyrays.data

import com.xerox.studyrays.db.khazanaFavDb.KhazanaFav
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDao
import com.xerox.studyrays.model.khazanaModel.khazana.KhazanaItem
import com.xerox.studyrays.network.ApiService
import com.xerox.studyrays.network.EncryptedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KhazanaRepository @Inject constructor(
    private val api: ApiService,
    private val encryptedApi: EncryptedApiService,
    private val khazanaDao: KhazanaFavDao,

) {


    suspend fun checkIfPresentInKhazanaDb(externalId: String): Boolean {
        return khazanaDao.checkIfPresent(externalId)
    }


    suspend fun insertKhazanaFav(item: KhazanaFav) {
        khazanaDao.updateItem(item)
    }

    suspend fun deleteKhazanaFav(item: KhazanaFav) {
        khazanaDao.deleteItem(item)
    }

    fun getAllKhazanaCourses(): Flow<List<KhazanaFav?>?> {
        return khazanaDao.getAllCourses()
    }


    suspend fun getKhazana(): List<KhazanaItem> {
        return api.getKhazana()

    }


    suspend fun getKhazanaSubjects(slug: String): String {
        return encryptedApi.getKhazanaSubjects(slug)
    }


    suspend fun getKhazanaVideos(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): String {
        return encryptedApi.getKhazanaVideos(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )
    }


    suspend fun getKhazanaNotes(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): String {
        return encryptedApi.getKhazanaNotes(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )
    }

    suspend fun getKhazanaDpp(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): String {
        return encryptedApi.getKhazanaDpp(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )

    }


    suspend fun getKhazanaSolution(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): String {
        return encryptedApi.getKhazanaSolution(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )

    }


//    Khazana chapters

    suspend fun getKhazanaChapters(subjectId: String, chapterId: String): String {
        return encryptedApi.getKhazanaChapters(subjectId = subjectId, chapterId = chapterId)
    }

//    Khazana teachers details

    suspend fun getKhazanaTeachers(id: String): String {
        return encryptedApi.getKhazanaTeachers(id)
    }


}