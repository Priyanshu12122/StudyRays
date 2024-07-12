package com.xerox.studyrays.network

import com.xerox.studyrays.utils.Constants
import com.xerox.studyrays.utils.Constants.AK_LESSON
import com.xerox.studyrays.utils.Constants.AK_NOTES
import com.xerox.studyrays.utils.Constants.AK_SUBJECT
import com.xerox.studyrays.utils.Constants.AK_VIDEO
import com.xerox.studyrays.utils.Constants.ANNOUNCEMENTS_URL
import com.xerox.studyrays.utils.Constants.BATCH_DETAILS
import com.xerox.studyrays.utils.Constants.COMMENTS_URL
import com.xerox.studyrays.utils.Constants.DPP_GET_URL
import com.xerox.studyrays.utils.Constants.DPP_SOLUTION_GET_URL
import com.xerox.studyrays.utils.Constants.INDEX
import com.xerox.studyrays.utils.Constants.KHAZANA_CHAPTERS_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_COURSES_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_DPP_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_NOTES_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_SOLUTION_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_TEACHERS_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_VIDEO_URL
import com.xerox.studyrays.utils.Constants.LESSON_GET_URL
import com.xerox.studyrays.utils.Constants.NOTES_GET_URL
import com.xerox.studyrays.utils.Constants.TIMELINE_URL
import com.xerox.studyrays.utils.Constants.VIDEO_GET_URL
import com.xerox.studyrays.utils.Constants.VIDEO_GET_URL_OLD
import retrofit2.http.GET
import retrofit2.http.Query

interface EncryptedApiService {

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


    @GET(COMMENTS_URL)
    suspend fun getComments(
        @Query("video_external_id") externalId: String,
        @Query("topic_slug") topicSlug: String,
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


    //

    @GET(BATCH_DETAILS)
    suspend fun getBatchDetails(
        @Query("batch_id") batchId: String,
        @Query("batch_slug", encoded = true) batchSlug: String,
        @Query("class") classValue: String,

    ): String

    @GET(LESSON_GET_URL)
    suspend fun getLessons(
        @Query("batch_id") batchId: String,
        @Query("subject_slug") subjectSlug: String,
    ): String


    @GET(VIDEO_GET_URL_OLD)
    suspend fun getAllVideosOld(@Query("slug") slug: String): String

    @GET(VIDEO_GET_URL)
    suspend fun getAllVideos(
        @Query("batch_id") batchId: String,
        @Query("subject_slug") subjectSlug: String,
        @Query("topic_slug") topicSlug: String,
    ): String

    @GET(NOTES_GET_URL)
    suspend fun getAllNotes(
        @Query("batch_id") batchId: String,
        @Query("subject_slug") subjectSlug: String,
        @Query("topic_slug") topicSlug: String,
    ): String

    @GET(DPP_GET_URL)
    suspend fun getAllDpp(
        @Query("batch_id") batchId: String,
        @Query("subject_slug") subjectSlug: String,
        @Query("topic_slug") topicSlug: String,
    ): String

    @GET(DPP_SOLUTION_GET_URL)
    suspend fun getAllDppSolution(
        @Query("batch_id") batchId: String,
        @Query("subject_slug") subjectSlug: String,
        @Query("topic_slug") topicSlug: String,
    ): String


    @GET(ANNOUNCEMENTS_URL)
    suspend fun getAnnouncements(@Query("batch_id") batchId: String): String

    @GET(TIMELINE_URL)
    suspend fun getTimeline(@Query("video_external_id") externalId: String): String

}