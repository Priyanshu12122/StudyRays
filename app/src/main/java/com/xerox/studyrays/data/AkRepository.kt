package com.xerox.studyrays.data

import com.xerox.studyrays.model.akModel.aKVideoU.AkUrl
import com.xerox.studyrays.network.ApiService
import com.xerox.studyrays.network.EncryptedApiService
import javax.inject.Inject

class AkRepository @Inject constructor(
    private val api: ApiService,
    private val encryptedApi: EncryptedApiService,
) {
    //Ak

    suspend fun getIndex(): String {
        return encryptedApi.getIndex()
    }

    suspend fun getAkSubjects(id: Int): String {
        return encryptedApi.getAkSubjects(id)
    }

    suspend fun getAkLessons(sid: Int, bid: Int): String {
        return encryptedApi.getAkLessons(sid = sid, bid = bid)
    }

    suspend fun getAkVideos(sid: Int, tid: Int, bid: Int): String {
        return encryptedApi.getAkVideo(sid, tid, bid)
    }

    suspend fun getAkNotes(sid: Int, tid: Int, bid: Int): String {
        return encryptedApi.getAkNotes(sid, tid, bid)

    }

    suspend fun getAkVideoU(
        sid: Int,
        tid: Int,
        bid: Int,
        key: Int,
        lid: String,
    ): AkUrl {
        return api.getAkVideoU(key = key, lid = lid, tid = tid, bid = bid, sid = sid)
    }

}