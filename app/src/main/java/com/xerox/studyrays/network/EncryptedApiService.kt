package com.xerox.studyrays.network

import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.Constants.AK_LESSON
import com.xerox.studyrays.utils.Constants.AK_NOTES
import com.xerox.studyrays.utils.Constants.AK_SUBJECT
import com.xerox.studyrays.utils.Constants.AK_VIDEO
import com.xerox.studyrays.utils.Constants.INDEX
import com.xerox.studyrays.utils.Constants.KHAZANA_CHAPTERS_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_COURSES_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_DPP_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_NOTES_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_SOLUTION_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_TEACHERS_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_VIDEO_URL
import com.xerox.studyrays.utils.Constants.VIDEO_GET_URL
import retrofit2.http.GET
import retrofit2.http.Query

interface EncryptedApiService {

    @GET(VIDEO_GET_URL)
    suspend fun getAllVideos(@Query("slug") slug: String): String

    @GET(KHAZANA_COURSES_URL)
    suspend fun getKhazanaSubjects(@Query("slug") slug: String): String

    @GET(KHAZANA_TEACHERS_URL)
    suspend fun getKhazanaTeachers(@Query("subject_id") id: String): String

    @GET(KHAZANA_CHAPTERS_URL)
    suspend fun getKhazanaChapters(
        @Query("subject_id") subjectId: String,
        @Query("chapter_id") chapterId: String,
    ): String

    @GET(KHAZANA_VIDEO_URL)
    suspend fun getKhazanaVideos(
        @Query("subject_id") subjectId: String,
        @Query("chapter_id") chapterId: String,
        @Query("topic_id") topicId: String,
    ): String

    @GET(KHAZANA_NOTES_URL)
    suspend fun getKhazanaNotes(
        @Query("subject_id") subjectId: String,
        @Query("chapter_id") chapterId: String,
        @Query("topic_id") topicId: String,
    ): String

    @GET(KHAZANA_DPP_URL)
    suspend fun getKhazanaDpp(
        @Query("subject_id") subjectId: String,
        @Query("chapter_id") chapterId: String,
        @Query("topic_id") topicId: String,
    ): String

    @GET(KHAZANA_SOLUTION_URL)
    suspend fun getKhazanaSolution(
        @Query("subject_id") subjectId: String,
        @Query("chapter_id") chapterId: String,
        @Query("topic_id") topicId: String,
    ): String


    @GET(Constants.COMMENTS_URL)
    suspend fun getComments(
        @Query("bname") bname: String,
        @Query("commentKey") commentKey: String,
        @Query("page") page: Int,
    ): String

    @GET(INDEX)
    suspend fun getIndex(): String

    @GET(AK_SUBJECT)
    suspend fun getAkSubjects(
        @Query("bid") id: Int
    ): String


    @GET(AK_LESSON)
    suspend fun getAkLessons(
        @Query("sid") sid: Int,
        @Query("bid") bid: Int
    ): String


    @GET(AK_VIDEO)
    suspend fun getAkVideo(
        @Query("sid") sid: Int,
        @Query("tid") tid: Int,
        @Query("bid") bid: Int
    ): String

    @GET(AK_NOTES)
    suspend fun getAkNotes(
        @Query("sid") sid: Int,
        @Query("tid") tid: Int,
        @Query("bid") bid: Int
    ): String

}