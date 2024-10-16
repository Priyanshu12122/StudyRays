package com.xerox.studyrays.network

import com.xerox.studyrays.model.akModel.aKVideoU.AkUrl
import com.xerox.studyrays.model.downloads.DownloadItem
import com.xerox.studyrays.model.downloads.KeyItem
import com.xerox.studyrays.model.khazanaModel.khazana.KhazanaItem
import com.xerox.studyrays.model.pwModel.CourseItem
import com.xerox.studyrays.model.pwModel.CourseItemX
import com.xerox.studyrays.model.pwModel.Lesson.LessonItem
import com.xerox.studyrays.model.pwModel.PromoItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.SearchOldItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.announcementsItem.AnnouncementItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.pwModel.fetchModel.FetchClassItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchExamItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchYearItem
import com.xerox.studyrays.model.pwModel.navbar.NavItem
import com.xerox.studyrays.model.pwModel.old.DppOldItem
import com.xerox.studyrays.model.pwModel.old.DppSolutionOldItem
import com.xerox.studyrays.model.pwModel.old.NoteOldItem
import com.xerox.studyrays.model.pwModel.priceItem.PriceItem
import com.xerox.studyrays.model.pwModel.statusItem.StatusItem
import com.xerox.studyrays.model.userModel.GetUserItem
import com.xerox.studyrays.model.userModel.PostUserItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.utils.Constants.AK_VIDEO_URL
import com.xerox.studyrays.utils.Constants.ALERT_URL
import com.xerox.studyrays.utils.Constants.ANNOUNCEMENTS_URL_OLD
import com.xerox.studyrays.utils.Constants.DOWNLOAD_URL
import com.xerox.studyrays.utils.Constants.DPP_GET_URL_OLD
import com.xerox.studyrays.utils.Constants.DPP_SOLUTION_GET_URL_OLD
import com.xerox.studyrays.utils.Constants.FETCH_FILTERS
import com.xerox.studyrays.utils.Constants.GET_URL
import com.xerox.studyrays.utils.Constants.GET_URL_OLD
import com.xerox.studyrays.utils.Constants.KEY_TASK_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_URL
import com.xerox.studyrays.utils.Constants.LEADERBOARD_URL
import com.xerox.studyrays.utils.Constants.LESSON_GET_URL_OLD
import com.xerox.studyrays.utils.Constants.NAV_URL
import com.xerox.studyrays.utils.Constants.NOTES_GET_URL_OLD
import com.xerox.studyrays.utils.Constants.PROMO_URL
import com.xerox.studyrays.utils.Constants.SEARCH_URL
import com.xerox.studyrays.utils.Constants.SEARCH_URL_OLD
import com.xerox.studyrays.utils.Constants.STATUS_URL
import com.xerox.studyrays.utils.Constants.SUBJECT_GET_URL_OLD
import com.xerox.studyrays.utils.Constants.TOTAL_FEE_URL
import com.xerox.studyrays.utils.Constants.USER_URL
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @GET(GET_URL)
    suspend fun getAllCourses(
        @Query("class", encoded = true) classValue: String
    ): List<CourseItemX>?

    @GET(GET_URL)
    suspend fun getAllCoursesPaginated(
        @Query("class", encoded = true) classValue: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<CourseItemX>?

    @GET(GET_URL_OLD)
    suspend fun getAllCoursesOld(
        @Query(
            "class",
            encoded = true
        ) classValue: String
    ): List<CourseItem>?

    @GET(SUBJECT_GET_URL_OLD)
    suspend fun getAllSubjects(@Query("course_id") courseId: String): BatchDetails

    @GET(LESSON_GET_URL_OLD)
    suspend fun getALlLessons(@Query("subject_id") subjectId: String): List<LessonItem>?

    @GET(NOTES_GET_URL_OLD)
    suspend fun getAllNotes(@Query("slug") slug: String): List<NoteOldItem>?

    @GET(DPP_GET_URL_OLD)
    suspend fun getAllDpp(@Query("slug") slug: String): List<DppOldItem>?

    @GET(DPP_SOLUTION_GET_URL_OLD)
    suspend fun getAllDppSolution(@Query("slug") slug: String): List<DppSolutionOldItem>?

    @GET(PROMO_URL)
    suspend fun getAllPromoItems(): List<PromoItem?>?

    @GET(SEARCH_URL)
    suspend fun getQuery(@Query("q") query: String): List<SearchItem>?

    @GET(SEARCH_URL_OLD)
    suspend fun getQueryOld(@Query("q") query: String): List<SearchOldItem>?

    @GET(KHAZANA_URL)
    suspend fun getKhazana(): List<KhazanaItem>

    @GET(NAV_URL)
    suspend fun getNavItems(): NavItem

    @GET(ALERT_URL)
    suspend fun getAlertItem(): AlertItem

    @GET(TOTAL_FEE_URL)
    suspend fun getTotalFee(): PriceItem

    @GET(AK_VIDEO_URL)
    suspend fun getAkVideoU(
        @Query("key") key: Int,
        @Query("lid") lid: String,
        @Query("bid") bid: Int,
        @Query("tid") tid: Int,
        @Query("sid") sid: Int,

        ): AkUrl

    @GET(DOWNLOAD_URL)
    suspend fun getDownloadLinks(): DownloadItem

    @GET(STATUS_URL)
    suspend fun getStatus(): StatusItem

    @GET(KEY_TASK_URL)
    suspend fun getKeyTask(): KeyItem

    @GET(ANNOUNCEMENTS_URL_OLD)
    suspend fun getAnnouncements(@Query("batch_id") batchId: String): List<AnnouncementItem>?

    @POST(USER_URL)
    suspend fun postUserData(@Body user: GetUserItem)

    @GET(USER_URL)
    suspend fun getUserData(): List<GetUserItem>?

    @PUT(USER_URL)
    suspend fun updateUser(@Body user: PostUserItem): PostUserItem

    @POST(LEADERBOARD_URL)
    suspend fun postLeaderBoardData(@Body item: LeaderBoardItem)

    @GET(LEADERBOARD_URL)
    suspend fun getLeaderBoardData(): List<LeaderBoardItem>

    @GET(LEADERBOARD_URL)
    suspend fun getCurrentUserLeaderBoardData(@Query("user_id") userId: String): LeaderBoardItem?

    @GET(FETCH_FILTERS)
    suspend fun fetchClasses(@Query("filterType") filterType: String = "class"): List<FetchClassItem>?

    @GET(FETCH_FILTERS)
    suspend fun fetchYears(
        @Query("filterType") filterType: String = "year",
        @Query("class") classValue: String,
    ): List<FetchYearItem>?


    @GET(FETCH_FILTERS)
    suspend fun fetchExams(
        @Query("filterType") filterType: String = "exam",
        @Query("class") classValue: String,
        @Query("year") year: String,
    ): List<FetchExamItem?>?


    @GET(FETCH_FILTERS)
    suspend fun fetchSearches(
        @Query("filterType") filterType: String = "search",
        @Query("searchQuery") searchQuery: String,
    ): List<CourseItemX>?

    @GET(FETCH_FILTERS)
    suspend fun fetchBatches(
        @Query("filterType") filterType: String = "batches",
        @Query("class") classValue: String,
        @Query("year") year: String,
        @Query("exam") exam: String,
    ): List<CourseItemX>?

    @GET(FETCH_FILTERS)
    suspend fun fetchAllBatches(
        @Query("filterType") filterType: String = "all_batches"
    ): List<CourseItemX>?


}